package com.ab.sc.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ab.sc.dao.UserDao;
import com.ab.sc.dto.AuthenticationRequest;
import com.ab.sc.response.AuthResponse;
import com.ab.sc.utils.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService {

	private final AuthenticationManager authenticationManager;
	private final UserDao userDao;
	private final JwtUtils jwtUtils;

	public AuthResponse authenticate(@RequestBody AuthenticationRequest request) throws Exception {
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		UserDetails user = userDao.findUserByEmail(request.getEmail());
		if (user != null) {
			return jwtUtils.generateAuthResponse(user);
		}

		throw new UsernameNotFoundException("User Not Found with userId");
	}

	public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

		final String refreshToken = request.getHeader("Authorization");
		final String userEmail;
		final String jwtToken;

		if (refreshToken == null || !refreshToken.startsWith("Bearer")) {
			throw new RuntimeException("Refresh token is missing");
		}

		jwtToken = refreshToken.substring(7);
		userEmail = jwtUtils.extractUsername(jwtToken);

		UserDetails user = userDao.findUserByEmail(userEmail);

		if (user != null) {
			AuthResponse refreshTokenResponse = jwtUtils.generateAuthResponse(user);
			refreshTokenResponse.setRefreshToken(refreshToken.substring(7));
			return refreshTokenResponse;
		}

		throw new RuntimeException("Refresh token is missing");
	}

}

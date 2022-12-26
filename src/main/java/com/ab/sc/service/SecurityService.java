package com.ab.sc.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService {
	
	private final AuthenticationManager authenticationManager;
	private final UserDao userDao;
	private final JwtUtils jwtUtils;
	
	public AuthResponse authenticate(@RequestBody AuthenticationRequest request) throws Exception{
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		
		UserDetails user = userDao.findUserByEmail(request.getEmail());
		if(user != null) {
			return jwtUtils.generateAuthResponse(user);
		}
		
		throw new UsernameNotFoundException("User Not Found with userId");
	}

}

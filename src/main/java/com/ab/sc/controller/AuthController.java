package com.ab.sc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ab.sc.dao.UserDao;
import com.ab.sc.dto.AuthenticationRequest;
import com.ab.sc.entity.Users;
import com.ab.sc.response.AuthResponse;
import com.ab.sc.service.SecurityService;
import com.ab.sc.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	
	/*private final SecurityService seurityService;
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthenticationRequest request) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(seurityService.authenticate(request));
	}*/
	
	private final AuthenticationManager authenticationManager;
	private final UserDao userDao;
	private final JwtUtils jwtUtils;
	
	@PostMapping("/authenticate")
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
	
	@GetMapping("/get")
	public ResponseEntity<UserDetails> get() {
		return ResponseEntity.ok().body(userDao.findUserByEmail("John"));
	}
	
	/*@GetMapping("/get")
	public ResponseEntity<List<Users>> getUsers(){
		return ResponseEntity.ok().body(userDao.finall());
	}*/

}

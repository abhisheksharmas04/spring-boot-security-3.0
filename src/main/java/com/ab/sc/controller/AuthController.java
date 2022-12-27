package com.ab.sc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ab.sc.dto.AuthenticationRequest;
import com.ab.sc.response.AuthResponse;
import com.ab.sc.service.SecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final SecurityService seurityService;

	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthenticationRequest request) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(seurityService.authenticate(request));
	}

	@PostMapping("/refresh/token")
	public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		return ResponseEntity.status(HttpStatus.OK).body(seurityService.refreshToken(request, response));
	}
}

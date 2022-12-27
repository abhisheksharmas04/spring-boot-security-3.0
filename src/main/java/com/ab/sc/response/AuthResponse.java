package com.ab.sc.response;

import java.util.Date;

import lombok.Data;

@Data
public class AuthResponse {
	
	private String accessToken;
	private String refreshToken;
	private Date accessTokenExpireIn;
	private Date refreshTokenExpireIn;

}

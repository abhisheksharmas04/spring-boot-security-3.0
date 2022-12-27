package com.ab.sc.utils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ab.sc.response.AuthResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
	private String SECRET_KEY = "secret";
	
	@Value("${token.expiry.time}")
	private Long tokenExpiryTime;
	
	@Value("${refresh.token.expiry.time}")
	private Long refreshTokenExpiryTime;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		authorities.forEach( grantAuthority -> {claims.put("role", grantAuthority.getAuthority());});
		return createToken(claims, userDetails.getUsername());
	}
	
	public AuthResponse generateAuthResponse(UserDetails userDetails) {
		AuthResponse authResponse = new AuthResponse();
		authResponse.setAccessToken(generateToken(userDetails));
		authResponse.setAccessTokenExpireIn(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(tokenExpiryTime)));
		authResponse.setRefreshToken(generateRefreshToken(userDetails));
		authResponse.setRefreshTokenExpireIn(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(refreshTokenExpiryTime)));
		
		return authResponse;
	}
	
	public String generateRefreshToken(UserDetails userDetails) {
		return createRefreshToken(userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(tokenExpiryTime)))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	private String createRefreshToken(String subject) {
		return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(refreshTokenExpiryTime)))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}

package com.msauth.security;


import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.msauth.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	/* Generates the token */
	public String generateToken(Authentication authentication) {

		User usuario = (User) authentication.getPrincipal();

		Date now = new Date();
		Date exp = new Date(now.getTime() + JwtProperties.EXPIRATION_TIME);

		return Jwts.builder().setIssuer("IRS").setSubject(usuario.getId().toString()).setIssuedAt(new Date())
				.setExpiration(exp).signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET).compact();
	}
	
	/* Checks the token is valid */
	public boolean isTokenValid(String token) {
		try {
			Jwts.parser().setSigningKey(JwtProperties.SECRET).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/* Search token to id */
	public Integer getTokenId(String token) {
		Claims body = Jwts.parser().setSigningKey(JwtProperties.SECRET).parseClaimsJws(token).getBody();
		return Integer.valueOf(body.getSubject());
	}
}
	
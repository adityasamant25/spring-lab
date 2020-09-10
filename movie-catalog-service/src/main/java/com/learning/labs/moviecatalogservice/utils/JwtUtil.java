package com.learning.labs.moviecatalogservice.utils;

import java.util.Date;

import org.apache.cxf.rs.security.jose.jwa.SignatureAlgorithm;
import org.apache.cxf.rs.security.jose.jws.HmacJwsSignatureProvider;
import org.apache.cxf.rs.security.jose.jws.HmacJwsSignatureVerifier;
import org.apache.cxf.rs.security.jose.jws.JwsCompactProducer;
import org.apache.cxf.rs.security.jose.jws.JwsJwtCompactConsumer;
import org.apache.cxf.rs.security.jose.jws.JwsJwtCompactProducer;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.apache.cxf.rs.security.jose.jwt.JwtToken;
import org.apache.cxf.rs.security.jose.jwt.JwtUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

	private String SECRET_KEY = "secret";

	public String generateToken(UserDetails userDetails) {

		JwtClaims claims = new JwtClaims();
		claims.setIssuer(userDetails.getUsername());
		claims.setSubject(userDetails.getUsername());
		claims.setIssuedAt(new Date().getTime());
		claims.setExpiryTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10).getTime());

		return createToken(claims);
	}

	private String createToken(JwtClaims claims) {
		JwtToken jwtToken = new JwtToken(claims);
		JwsCompactProducer jwsProducer = new JwsJwtCompactProducer(jwtToken);
		String jwsSequence = jwsProducer.signWith(new HmacJwsSignatureProvider(SECRET_KEY, SignatureAlgorithm.HS256));
		return jwsSequence;
	}

	public Boolean validateToken(String token) {
		JwsJwtCompactConsumer jwsConsumer = new JwsJwtCompactConsumer(token);
		return jwsConsumer.verifySignatureWith(new HmacJwsSignatureVerifier(SECRET_KEY, SignatureAlgorithm.HS256));
	}

	public String extractUsername(String token) {
		JwsJwtCompactConsumer jwsConsumer = new JwsJwtCompactConsumer(token);
		return jwsConsumer.getJwtToken().getClaim("sub").toString();
	}

	public void isTokenExpired(String token) {
		JwtUtils.validateJwtExpiry(new JwsJwtCompactConsumer(token).getJwtToken().getClaims(), 0, false);
		;
	}
}

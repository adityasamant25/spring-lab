package com.learning.labs.moviecatalogservice.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learning.labs.moviecatalogservice.models.AuthenticationRequest;
import com.learning.labs.moviecatalogservice.models.AuthenticationResponse;
import com.learning.labs.moviecatalogservice.services.MyUserDetailsService;
import com.learning.labs.moviecatalogservice.utils.JwtUtil;

@RestController
public class JwtAuthenticationResource {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	MyUserDetailsService userDetailsService;

	@Autowired
	JwtUtil jwtUtil;

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
			@RequestBody AuthenticationRequest authenticationRequest) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password");
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		String jwt = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}

package com.learning.labs.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.learning.labs.moviecatalogservice.models.AuthenticationRequest;
import com.learning.labs.moviecatalogservice.models.AuthenticationResponse;
import com.learning.labs.moviecatalogservice.models.CatalogItem;
import com.learning.labs.moviecatalogservice.models.UserRating;
import com.learning.labs.moviecatalogservice.services.MovieInfo;
import com.learning.labs.moviecatalogservice.services.MyUserDetailsService;
import com.learning.labs.moviecatalogservice.services.UserRatingInfo;
import com.learning.labs.moviecatalogservice.utils.JwtUtil;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	MovieInfo movieInfo;

	@Autowired
	UserRatingInfo userRatingInfo;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	MyUserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;

	@RequestMapping(method = RequestMethod.GET, value = "/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

		UserRating userRating = userRatingInfo.getUserRating(userId);

		return userRating.getUserRatings().stream().map(rating -> movieInfo.getCatalogItem(rating))
				.collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password");
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}

//For reactive programming
/*
 * @Autowired private WebClient.Builder webClientBuilder;
 */

/*
 * Movie movie =
 * webClientBuilder.build().get().uri("http://localhost:8082/movies/" +
 * rating.getMovieId()).retrieve() .bodyToMono(Movie.class).block();
 */

package com.learning.labs.ratingsdataservice.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.labs.ratingsdataservice.models.MovieRating;
import com.learning.labs.ratingsdataservice.models.UserRating;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsResource {

	@GetMapping("/{movieId}")
	public MovieRating getRating(@PathVariable("movieId") String movieId) {

		return new MovieRating(movieId, 4);
	}

	@GetMapping("/users/{userId}")
	public UserRating getUserRating(@PathVariable("userId") String userId) {

		List<MovieRating> movieRatings = Arrays.asList(new MovieRating("100", 3), new MovieRating("200", 4));

		UserRating userRating = new UserRating();
		userRating.setUserRatings(movieRatings);
		
		return userRating;
	}

}

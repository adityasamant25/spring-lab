package com.learning.labs.ratingsdataservice.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.learning.labs.ratingsdataservice.models.Rating;
import com.learning.labs.ratingsdataservice.models.UserRating;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsResource {

	@RequestMapping(method = RequestMethod.GET, value="/{movieId}")
	public Rating getRating(@PathVariable("movieId") String movieId) {

		return new Rating(movieId, 4);
	}

	@RequestMapping(method = RequestMethod.GET, value="/users/{userId}")
	public UserRating getUserRating(@PathVariable("userId") String userId) {

		List<Rating> ratings = Arrays.asList(new Rating("100", 3), new Rating("200", 4));

		UserRating userRating = new UserRating();
		userRating.setUserRatings(ratings);
		
		return userRating;
	}

}

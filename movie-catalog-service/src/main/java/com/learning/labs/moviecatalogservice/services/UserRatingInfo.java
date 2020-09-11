package com.learning.labs.moviecatalogservice.services;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.learning.labs.moviecatalogservice.models.MovieRating;
import com.learning.labs.moviecatalogservice.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class UserRatingInfo {
	
	Logger logger = LoggerFactory.getLogger(UserRatingInfo.class);
	
	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "getFallbackUserRating")
	public UserRating getUserRating(String userId) {
		return restTemplate.getForObject("https://ratings-data-service/ratingsdata/users/" + userId,
				UserRating.class);
	}

	public UserRating getFallbackUserRating(String userId) {
		logger.debug("Entered fallback for user rating {}", userId);
		UserRating userRating = new UserRating();
		userRating.setUserRatings(Arrays.asList(new MovieRating("0", 0)));
		return userRating;
	}
}

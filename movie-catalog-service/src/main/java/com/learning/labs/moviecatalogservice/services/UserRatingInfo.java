package com.learning.labs.moviecatalogservice.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.learning.labs.moviecatalogservice.models.Rating;
import com.learning.labs.moviecatalogservice.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class UserRatingInfo {
	
	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "getFallbackUserRating")
	public UserRating getUserRating(String userId) {
		UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId,
				UserRating.class);
		return userRating;
	}

	public UserRating getFallbackUserRating(String userId) {
		UserRating userRating = new UserRating();
		userRating.setUserRatings(Arrays.asList(new Rating("0", 0)));
		return userRating;
	}
}

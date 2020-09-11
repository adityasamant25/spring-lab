package com.learning.labs.ratingsdataservice.models;

import java.util.List;

public class UserRating {

	private List<MovieRating> userRatings;

	public UserRating() {
	}

	public UserRating(List<MovieRating> userRatings) {
		this.userRatings = userRatings;
	}

	public List<MovieRating> getUserRatings() {
		return userRatings;
	}

	public void setUserRatings(List<MovieRating> userRatings) {
		this.userRatings = userRatings;
	}

}

package com.learning.labs.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.learning.labs.moviecatalogservice.models.CatalogItem;
import com.learning.labs.moviecatalogservice.models.UserRating;
import com.learning.labs.moviecatalogservice.services.MovieInfo;
import com.learning.labs.moviecatalogservice.services.UserRatingInfo;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	MovieInfo movieInfo;

	@Autowired
	UserRatingInfo userRatingInfo;

	@RequestMapping(method = RequestMethod.GET, value="/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

		UserRating userRating = userRatingInfo.getUserRating(userId);

		return userRating.getUserRatings().stream().map(rating -> movieInfo.getCatalogItem(rating))
				.collect(Collectors.toList());
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

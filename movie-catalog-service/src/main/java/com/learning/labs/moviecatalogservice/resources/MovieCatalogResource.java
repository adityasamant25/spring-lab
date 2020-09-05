package com.learning.labs.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.learning.labs.moviecatalogservice.models.CatalogItem;
import com.learning.labs.moviecatalogservice.models.Movie;
import com.learning.labs.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

		UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId,
				UserRating.class);

		return userRating.getUserRatings().stream().map(rating -> {
			// for each movie ID, call movie info service and get details
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
			// put them all together
			return new CatalogItem(movie.getName(), "Desc", rating.getRating());
		}).collect(Collectors.toList());
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

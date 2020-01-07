package com.vs.moviectalogservice.resources;

import com.vs.moviectalogservice.model.CatalogItem;
import com.vs.moviectalogservice.model.Movie;
import com.vs.moviectalogservice.model.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final static String DESC = "desc";
    private final RestTemplate restTemplate;
    private final WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalogItem(@PathVariable("userId") String userId) {



        List<Rating> ratings = Arrays.asList(
                new Rating("123", 4),
                new Rating("345", 5)
        );

        return ratings.stream().map(rating -> {
                    // Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

                    Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://localhost:8082/movies/" + rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();

            assert movie != null;
            return CatalogItem.builder()
                            .name(movie.getName())
                            .desc(DESC)
                            .rating(Integer.parseInt(rating.getMovieId()))
                            .build(); })
                .collect(Collectors.toList()
        );

    }
}

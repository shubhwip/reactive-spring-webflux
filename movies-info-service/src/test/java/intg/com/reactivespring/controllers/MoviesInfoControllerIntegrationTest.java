package com.reactivespring.controllers;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Make sure to use profiles defined other than in application.yml
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@AutoConfigureWebTestClient
class MoviesInfoControllerIntegrationTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    private WebTestClient webTestClient;

    private static String MOVIE_INFO_ENDPOINT = "/v1/movieinfos";

    @BeforeEach
    void setup() {
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        // Use Blocking calls only in your tests
        movieInfoRepository.saveAll(movieInfos)
                .blockLast();
    }

    @Test
    void addMovieInfo() {
        var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        webTestClient
                .post()
                .uri(MOVIE_INFO_ENDPOINT)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert savedMovieInfo != null;
                    assert savedMovieInfo.getMovieInfoId() != null;
                });
    }

    @Test
    void getAllMovies() {
        webTestClient
                .get()
                .uri(MOVIE_INFO_ENDPOINT)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void getmoviebyid() {
        var movieInfoId = "abc";
        webTestClient
                .get()
                .uri(MOVIE_INFO_ENDPOINT + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                // Approach 1
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");
        // Approach 2
//                .expectBody(MovieInfo.class)
//                .consumeWith(movieInfoEntityExchangeResult -> {
//                    var responseBody = movieInfoEntityExchangeResult.getResponseBody();
//                    assert responseBody != null;
//                });
    }

    @Test
    void updateMovieById() {
        var movieInfoId = "abc";
        var movieInfo = new MovieInfo(null, "Batman Begins2",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        // Getting movie info by id
        webTestClient
                .put()
                .uri(MOVIE_INFO_ENDPOINT + "/{id}", movieInfoId)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assert responseBody.getName().equals("Batman Begins2");
                });
    }

    @Test
    void deleteMovieInfo() {
        // given
        var movieInfoId = "abc";

        // when
        webTestClient
                .delete()
                .uri(MOVIE_INFO_ENDPOINT + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .isNoContent();

        // then
        webTestClient
                .get()
                .uri(MOVIE_INFO_ENDPOINT + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assert responseBody == null;
                });

        // getting all movie infos
        webTestClient
                .get()
                .uri(MOVIE_INFO_ENDPOINT)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(2);
    }


    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }
}
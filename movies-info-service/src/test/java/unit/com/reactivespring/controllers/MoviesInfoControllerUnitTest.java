package com.reactivespring.controllers;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MoviesInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
public class MoviesInfoControllerUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    // Injecting a spring bean into spring application context
    // Different from @Mock
    // When you write unit test and spin up application context you need @MockBean

    @MockBean
    private MoviesInfoService moviesInfoService;

    private static String MOVIES_INFO_URL = "/v1/movieinfos";

    @Test
    void addMovieInfo() {

        // given
        var movieInfo = new MovieInfo("mockId", "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        // when
        when(moviesInfoService.addMovieInfo(isA(MovieInfo.class))).thenReturn(Mono.just(movieInfo));

        // then
        webTestClient
                .post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert savedMovieInfo != null;
                    assert savedMovieInfo.getMovieInfoId() != null;
                    Assertions.assertEquals("mockId", savedMovieInfo.getMovieInfoId());
                });
    }

    @Test
    void addMovieInfo_Annotation() {

        // given
        var movieInfo = new MovieInfo("mockId", "",
                -2005, List.of(""), LocalDate.parse("2005-06-15"));

        // when
        when(moviesInfoService.addMovieInfo(isA(MovieInfo.class))).thenReturn(Mono.just(movieInfo));

        // then
        webTestClient
                .post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var response = stringEntityExchangeResult.getResponseBody();
                    System.out.println(response);
                    var expectedErrorMessage = "movieInfo.cast must be present,movieInfo.name must be present,movieInfo.year must be present and positive";
                    Assertions.assertEquals(expectedErrorMessage, response);
                });
//                .expectBody(MovieInfo.class)
//                .consumeWith(movieInfoEntityExchangeResult -> {
//                    var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
//                    assert savedMovieInfo != null;
//                    assert savedMovieInfo.getMovieInfoId() != null;
//                    Assertions.assertEquals("mockId", savedMovieInfo.getMovieInfoId());
//                });
    }


    @Test
    void getAllMoviesInfo() {
        // given
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        // when
        when(moviesInfoService.getAllMoviesInfo()).thenReturn(Flux.fromIterable(movieInfos));

        // then
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void getMovieInfoById() {
        // given
        var movieInfoId = "abc";
        var movieInfo = new MovieInfo(movieInfoId, "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        // when
        when(moviesInfoService.getMovieInfoById(movieInfoId)).thenReturn(Mono.just(movieInfo));

        // then
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL + "/{id}", movieInfoId)
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
        var movieInfo = new MovieInfo(null, "Batman Begins",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));


        // when
        when(moviesInfoService.updateMovieInfo(isA(MovieInfo.class), isA(String.class))).thenReturn(Mono.just(new MovieInfo(movieInfoId, "Batman Begins",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"))));


        // Getting movie info by id
        webTestClient
                .put()
                .uri(MOVIES_INFO_URL + "/{id}", movieInfoId)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assert responseBody.getName().equals("Batman Begins");
                    assert responseBody.getMovieInfoId().equals("abc");
                });
    }

    @Test
    void deleteMovieInfo() {
        // given
        var movieInfoId = "abc";

        // when
        when(moviesInfoService.deleteMovieInfo(movieInfoId)).thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri(MOVIES_INFO_URL + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

}

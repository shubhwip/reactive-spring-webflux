package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
// This was able to not run only because of below property
// https://www.udemy.com/course/build-reactive-restful-apis-using-spring-boot-webflux/learn/lecture/28042406#questions/16421672
// https://stackoverflow.com/questions/70047380/enableautoconfigurationexclude-on-tests-failed-in-spring-boot-2-6-0
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@ActiveProfiles("test")
class MovieInfoRepositoryIntegrationTest {


    @Autowired
    MovieInfoRepository movieInfoRepository;

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
    void findAll() {
        var moviesInfoFlux = movieInfoRepository.findAll().log();
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        var moviesInfoMono = movieInfoRepository.findById("abc").log();
        StepVerifier.create(moviesInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals("Dark Knight Rises", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {
        var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        var moviesInfoMono = movieInfoRepository.save(movieInfo);

        StepVerifier.create(moviesInfoMono)
                .assertNext(mInfo -> {
                    assertEquals("Batman Begins1", mInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void updateMovieInfo() {
        // given
        var moviesInfoMono = movieInfoRepository.findById("abc").block();
        moviesInfoMono.setYear(2011);

        // when
        var mInfoUpdate = movieInfoRepository.save(moviesInfoMono);

        // then
        StepVerifier.create(mInfoUpdate)
                .assertNext(mInfo -> {
                    assertEquals(2011, mInfo.getYear());
                })
                .verifyComplete();
    }

    @Test
    void deleteMovieInfo() {

        // when
        // .block is necessary
        var moviesInfoMono = movieInfoRepository.deleteById("abc").block();
        var mInfoUpdate = movieInfoRepository.findAll().log();

        // then
        StepVerifier.create(mInfoUpdate)
                .expectNextCount(2)
                .verifyComplete();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }
}
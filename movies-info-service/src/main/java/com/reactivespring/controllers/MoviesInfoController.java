package com.reactivespring.controllers;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MoviesInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class MoviesInfoController {

    private MoviesInfoService movieInfoService;

    public MoviesInfoController(MoviesInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.addMovieInfo(movieInfo);
    }

    @GetMapping("/movieinfos")
    public Flux<MovieInfo> getAllMovies() {
        return movieInfoService.getAllMoviesInfo();
    }

//    @GetMapping("/movieinfos/{id}")
//    public Mono<MovieInfo> getMovieInfoById(@PathVariable String id) {
//        return movieInfoService.getMovieInfoById(id);
//    }

    @GetMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable String id) {
        return movieInfoService.getMovieInfoById(id)
                .map(ResponseEntity::ok).switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

//    @PutMapping("/movieinfos/{id}")
//    // If we don't specify @ResponseStatus explictly then it is 200 ok always
//    public Mono<MovieInfo> updateMovieInfo(@RequestBody MovieInfo movieInfo, @PathVariable String id) {
//        return movieInfoService.updateMovieInfo(movieInfo, id);
//    }

    @PutMapping("/movieinfos/{id}")
    // If we don't specify @ResponseStatus explictly then it is 200 ok always
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfo(@RequestBody MovieInfo movieInfo, @PathVariable String id) {
        return movieInfoService.updateMovieInfo(movieInfo, id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfo(@PathVariable String id) {
        return movieInfoService.deleteMovieInfo(id);
    }

}

package com.reactivespring.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.time.Duration;

@RestController
public class FluxAndMonoController {

    // data handling and request handling are totally done in separate thread

    // By default these APIs are non blocking and asynchronous
    @GetMapping("/flux")
    public Flux<Integer> flux() {
        // How to return flux from DB or External API
        return Flux.just(1, 2, 3);
    }

    @GetMapping("/mono")
    public Mono<String> mono() {
        // How to return flux from DB or External API
        return Mono.just("Hello World");
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> stream() {
        // How to return flux from DB or External API
        return Flux.interval(Duration.ofSeconds(1)).log();
    }

}

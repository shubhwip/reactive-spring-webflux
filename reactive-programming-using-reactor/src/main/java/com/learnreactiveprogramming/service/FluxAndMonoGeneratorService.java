package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    // namesFlux is a flux publisher
    public Flux<String> namesFlux() {
        // Flux.fromIterable is a data source
        return Flux.fromIterable(List.of("Shubham", "Alex", "Ali", "Josh")).log();
    }

    public Flux<String> namesFluxWithMap() {
        // Flux.fromIterable is a data source
        return Flux.fromIterable(List.of("Shubham", "Alex", "Ali", "Josh"))
                .map(String::toUpperCase)
                .log();
    }

    public Flux<String> namesFluxWithMapAndFilter() {
        // Flux.fromIterable is a data source
        return Flux.fromIterable(List.of("Shubham", "Alex", "Ali", "Josh"))
                // One to one transformation // simple transformation
                // Used for synchronous operations
                .map(String::toUpperCase)
                .filter(name -> name.length() > 3)
                .log();
    }

    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitStringWithDelay(String name) {
        var charArray = name.split("");
        Random random = new Random();
        var duration = random.nextInt(1000);
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(duration));
    }

    public Flux<String> namesFluxWithFlatMap() {
        // Flux.fromIterable is a data source
        return Flux.fromIterable(List.of("Shubham", "Alex", "Ali", "Josh"))
                .map(String::toUpperCase)
                // one to N transformation
                // can be used for async operations
                .flatMap(name -> splitString(name));
    }

    public Flux<String> namesFluxTransform() {
        // when you have a common functionality across a project
        // then you can declare it once and reuse it across the project
        Function<Flux<String>, Flux<String>> filterMap = name ->  name.map(String::toUpperCase)
                // one to N transformation
                // can be used for async operations
                .flatMap(s -> splitString(s));

        // Flux.fromIterable is a data source
        return Flux.fromIterable(List.of("Shubham", "Alex", "Ali", "Josh"))
                .transform(filterMap);
    }

    public Flux<String> namesFluxTransformWithDefault() {
        // when you have a common functionality across a project
        // then you can declare it once and reuse it across the project
        Function<Flux<String>, Flux<String>> filterMap = name ->  name.map(String::toUpperCase)
                // one to N transformation
                // can be used for async operations
                .filter(s -> s.length() > 8)
                .flatMap(s -> splitString(s));

        // Flux.fromIterable is a data source
        // Flux.empty()
        return Flux.fromIterable(List.of("Shubham", "Alex", "Ali", "Josh"))
                .transform(filterMap)
                // returns default value if flux is empty
                .defaultIfEmpty("default");
    }

    public Flux<String> namesFluxTransformWithSwitchIfEmpty() {
        // when you have a common functionality across a project
        // then you can declare it once and reuse it across the project
        Function<Flux<String>, Flux<String>> filterMap = name ->  name.map(String::toUpperCase)
                // one to N transformation
                // can be used for async operations
                .filter(s -> s.length() > 8)
                .flatMap(s -> splitString(s));

        var defaultFlux = Flux.just("defaultab").transform(filterMap);

        // Flux.fromIterable is a data source
        // Flux.empty()
        return Flux.fromIterable(List.of("Shubham", "Alex", "Ali", "Josh"))
                .transform(filterMap)
                // Accepts Publisher Mono/Flux
                .switchIfEmpty(defaultFlux);
    }

    public Flux<String> namesFluxWithFlatMapAsynchronous() {
        // Flux.fromIterable is a data source
        return Flux.fromIterable(List.of("Shubham", "Alex", "Ali", "Josh"))
                .map(String::toUpperCase)
                // can be used for async operations
                // Time will be taken lesser than concatMap
                .flatMap(name -> splitStringWithDelay(name));
    }

    public Flux<String> namesFluxWithConcatMapAsynchronous() {
        // Flux.fromIterable is a data source
        return Flux.fromIterable(List.of("Shubham", "Alex", "Ali", "Josh"))
                .map(String::toUpperCase)
                // can be used for async operations
                // Preserving the order, concat map can be used
                // Time will be taken more than flatMap
                .concatMap(name -> splitStringWithDelay(name));
    }

    public Flux<String> namesFluxWithMapWithImmutability() {
        // Flux.fromIterable is a data source
        var namesFlux = Flux.fromIterable(List.of("Shubham", "Alex", "Ali", "Josh"));
        // names is immutable
        namesFlux.map(String::toUpperCase);
        return namesFlux;
    }

    // namesMono is a mono publisher
    public Mono<String> namesMono() {
        return Mono.just("Jayesh").log();
    }

    public Mono<List<String>> namesMonoFlatMapWithFilter() {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .flatMap(this::splitStringMono);
    }

    // When you're doing mono transformation
    // and want to return Flux
    // Anytime your mono pipeline return flux we can use flatMapMany
    public Flux<String> namesMonoFlatMapManyWithFilter() {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .flatMapMany(this::splitString);
    }

    public Mono<List<String>> splitStringMono(String name) {
        var charArray = name.split("");
        var list = List.of(charArray);
        return Mono.just(list);
    }

    public Flux<String> exploreConcat() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return Flux.concat(abcFlux, defFlux);
    }

    public Flux<String> exploreConcatWith() {
        var aMonp = Mono.just("A");
        var bMonp = Mono.just("B");
        return aMonp.concatWith(bMonp);
    }

    public Flux<String> exploreMerge() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return Flux.merge(abcFlux, defFlux);
    }

    public Flux<String> exploreMergeWith() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return abcFlux.mergeWith(defFlux);
    }

    public Flux<String> exploreMergeWithMono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        return aMono.mergeWith(bMono);
    }

    // Todo : What's point of concat then ?
    public Flux<String> exploreMergeSequntial() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return Flux.mergeSequential(abcFlux, defFlux);
    }

    public Flux<String> exploreZip() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return Flux.zip(abcFlux, defFlux, (a,b) -> (a+b));
    }

    public Flux<String> exploreZipWith() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return abcFlux.zipWith(defFlux, (a,b) -> (a+b));
    }

    public Mono<String> exploreZipWithMono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("D");
        return aMono.zipWith(bMono)
                .map(t1 -> t1.getT1() + t1.getT2());
    }

    public Flux<String> exploreZipTuples() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        var _123Flux = Flux.just("1", "2", "3");
        var _456Flux = Flux.just("4", "5", "6");
        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4());
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        // Using flux
        // You always need to subscribe to Flux/Mono Reactive types in order to further process them
        // 1
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name-> {
                    System.out.println("Name is : " + name);
                });
        // 2
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name-> System.out.println(name));
        // 3
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(System.out::println);

        // Using Mono
        fluxAndMonoGeneratorService.namesMono()
                .subscribe(System.out::println);
        fluxAndMonoGeneratorService.namesMono()
                .subscribe(name -> {
                    System.out.println("Name is: " + name);
                });
    }
}

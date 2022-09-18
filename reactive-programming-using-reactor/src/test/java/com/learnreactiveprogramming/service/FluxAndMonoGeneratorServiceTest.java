package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService;

    @BeforeEach
    public void setUp() {
        fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
    }

    @Test
    public void testNamesFlux() {
        //given

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesFlux)
                .expectNext("Shubham", "Alex", "Ali", "Josh")
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxWithMap() {
        //given

        //when
        var namesFluxWithMap = fluxAndMonoGeneratorService.namesFluxWithMap();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesFluxWithMap)
                .expectNext("SHUBHAM", "ALEX", "ALI", "JOSH")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxWithFlatMap() {
        //given

        //when
        var namesFluxWithMap = fluxAndMonoGeneratorService.namesFluxWithFlatMap();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesFluxWithMap)
                .expectNext("S","H", "U","B","H","A","M", "A","L","E","X", "A", "L", "I", "J","O","S","H")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxWithTransform() {
        //given

        //when
        var namesFluxTransform = fluxAndMonoGeneratorService.namesFluxTransform();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesFluxTransform)
                .expectNext("S","H", "U","B","H","A","M", "A","L","E","X", "A", "L", "I", "J","O","S","H")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxWithTransformWithDefault() {
        //given

        //when
        var namesFluxTransformWithDefault = fluxAndMonoGeneratorService.namesFluxTransformWithDefault();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesFluxTransformWithDefault)
                //.expectNext("S","H", "U","B","H","A","M", "A","L","E","X", "A", "L", "I", "J","O","S","H")
                .expectNext("default")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxWithTransformWithSwitchIfEmpty() {
        //given

        //when
        var namesFluxTransformWithSwitchIfEmpty = fluxAndMonoGeneratorService.namesFluxTransformWithSwitchIfEmpty();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesFluxTransformWithSwitchIfEmpty)
                //.expectNext("S","H", "U","B","H","A","M", "A","L","E","X", "A", "L", "I", "J","O","S","H")
                .expectNext("D", "E", "F", "A", "U", "L", "T", "A", "B")
                //.expectNextCount(4)
                .verifyComplete();
    }



    @Test
    public void testNamesFluxWithFlatMapAsync() {
        //given

        //when
        var namesFluxWithFlatMapAsynchronous = fluxAndMonoGeneratorService.namesFluxWithFlatMapAsynchronous();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesFluxWithFlatMapAsynchronous)
                //.expectNext("S","H", "U","B","H","A","M", "A","L","E","X", "A", "L", "I", "J","O","S","H")
                .expectNextCount(18)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxWithConcatMapAsync() {
        //given

        //when
        var namesFluxWithConcatMapAsynchronous = fluxAndMonoGeneratorService.namesFluxWithConcatMapAsynchronous();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesFluxWithConcatMapAsynchronous)
                .expectNext("S","H", "U","B","H","A","M", "A","L","E","X", "A", "L", "I", "J","O","S","H")
                //.expectNextCount(18)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxWithMapAndFilter() {
        //given

        //when
        var namesFluxWithMap = fluxAndMonoGeneratorService.namesFluxWithMapAndFilter();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesFluxWithMap)
                .expectNext("SHUBHAM", "ALEX", "JOSH")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxWithMapWithImmutability() {
        //given

        //when
        var namesFluxWithMapWithImmutability = fluxAndMonoGeneratorService.namesFluxWithMapWithImmutability();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesFluxWithMapWithImmutability)
                .expectNext("Shubham", "Alex", "Ali", "Josh")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesMonoWithFlatMap() {
        //given

        //when
        var namesMonoFlatMapWithFilter = fluxAndMonoGeneratorService.namesMonoFlatMapWithFilter();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesMonoFlatMapWithFilter)
                .expectNext(List.of("A", "L", "E", "X"))
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesMonoWithFlatMapMany() {
        //given

        //when
        var namesMonoFlatMapManyWithFilter = fluxAndMonoGeneratorService.namesMonoFlatMapManyWithFilter();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(namesMonoFlatMapManyWithFilter)
                .expectNext("A", "L", "E", "X")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxConcat() {
        //given

        //when
        var exploreConcat = fluxAndMonoGeneratorService.exploreConcat();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(exploreConcat)
                .expectNext("A", "B", "C", "D", "E", "F")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxConcatWith() {
        //given

        //when
        var exploreConcatWith = fluxAndMonoGeneratorService.exploreConcatWith();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(exploreConcatWith)
                .expectNext("A", "B")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxMerge() {
        //given

        //when
        var exploreMerge = fluxAndMonoGeneratorService.exploreMerge();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(exploreMerge)
                .expectNext("A", "D", "B", "E", "C", "F")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxMergeWith() {
        //given

        //when
        var exploreMergeWith = fluxAndMonoGeneratorService.exploreMergeWith();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(exploreMergeWith)
                .expectNext("A", "D", "B", "E", "C", "F")
                //.expectNextCount(4)
                .verifyComplete();
    }


    @Test
    public void testNamesFluxMergeWithMono() {
        //given

        //when
        var exploreMergeWithMono = fluxAndMonoGeneratorService.exploreMergeWithMono();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(exploreMergeWithMono)
                .expectNext("A", "B")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxMergeSequential() {
        //given

        //when
        var exploreMergeSequntial = fluxAndMonoGeneratorService.exploreMergeSequntial();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(exploreMergeSequntial)
                .expectNext("A", "B", "C", "D", "E", "F")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxZip() {
        //given

        //when
        var exploreZip = fluxAndMonoGeneratorService.exploreZip();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(exploreZip)
                .expectNext("AD", "BE", "CF")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxZipWith() {
        //given

        //when
        var exploreZipWith = fluxAndMonoGeneratorService.exploreZipWith();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(exploreZipWith)
                .expectNext("AD", "BE", "CF")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesFluxZipTuples() {
        //given

        //when
        var exploreZipTuples = fluxAndMonoGeneratorService.exploreZipTuples();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(exploreZipTuples)
                .expectNext("AD14", "BE25", "CF36")
                //.expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testNamesMonoZipWith() {
        //given

        //when
        var exploreZipWithMono = fluxAndMonoGeneratorService.exploreZipWithMono();

        //then
        //create automatically calls subscriber function
        StepVerifier.create(exploreZipWithMono)
                .expectNext("AD")
                //.expectNextCount(4)
                .verifyComplete();
    }


}
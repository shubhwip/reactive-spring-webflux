# Learnings

## flatMaps in Mono
- Use it when transformation returns Mono
- Returns a Mono<T>
- In enterprise application, Use flatMap if the transformation involves making a REST API call or any kind of functionality that can be done asynchronously.

## flatMapMap in Mono
-

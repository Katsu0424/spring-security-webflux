package com.example.springsecuritywebflux.router

import kotlinx.coroutines.delay
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import reactor.core.publisher.Mono

@Component
class HelloHandler {

    fun hello(request: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().bodyValue("Hello")

    suspend fun blockingHello(request: ServerRequest): ServerResponse {
        delay(1000)
        return ServerResponse.ok().bodyValueAndAwait("Blocking Hello")
    }
}
package com.example.springsecuritywebflux.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.router

@Configuration
class RouterConfig(private val helloHandler: HelloHandler) {

    @Bean
    fun helloRoutes(): RouterFunction<ServerResponse> = router {
        GET("/hello", helloHandler::hello)
    }

    // coRouterでサスペンド関数を扱う
    @Bean
    fun coHelloRoutes(): RouterFunction<ServerResponse> = coRouter {
        GET("/blocking/hello", helloHandler::blockingHello)
    }
}
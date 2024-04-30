//package com.example.springsecuritywebflux.controller
//
//import kotlinx.coroutines.delay
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//class HelloController {
//
//    @GetMapping("/hello")
//    fun hello(): String {
//        return "Hello"
//    }
//
//    @GetMapping("/blocking/hello")
//    suspend fun blockingHello(): String {
//        delay(1000)
//        return "Blocking Hello"
//    }
//}
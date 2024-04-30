package com.example.springsecuritywebflux.security

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.codec.HttpMessageReader
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.reactive.function.BodyExtractor
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.*

/**
 * Json形式で認証パラメーターを受け取るためのコンバーター
 */
class JsonBodyAuthenticationConverter(
    val messageReaders: List<HttpMessageReader<*>>
) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        return BodyExtractors.toMono(AuthenticationInfo::class.java)
            .extract(exchange.request, object : BodyExtractor.Context {
                override fun messageReaders(): List<HttpMessageReader<*>> = messageReaders
                override fun serverResponse(): Optional<ServerHttpResponse> = Optional.of(exchange.response)
                override fun hints(): Map<String, Any> = mapOf()
            })
            .map { it.toToken() }
    }
}

//認証リクエストの形式
data class AuthenticationInfo(
    @JsonProperty("mail_address")
    val mailAddress: String,
    val password: String
) {
    fun toToken() = UsernamePasswordAuthenticationToken(this.mailAddress, this.password)
}
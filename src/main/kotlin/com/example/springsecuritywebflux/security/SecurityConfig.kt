package com.example.springsecuritywebflux.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.server.WebFilter

@Configuration
@EnableWebFluxSecurity // SpringSecurityの有効化
@EnableReactiveMethodSecurity // APIのエンドポイントごとの認可設定を有効化
class SecurityConfig {
    @Bean
    fun securityFilterChain(
        http: ServerHttpSecurity,
        authenticationManager: ReactiveAuthenticationManager,
        serverCodecConfigurer: ServerCodecConfigurer
    ): SecurityWebFilterChain {
        // 認証設定
        val authenticationFilter = authenticationWebFilter(
            authenticationManager,
            serverCodecConfigurer,
            ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/login")
        )
        http
            .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .csrf{it.disable()} // CSRF保護を無効化
            // ログインのみ認証不要。その他は全て認証必要
            .authorizeExchange{ authorize -> authorize
                .pathMatchers("/login").permitAll()
                .anyExchange().authenticated()
            }
            // 認可で拒否した場合の処理
            .exceptionHandling{ exception -> exception
                .authenticationEntryPoint(AuthenticationEntryPoint())
            }

        return http.build()
    }

    @Bean
    fun authenticationManager(
        userDetailsService: HelloUserDetailsService
    ): UserDetailsRepositoryReactiveAuthenticationManager {
        // authenticationManagerとしてどの認証プロセスを使用するのか設定する
        // 今回はUserDetailsRepositoryReactiveAuthenticationManager
        return UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService).apply {
            setPasswordEncoder(BCryptPasswordEncoder())
        }
    }

    private fun authenticationWebFilter(
        authenticationManager: ReactiveAuthenticationManager,
        serverCodecConfigurer: ServerCodecConfigurer,
        loginPath: ServerWebExchangeMatcher
    ): WebFilter {
        return AuthenticationWebFilter(authenticationManager).apply {
            // 認証処理を行うリクエスト
            setRequiresAuthenticationMatcher(loginPath)
            // 認証情報の抽出方法
            // ここで設定された情報をAuthenticationManagerが受け取ってUserDetailsService(HelloUserDetailsService)を通してユーザーの詳細をロードする
            setServerAuthenticationConverter(JsonBodyAuthenticationConverter(serverCodecConfigurer.readers))
            // 認証成功・失敗時の処理
            setAuthenticationSuccessHandler(AuthenticationSuccessHandler())
            setAuthenticationFailureHandler(AuthenticationFailureHandler())
            // セキュリティコンテキストの保存方法
            setSecurityContextRepository(WebSessionServerSecurityContextRepository())
        }
    }
}
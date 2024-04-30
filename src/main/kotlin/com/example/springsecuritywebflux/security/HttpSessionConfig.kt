package com.example.springsecuritywebflux.security

import org.springframework.context.annotation.Configuration
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession

@Configuration
@EnableRedisWebSession // WebFluxはこっち
// @EnableRedisHttpSession
class HttpSessionConfig {
    // redisのカスタマイズがしたければ入れる
    // 今は不要なのでコメントアウト
//    @Bean
//    fun lettuceConnectionFactory(): LettuceConnectionFactory {
//        // RedisStandaloneConfigurationを作成して設定を適用
//        val config = RedisStandaloneConfiguration()
//        config.hostName = "custom-host"
//        config.port = 1234
//        config.password = RedisPassword.of("securepassword")
//
//        // LettuceConnectionFactoryに設定を適用
//        return LettuceConnectionFactory(config)
//    }
}
package com.example.springsecuritywebflux.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class HelloUserDetailsService : ReactiveUserDetailsService {
    // setServerAuthenticationConverterでsetされた情報をusername(実際はmailAddress)として取り出している
    override fun findByUsername(username: String): Mono<UserDetails> {
        // 本当はここでちゃんとDBからユーザー情報を取得する
        // usernameでDBからpass(ハッシュ化)を取得
        val passwordEncoder = BCryptPasswordEncoder()
        val pass = passwordEncoder.encode("password")

        // usernameとDBから取得したpassをHelloUserDetailsで返す
        // AuthenticationManagerがこのHelloUserDetailsとsetServerAuthenticationConverterでsetされた情報をチェックしてくれるっぽい
        return Mono.just(HelloUserDetails(username, pass))
    }
}

data class HelloUserDetails(val email: String, val pass: String) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String {
        return this.pass
    }

    override fun getUsername(): String {
        return this.email
    }

    // アカウントの有効期限
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    // アカウントロック状態かどうか
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    // 権限に有効期限の設定があるかどうか
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    // 有効なユーザーかどうか
    override fun isEnabled(): Boolean {
        return true
    }
}
package dev.procrastineyaz.trellospring.security.config

import dev.procrastineyaz.trellospring.security.TokenAuthenticationConverter
import dev.procrastineyaz.trellospring.security.jwt.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration(
    private val reactiveUserDetailsService: ReactiveUserDetailsServiceImpl,
    private val tokenProvider: TokenProvider
) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity, entryPoint: UnauthorizedAuthEntryPoint): SecurityWebFilterChain {
        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .logout().disable()

        http
            .exceptionHandling()
            .authenticationEntryPoint(entryPoint)
            .and()
            .addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
            .authorizeExchange()
            .pathMatchers("/api/login").permitAll()
            .pathMatchers(HttpMethod.POST, "/api/users").permitAll()
            .anyExchange().authenticated()

        return http.build()
    }

    @Bean
    fun webFilter(): AuthenticationWebFilter {
        return AuthenticationWebFilter(repositoryReactiveAuthenticationManager()).apply {
            setServerAuthenticationConverter(TokenAuthenticationConverter(tokenProvider))
            setRequiresAuthenticationMatcher(JWTHeadersExchangeMatcher())
        }
    }

    @Bean
    fun repositoryReactiveAuthenticationManager(): JWTReactiveAuthenticationManager {
        return JWTReactiveAuthenticationManager(reactiveUserDetailsService, passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
package mireya.sanchez.apispring.config.security

import mireya.sanchez.apispring.config.security.jwt.JwtAuthenticationFilter
import mireya.sanchez.apispring.config.security.jwt.JwtAuthorizationFilter
import mireya.sanchez.apispring.config.security.jwt.JwtTokenUtils
import mireya.sanchez.apispring.services.UsuariosService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig
@Autowired constructor(
    private val service: UsuariosService,
    private val jwtTokenUtils: JwtTokenUtils
) {
    @Bean
    fun authManager(http: HttpSecurity): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )
        authenticationManagerBuilder.userDetailsService(service)
        return authenticationManagerBuilder.build()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val authenticationManager = authManager(http)

        http
            .csrf()
            .disable()
            .exceptionHandling()
            .and()

            .authenticationManager(authenticationManager)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()

            .requestMatchers("/error/**").permitAll()

            .requestMatchers("/apiSpring/**").permitAll()
            .requestMatchers("/usuarios/login", "/usuarios/register").permitAll()
            .requestMatchers("/usuarios/list").hasRole("ADMIN")

            .anyRequest().authenticated()

            .and()

            .addFilter(JwtAuthenticationFilter(jwtTokenUtils, authenticationManager))
            .addFilter(JwtAuthorizationFilter(jwtTokenUtils, service, authenticationManager))

        return http.build()
    }
}
package com.example.departemplespring.config.security

import com.example.departemplespring.config.security.token.AuthenticationFilter
import com.example.departemplespring.config.security.token.AuthorizationFilter
import com.example.departemplespring.config.security.token.JwtTokenUtil
import com.example.departemplespring.services.usuarios.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig
@Autowired constructor(
    private val service: UsuarioService,
    private val jwtTokenUtil: JwtTokenUtil
) {

    @Bean
    fun authManager(http: HttpSecurity): AuthenticationManager{
        val authenticationManager = http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )
        authenticationManager.userDetailsService(service)
        return authenticationManager.build()
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
            .requestMatchers("/**").permitAll()
            .requestMatchers("/usuarios/login", "/usuarios/register").permitAll()
//            .requestMatchers("/empleados/**").permitAll()
//            .requestMatchers(HttpMethod.GET,"/departamentos/**").permitAll()
            .requestMatchers(HttpMethod.DELETE,"/departamentos").hasRole("ADMIN")
            .requestMatchers("/usuarios/me").hasAnyRole("ADMIN","USER")
            .requestMatchers("/storage/**").hasAnyRole("ADMIN","USER")
            .anyRequest().authenticated()
            .and()
            .addFilter(AuthenticationFilter(jwtTokenUtil, authenticationManager))
            .addFilter(AuthorizationFilter(jwtTokenUtil, service, authenticationManager))

        return http.build()
    }
}
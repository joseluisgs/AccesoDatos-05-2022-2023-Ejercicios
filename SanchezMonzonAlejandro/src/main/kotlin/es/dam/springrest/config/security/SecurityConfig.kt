package es.dam.springrest.config.security

import es.dam.springrest.config.security.jwt.JWTAuthenticationFilter
import es.dam.springrest.config.security.jwt.JWTAuthorizationFilter
import es.dam.springrest.config.security.jwt.JWTUtils
import es.dam.springrest.services.usuarios.UsuariosServices
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
    private val usuariosService: UsuariosServices,
    private val jwtUtils: JWTUtils
) {
    @Bean
    fun authManager(http: HttpSecurity): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )
        authenticationManagerBuilder.userDetailsService(usuariosService)
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
            .requestMatchers("/repaso/**").permitAll()
            .requestMatchers("/usuarios/login", "/usuarios/register").permitAll()
            .requestMatchers("/usuarios/list").hasRole("ADMIN")
            .requestMatchers("/departamentos/delete").hasRole("ADMIN")
            .anyRequest().authenticated()

            .and()
            .addFilter(JWTAuthenticationFilter(jwtUtils, authenticationManager))
            .addFilter(JWTAuthorizationFilter(jwtUtils, usuariosService, authenticationManager))

        return http.build()
    }
}
package daniel.rodriguez.ejerciciospring.config.security

import daniel.rodriguez.ejerciciospring.config.security.jwt.JwtAuthenticationFilter
import daniel.rodriguez.ejerciciospring.config.security.jwt.JwtAuthorizationFilter
import daniel.rodriguez.ejerciciospring.config.security.jwt.JwtTokensUtils
import daniel.rodriguez.ejerciciospring.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
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
    private val service: UserService,
    private val jwtTokensUtils: JwtTokensUtils
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
            // Abro todos los endpoints y luego ya voy cerrandolos,
            // porque por algun motivo sin esta linea no va aunque los intente abrir luego.
            // Asique se queda abierto por defecto y luego cierro lo que necesite estar cerrado.
            .requestMatchers("/**").permitAll()

            .requestMatchers(HttpMethod.DELETE, "/ejercicioSpring/users/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/ejercicioSpring/empleados/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/ejercicioSpring/departamentos/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/ejercicioSpring/storage/**").hasRole("ADMIN")
            .anyRequest().authenticated()

            .and()
            .addFilter(JwtAuthenticationFilter(jwtTokensUtils, authenticationManager)) // Para autenticar
            .addFilter(JwtAuthorizationFilter(jwtTokensUtils, service, authenticationManager)) // Para autorizar

        return http.build()
    }
}
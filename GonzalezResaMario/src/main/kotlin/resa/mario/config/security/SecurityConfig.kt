package resa.mario.config.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import resa.mario.config.security.jwt.JwtAuthenticationFilter
import resa.mario.config.security.jwt.JwtAuthorizationFilter
import resa.mario.config.security.jwt.JwtTokenUtils
import resa.mario.services.usuario.UsuarioServiceImpl

/**
 * Clase de configuracion de la seguridad de Spring; se encarga de aplicar filtros en los END_POINTS
 *
 * @property service
 * @property jwtTokenUtils
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig
@Autowired constructor(
    private val service: UsuarioServiceImpl,
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

            // Para token JWT
            .authenticationManager(authenticationManager)

            // No usamos estado de sesion
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            // Aceptamos request del tipo Http
            .authorizeHttpRequests()

            // Spring desplaza a esta ruta todos los errores y excepciones, asi podemos mostrarlas en vez de un Forbidden
            .requestMatchers("/error/**").permitAll()

            // Permitimos el acceso a swagger
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()

            // Permitimos el acceso a la API
            .requestMatchers("/api/**").permitAll()

            // Permitimos el acceso sin autenticacion ni verificacion a las siguientes rutas
            .requestMatchers("/usuarios/login", "/usuarios/register").permitAll()

            // Para listar a los usuarios, se requeria un rol de ADMIN
            .requestMatchers("/usuarios/list").hasRole("ADMIN")

            // Como se pide en el enunciado, tanto aqui como en el propio controlador de departamentos
            .requestMatchers("/departamentos/delete").hasRole("ADMIN")

            .anyRequest().authenticated()

            .and()

            .addFilter(JwtAuthenticationFilter(jwtTokenUtils, authenticationManager)) // Autenticacion
            .addFilter(JwtAuthorizationFilter(jwtTokenUtils, service, authenticationManager)) // Autorizacion

        return http.build()
    }
}
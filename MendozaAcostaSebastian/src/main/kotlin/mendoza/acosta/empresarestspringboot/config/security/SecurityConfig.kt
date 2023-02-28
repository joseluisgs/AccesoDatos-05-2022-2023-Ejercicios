package mendoza.acosta.empresarestspringboot.config.security

import mendoza.acosta.empresarestspringboot.config.security.jwt.JwtAuthenticationFilter
import mendoza.acosta.empresarestspringboot.config.security.jwt.JwtAuthorizationFilter
import mendoza.acosta.empresarestspringboot.config.security.jwt.JwtToken
import mendoza.acosta.empresarestspringboot.service.usuario.UsuarioService
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
    private val usuarioService: UsuarioService,
    private val jwtToken: JwtToken
) {
    @Bean
    fun authManager(http: HttpSecurity): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )
        authenticationManagerBuilder.userDetailsService(usuarioService)
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
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
            .requestMatchers("/api/**")
            .permitAll()
            .requestMatchers("usuarios/login", "usuarios/register").permitAll()
            .requestMatchers("usuarios/me").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.GET, "usuarios/list").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .addFilter(JwtAuthenticationFilter(jwtToken, authenticationManager))
            .addFilter(JwtAuthorizationFilter(jwtToken, usuarioService, authenticationManager))
        return http.build()
    }
}



























package dev.axgr.mfa

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfig {

  //  @formatter:off
    @Bean
    fun chain(http: HttpSecurity, config: AuthenticationConfiguration, provider: OneTimePasswordAuthProvider, users: UserService): SecurityFilterChain {
        return http
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .httpBasic(Customizer.withDefaults())
            .csrf { it.disable() }
            .authenticationProvider(DaoAuthenticationProvider().apply {
                setUserDetailsService(users)
                setPasswordEncoder(encoder())
            })
            .authenticationProvider(provider)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterAfter(
                OneTimePasswordAuthFilter(config.authenticationManager, users),
                BasicAuthenticationFilter::class.java
            )
            .build()
    }
  //  @formatter:on

  @Bean
  fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

}

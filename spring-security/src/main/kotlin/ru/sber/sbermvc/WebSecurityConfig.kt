package ru.sber.sbermvc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    lateinit var userDetailsService: UserDetailsService

    @Bean
    fun argon2PasswordEncoder(): Argon2PasswordEncoder {
        return Argon2PasswordEncoder()  //should default config be enough?
    }

    @Override
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/app/**").authenticated()
            .antMatchers("/api/**").hasRole("API")
            .anyRequest().permitAll()
        .and()
            .formLogin()
            .loginPage("/login")
            .usernameParameter("login")
            .passwordParameter("password")
            .defaultSuccessUrl("/app/list")
            .permitAll()
        .and()
            .logout().permitAll()
        .and()
            .csrf().disable()
    }

    @Bean
    fun customAuthenticationManager(): AuthenticationManager {
        return authenticationManager()
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(argon2PasswordEncoder())
    }
}
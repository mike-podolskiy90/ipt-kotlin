package org.gbif.ipt.config

import org.gbif.ipt.service.IptAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {
    http
      .csrf().disable()
      .authorizeRequests()
      .antMatchers("/", "/login", "/home", "/styles/**", "/images/**", "/js/**").permitAll()
      .anyRequest().authenticated()
      .and()
      .formLogin()
      .loginPage("/login")
      .usernameParameter("email")
      .passwordParameter("password")
      .and()
      .logout()
  }

  @Primary
  @Bean
  fun passwordEncoder(): PasswordEncoder? {
    return BCryptPasswordEncoder()
  }

  @Primary
  @Bean
  fun authProvider(): IptAuthenticationProvider? {
    return IptAuthenticationProvider()
  }
}

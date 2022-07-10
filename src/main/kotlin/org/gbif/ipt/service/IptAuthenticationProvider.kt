package org.gbif.ipt.service

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class IptAuthenticationProvider: AuthenticationProvider {

  @Autowired
  private var userDetailsService: UserDetailsService? = null
  @Autowired
  private var passwordEncoder: PasswordEncoder? = null

  override fun authenticate(authentication: Authentication?): Authentication {
    val username = if (authentication!!.principal == null) "NONE_PROVIDED" else authentication.name
    if (StringUtils.isEmpty(username)) {
      throw BadCredentialsException("invalid login details")
    }

    try {
      val user = userDetailsService!!.loadUserByUsername(username)
      val passwordMatch =
        passwordEncoder!!.matches(authentication.credentials as CharSequence, user.password)

      if (passwordMatch) {
        return createSuccessfulAuthentication(authentication, user)
      } else {
        throw BadCredentialsException("invalid login details")
      }
    } catch (exception: UsernameNotFoundException) {
      throw BadCredentialsException("invalid login details")
    }
  }

  private fun createSuccessfulAuthentication(authentication: Authentication, user: UserDetails): Authentication {
    val token = UsernamePasswordAuthenticationToken(user.username, authentication.credentials, user.authorities)
    token.details = authentication.details
    return token
  }

  override fun supports(authentication: Class<*>?): Boolean {
    return authentication!! == UsernamePasswordAuthenticationToken::class.java
  }
}


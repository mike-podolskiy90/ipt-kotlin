package org.gbif.ipt.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Primary
@Service
class IptUserDetailService : UserDetailsService {

  @Autowired
  private val userAccountManager: UserAccountManager? = null

  override fun loadUserByUsername(username: String?): UserDetails {
    val userByEmail = userAccountManager!![username]

    return if (userByEmail != null) {
      User(
        userByEmail.email,
        userByEmail.password,
        listOf(SimpleGrantedAuthority(userByEmail.role.toString()))
      )
    } else {
      throw UsernameNotFoundException("User not found with email: $username")
    }
  }
}

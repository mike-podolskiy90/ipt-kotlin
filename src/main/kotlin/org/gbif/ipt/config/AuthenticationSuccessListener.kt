package org.gbif.ipt.config

import org.gbif.ipt.service.UserAccountManager
import java.util.Date
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationSuccessListener(
  private var userAccountManager: UserAccountManager
) : ApplicationListener<ApplicationEvent> {
  override fun onApplicationEvent(event: ApplicationEvent) {
    if (event is InteractiveAuthenticationSuccessEvent) {
      handleLoginEvent()
    }
  }

  private fun handleLoginEvent() {
    val username = SecurityContextHolder.getContext().authentication.name
    val user = userAccountManager[username]
    if (user != null) {
      user.lastLogin = Date()
    }
    userAccountManager.save(user)
  }

}

package org.gbif.ipt.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AccountController {

  @GetMapping("/account")
  fun getAccountView(): String {
    return "account"
  }

  @GetMapping("/current-user")
  fun getCurrentUser(): String {
    val authentication = SecurityContextHolder.getContext().authentication
    return authentication.name
  }
}

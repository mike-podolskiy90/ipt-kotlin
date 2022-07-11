package org.gbif.ipt.controller

import org.gbif.ipt.service.UserAccountManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AccountController {

  @Autowired
  private lateinit var userAccountManager: UserAccountManager

  @GetMapping("/account")
  fun getAccountView(model: Model): String {
    val authentication = SecurityContextHolder.getContext().authentication
    val user = userAccountManager[authentication.name]
    model.addAttribute("user", user)
    model.addAttribute("email", user?.email)

    model.addAttribute("newPassword", "")
    model.addAttribute("password2", "")
    model.addAttribute("currentPassword", "")
    return "account"
  }

  @GetMapping("/current-user")
  fun getCurrentUser(): String {
    val authentication = SecurityContextHolder.getContext().authentication
    return authentication.name
  }
}

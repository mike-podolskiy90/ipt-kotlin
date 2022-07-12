package org.gbif.ipt.controller

import org.gbif.ipt.model.User
import org.gbif.ipt.service.UserAccountManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

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

  @PostMapping("/account", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
  fun updateUser(user: User): String {
    val authentication = SecurityContextHolder.getContext().authentication
    val currentUser = userAccountManager[authentication.name]
    currentUser?.lastname = user.lastname ?: user.lastname
    currentUser?.firstname = user.firstname ?: user.firstname
    userAccountManager.save()
    return "redirect:/account"
  }

  @GetMapping("/current-user")
  fun getCurrentUser(): String {
    val authentication = SecurityContextHolder.getContext().authentication
    return authentication.name
  }
}

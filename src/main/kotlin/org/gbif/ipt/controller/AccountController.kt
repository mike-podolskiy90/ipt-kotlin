package org.gbif.ipt.controller

import org.gbif.ipt.model.User
import org.gbif.ipt.service.UserAccountManager
import org.gbif.ipt.struts2.SimpleTextProvider
import org.gbif.ipt.validation.UserValidator
import java.io.IOException
import org.apache.logging.log4j.LogManager
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class AccountController(
  private var passwordEncoder: PasswordEncoder,
  textProvider: SimpleTextProvider,
  private var userAccountManager: UserAccountManager,
  private var validator: UserValidator
) : BaseController(textProvider) {

  companion object {
    private val LOG = LogManager.getLogger(AccountController::class.java)
  }

  @GetMapping("/account")
  fun getAccountView(model: Model): String {
    val authentication = SecurityContextHolder.getContext().authentication
    val user = userAccountManager[authentication.name]
    model.addAttribute("user", user)
    model.addAttribute("email", user?.email)
    return "account"
  }

  @PostMapping("/account", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
  fun updateUser(user: User, model: Model, redirectAttributes: RedirectAttributes): String {
    try {
      // password can't be updated here, skip password validation
      if (validator.validate(model, user, false)) {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = userAccountManager[authentication.name]
        currentUser?.lastname = user.lastname ?: user.lastname
        currentUser?.firstname = user.firstname ?: user.firstname
        redirectAttributes.addActionMessage(getText("admin.user.account.updated")!!)
        LOG.debug("The user account has been updated")
        userAccountManager.save()
        return "redirect:/account"
      }
    } catch (e: IOException) {
      redirectAttributes.addActionError(getText("admin.user.account.saveError")!!)
      LOG.error("The user account change could not be made: " + e.message, e)
      redirectAttributes.addActionError(e.message!!)
    }

    redirectAttributes.addFlashAttribute("fieldErrors", model.getAttribute("fieldErrors"))
    return "redirect:/account"
  }

  @PostMapping("/change-password", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
  fun changePassword(
    currentPassword: String,
    newPassword: String,
    password2: String,
    model: Model,
    redirectAttributes: RedirectAttributes
  ): String? {
    val authentication = SecurityContextHolder.getContext().authentication
    val currentUser = userAccountManager[authentication.name]
    if (currentUser != null) {
      if (validator.validatePassword(model, currentPassword, currentUser.password!!, newPassword, password2)) {
        currentUser.password = passwordEncoder.encode(newPassword)
        redirectAttributes.addActionMessage(getText("admin.user.account.passwordChanged")!!)
        LOG.debug("The password has been reset")
        return "redirect:/account"
      }
    }
    return "redirect:/account"
  }
}

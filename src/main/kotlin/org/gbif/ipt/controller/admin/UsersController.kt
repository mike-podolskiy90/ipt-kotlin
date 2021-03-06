package org.gbif.ipt.controller.admin

import org.gbif.ipt.controller.BaseController
import org.gbif.ipt.model.User
import org.gbif.ipt.service.AlreadyExistingException
import org.gbif.ipt.service.UserAccountManager
import org.gbif.ipt.struts2.SimpleTextProvider
import java.io.IOException
import org.apache.logging.log4j.LogManager
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class UsersController(
  textProvider: SimpleTextProvider,
  private var userAccountManager: UserAccountManager
) : BaseController(textProvider) {

  companion object {
    private val LOG = LogManager.getLogger(UsersController::class.java)
  }

  @GetMapping("/admin/users")
  fun getUsersView(model: Model): String {
    val users = userAccountManager.list()
    model.addAttribute("users", users)
    return "admin/users"
  }

  @GetMapping("/admin/user")
  fun getUserView(model: Model, @RequestParam("id", required = false) id : String?): String {
    val user = userAccountManager[id]
    // TODO: 16/07/2022 user not found?
    if (id != null) {
      model.addAttribute("user", user)
      model.addAttribute("id", id)
    }
    return "admin/user"
  }

  @PostMapping("/admin/user", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
  fun createOrUpdateUser(
    user: User,
    @RequestParam("id", required = false) id: String?,
    model: Model,
    redirectAttributes: RedirectAttributes
  ): String {
    val currentUser = userAccountManager[getCurrentUser()]
    try {
      if (id == null) {
        userAccountManager.create(user)
        redirectAttributes.addActionMessage(getText("admin.user.added")!!)
        return "admin/users"
      } else {
        if (userAccountManager.isLastAdmin(id) && user.role !== User.Role.Admin) {
          redirectAttributes.addActionError(getText("admin.user.changed.current")!!)
          redirectAttributes.addAttribute("id", id)
          return "redirect:/admin/user"
        }
        if (user.email == currentUser?.email) {
          currentUser?.role = user.role
        }
        userAccountManager.save(user)
        redirectAttributes.addActionMessage(getText("admin.user.changed")!!)
      }
      redirectAttributes.addAttribute("id", id)
      return "redirect:/admin/user"
    } catch (e: IOException) {
      LOG.error("The user change couldnt be saved: " + e.message, e)
      redirectAttributes.addActionError(getText("admin.user.saveError")!!)
      redirectAttributes.addActionError(e.message!!)
      redirectAttributes.addAttribute("id", id)
      return "redirect:/admin/user"
    } catch (e: AlreadyExistingException) {
      redirectAttributes.addActionError(getText("admin.user.exists", arrayOf(user.email!!))!!)
      redirectAttributes.addAttribute("id", id)
      return "redirect:/admin/user"
    }
  }
}

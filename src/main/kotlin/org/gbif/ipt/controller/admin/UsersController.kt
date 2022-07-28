package org.gbif.ipt.controller.admin

import org.gbif.ipt.controller.BaseController
import org.gbif.ipt.model.User
import org.gbif.ipt.service.AlreadyExistingException
import org.gbif.ipt.service.DeletionNotAllowedException
import org.gbif.ipt.service.PasswordGenerator
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
  private var userAccountManager: UserAccountManager,
  private var passwordGenerator: PasswordGenerator
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
  fun post(
    save: String?,
    delete: String?,
    resetPassword: String?,
    @RequestParam("id", required = false) id: String?,
    user: User,
    redirectAttributes: RedirectAttributes
  ): String {
    if (save != null && id == null) {
      return create(user, redirectAttributes)
    } else if (save != null && id != null) {
      return update(user, id, redirectAttributes)
    } else if (delete != null && id != null) {
      return delete(id, redirectAttributes)
    } else if (resetPassword != null && id != null) {
      return resetPassword(user, id, redirectAttributes)
    }

    return "redirect:/admin/user"
  }

  fun create(
    user: User,
    redirectAttributes: RedirectAttributes
  ): String {
    return try {
      userAccountManager.create(user)
      redirectAttributes.addActionMessage(getText("admin.user.added"))
      "admin/users"
    } catch (e: IOException) {
      LOG.error("The user change couldnt be saved: " + e.message, e)
      redirectAttributes.addActionError(getText("admin.user.saveError"))
      e.message?.let { redirectAttributes.addActionError(it) }
      redirectAttributes.addAttribute("id", user.email)
      "redirect:/admin/user"
    } catch (e: AlreadyExistingException) {
      user.email?.let { redirectAttributes.addActionError(getText("admin.user.exists", arrayOf(it))) }
      redirectAttributes.addAttribute("id", user.email)
      "redirect:/admin/user"
    }
  }

  fun update(
    user: User,
    id: String,
    redirectAttributes: RedirectAttributes
  ): String {
    val currentUser = userAccountManager[getCurrentUser()]
    try {
      if (userAccountManager.isLastAdmin(id) && user.role !== User.Role.Admin) {
        redirectAttributes.addActionError(getText("admin.user.changed.current"))
        redirectAttributes.addAttribute("id", id)
        return "redirect:/admin/user"
      }
      if (user.email == currentUser?.email) {
        currentUser?.role = user.role
      }
      userAccountManager.save(user)
      redirectAttributes.addActionMessage(getText("admin.user.changed"))
      redirectAttributes.addAttribute("id", id)
      return "redirect:/admin/user"
    } catch (e: IOException) {
      LOG.error("The user change couldnt be saved: " + e.message, e)
      redirectAttributes.addActionError(getText("admin.user.saveError"))
      e.message?.let { redirectAttributes.addActionError(it) }
      redirectAttributes.addAttribute("id", id)
      return "redirect:/admin/user"
    }
  }

  fun delete(
    id: String,
    redirectAttributes: RedirectAttributes
  ): String {
    val currentUser = userAccountManager[getCurrentUser()]
    if (currentUser?.email.equals(id, ignoreCase = true)) {
      // can't remove logged in user
      redirectAttributes.addActionError(getText("admin.user.deleted.current"))
      redirectAttributes.addAttribute("id", id)
    } else {
      try {
        val removedUser: User = userAccountManager.delete(id) ?: return "redirect:/not_found"
        userAccountManager.save()
        redirectAttributes.addActionMessage(getText("admin.user.deleted"))
        return "redirect:/admin/users"
      } catch (e: DeletionNotAllowedException) {
        if (DeletionNotAllowedException.Reason.LAST_ADMIN === e.reason) {
          redirectAttributes.addActionError(getText("admin.user.deleted.lastadmin"))
        } else if (DeletionNotAllowedException.Reason.LAST_RESOURCE_MANAGER === e.reason) {
          e.message?.let { redirectAttributes.addActionError(getText("admin.user.deleted.lastmanager", arrayOf(it))) }
        } else if (DeletionNotAllowedException.Reason.IS_RESOURCE_CREATOR === e.reason) {
          e.message?.let { redirectAttributes.addActionError(getText("admin.user.deleted.error.creator", arrayOf(it))) }
        } else {
          redirectAttributes.addActionError(getText("admin.user.deleted.error"))
        }
      } catch (e: IOException) {
        e.message?.let { redirectAttributes.addActionError(getText("admin.user.cantSave", arrayOf(it))) }
      }
    }
    return "redirect:/admin/user"
  }

  fun resetPassword(
    user: User,
    id: String,
    redirectAttributes: RedirectAttributes
  ): String {
    val newPassword = passwordGenerator.generateNewPassword()
    val hash: String = passwordGenerator.hashPassword(newPassword)
    user.password = hash
    userAccountManager.save(user)
    user.email?.let { redirectAttributes.addActionMessage(getText("admin.user.passwordChanged", arrayOf(it, newPassword))) }
    redirectAttributes.addAttribute("id", id)
    return "redirect:/admin/user"
  }
}

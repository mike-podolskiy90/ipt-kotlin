package org.gbif.ipt.controller.admin

import org.gbif.ipt.service.UserAccountManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class UsersController {

  @Autowired
  private lateinit var userAccountManager: UserAccountManager

  @GetMapping("/admin/users")
  fun getUsersView(model: Model): String {
    val users = userAccountManager.list()
    model.addAttribute("users", users)
    return "admin/users"
  }

  @GetMapping("/admin/user")
  fun getUserView(model: Model, @RequestParam("id") id : String): String {
    val user = userAccountManager[id]
    model.addAttribute("user", user)
    model.addAttribute("id", user?.email)
    model.addAttribute("newUser", "no")
    return "admin/user"
  }
}

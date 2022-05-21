package org.gbif.ipt.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AdminHomeController {

  @GetMapping("/admin")
  fun getHomeView(): String {
    return "admin/home"
  }
}

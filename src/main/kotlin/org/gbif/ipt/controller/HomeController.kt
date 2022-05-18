package org.gbif.ipt.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

  @GetMapping("/")
  fun getHomeView(model: Model): String {
    return "portal/home"
  }

}

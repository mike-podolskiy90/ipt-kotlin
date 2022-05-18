package org.gbif.ipt.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AboutController {

  @GetMapping("/about")
  fun getAboutView(model: Model): String {
    return "about"
  }
}

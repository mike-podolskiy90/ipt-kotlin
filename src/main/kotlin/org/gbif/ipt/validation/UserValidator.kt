package org.gbif.ipt.validation

import org.gbif.ipt.model.User
import org.gbif.ipt.struts2.SimpleTextProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.ui.Model

@Component
class UserValidator : BaseValidator() {

  @Autowired
  private lateinit var textProvider: SimpleTextProvider

  fun validate(model: Model, user: User?, validatePassword: Boolean = true): Boolean {
    var valid = true
    if (user != null) {
      if (exists(user.email)) {
        if (!isValidEmail(user.email)) {
          valid = false
          model.addFieldError("user.email", textProvider.getText("validation.email.invalid"))
        }
      } else {
        model.addFieldError("user.email", textProvider.getText("validation.email.required"))
        valid = false
      }
      if (!exists(user.firstname, 1)) {
        valid = false
        model.addFieldError("firstname", textProvider.getText("validation.firstname.required"))
      }
      if (!exists(user.lastname, 1)) {
        valid = false
        model.addFieldError("user.lastname", textProvider.getText("validation.lastname.required"))
      }
      if (validatePassword && !exists(user.password, 4)) {
        valid = false
        model.addFieldError("newPassword", textProvider.getText("validation.password.required"))
        model.addFieldError("user.password", textProvider.getText("validation.password.required"))
      }
    }
    return valid
  }

  fun validatePassword(model: Model, password: String?): Boolean {
    var valid = true
    if (!exists(password, 4)) {
      valid = false
      model.addFieldError("newPassword", textProvider.getText("validation.password.required"))
      model.addFieldError("user.password", textProvider.getText("validation.password.required"))
    }
    return valid
  }
}

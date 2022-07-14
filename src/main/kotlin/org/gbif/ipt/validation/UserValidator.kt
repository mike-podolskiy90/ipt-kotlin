package org.gbif.ipt.validation

import org.gbif.ipt.model.User
import org.gbif.ipt.struts2.SimpleTextProvider
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.ui.Model

@Component
class UserValidator : BaseValidator() {

  @Autowired
  private lateinit var passwordEncoder: PasswordEncoder;

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

  fun validatePassword(model: Model, currentPasswordInput: String, currentPasswordActual: String, newPassword: String, password2: String): Boolean {
    var valid = true
    val trimmedCurrentPassword = StringUtils.trimToNull(currentPasswordInput)
    val trimmedNewPassword = StringUtils.trimToNull(newPassword)
    val trimmedNewPasswordConfirmation = StringUtils.trimToNull(password2)

    // all passwords must not be nulls
    if (trimmedCurrentPassword == null) {
      model.addFieldError("currentPassword", textProvider.getText("validation.password.reentered"))
      LOG.error("The current password entered is empty")
      valid = false
    } else if (trimmedNewPassword == null) {
      model.addFieldError("newPassword", textProvider.getText("validation.password.reentered"))
      LOG.error("The new password entered is empty")
      valid = false
    } else if (trimmedNewPasswordConfirmation == null) {
      model.addFieldError("password2", textProvider.getText("validation.password.reentered"))
      LOG.error("The new password confirmation entered is empty")
      valid = false
    } else if (!passwordEncoder.matches(trimmedCurrentPassword, currentPasswordActual)) {
      model.addFieldError("currentPassword", textProvider.getText("validation.password.wrong"))
      LOG.error("The password does not match")
      valid = false
    } else if (trimmedNewPassword != trimmedNewPasswordConfirmation) {
      model.addFieldError("password2", textProvider.getText("validation.password2.wrong"))
      LOG.error("The passwords entered do not match")
      valid = false
    } else if (validatePassword(model, newPassword).not()) {
      valid = true
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

  companion object {
    private val LOG = LogManager.getLogger(UserValidator::class.java)
  }
}

package org.gbif.ipt.validation

import java.util.Date
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.LogManager
import org.springframework.ui.Model

abstract class BaseValidator {

  fun Model.addFieldError(field: String, error: String) {
    val fieldErrorsAttributeRaw = this.getAttribute("fieldErrors")

    if (fieldErrorsAttributeRaw != null && fieldErrorsAttributeRaw is MutableMap<*, *>) {
      val fieldErrorsAttribute: MutableMap<String, MutableList<String>> = fieldErrorsAttributeRaw as MutableMap<String, MutableList<String>>
      val fieldErrors = fieldErrorsAttribute[field]

      if (fieldErrors != null) {
        fieldErrors.add(error)
      } else {
        fieldErrorsAttribute[field] = mutableListOf(error)
      }
    } else {
      val fieldsErrors: MutableMap<String, MutableList<String>> = HashMap()
      fieldsErrors[field] = mutableListOf(error)
      this.addAttribute("fieldErrors", fieldsErrors)
    }
  }

  protected fun exists(i: Int?): Boolean {
    return i != null
  }

  protected fun exists(d: Date?): Boolean {
    return d != null
  }

  protected fun exists(x: String?, minLength: Int = 2): Boolean {
    return x != null && x.trim { it <= ' ' }.length >= minLength
  }

  /**
   * Checks if string has a length in a certain range.
   *
   * @param x         incoming string
   * @param minLength min length inclusive
   * @param maxLength max length inclusive
   *
   * @return true if string
   */
  protected fun existsInRange(x: String?, minLength: Int, maxLength: Int): Boolean {
    val trimmed = StringUtils.trimToNull(x)
    return trimmed != null && trimmed.length >= minLength && trimmed.length <= maxLength
  }

  protected fun isValidEmail(email: String?): Boolean {
    if (email != null) {
      try {
        val internetAddress = InternetAddress(email)
        internetAddress.validate()
        return true
      } catch (e: AddressException) {
        LOG.debug("Email address was invalid: " + StringUtils.trimToEmpty(email))
      }
    }
    return false
  }

  companion object {
    private val LOG = LogManager.getLogger(BaseValidator::class.java)
  }
}

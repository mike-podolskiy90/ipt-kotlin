package org.gbif.ipt.controller

import org.gbif.ipt.struts2.SimpleTextProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.mvc.support.RedirectAttributes

abstract class BaseController(private var textProvider: SimpleTextProvider) {

  fun RedirectAttributes.addActionMessage(message: String) {
    val actionMessagesRaw = this.getAttribute("actionMessages")

    if (actionMessagesRaw != null && actionMessagesRaw is List<*>) {
      val actionMessages: MutableList<String> = actionMessagesRaw as MutableList<String>
      actionMessages.add(message)
    } else {
      val actionMessages: MutableList<String> = ArrayList()
      actionMessages.add(message)
      this.addFlashAttribute("actionMessages", actionMessages)
    }
  }

  fun RedirectAttributes.addActionError(error: String) {
    val actionErrorsRaw = this.getAttribute("actionErrors")

    if (actionErrorsRaw != null && actionErrorsRaw is List<*>) {
      val actionErrors: MutableList<String> = actionErrorsRaw as MutableList<String>
      actionErrors.add(error)
    } else {
      val actionErrors: MutableList<String> = ArrayList()
      actionErrors.add(error)
      this.addFlashAttribute("actionErrors", actionErrors)
    }
  }

  fun RedirectAttributes.addWarning(warning: String) {
    val actionWarningsRaw = this.getAttribute("warnings")

    if (actionWarningsRaw != null && actionWarningsRaw is List<*>) {
      val actionWarnings: MutableList<String> = actionWarningsRaw as MutableList<String>
      actionWarnings.add(warning)
    } else {
      val actionWarnings: MutableList<String> = ArrayList()
      actionWarnings.add(warning)
      this.addFlashAttribute("warnings", actionWarnings)
    }
  }

  /**
   * Return the currently logged in (session) user.
   *
   * @return the currently logged in (session) user or null if not logged in
   */
  open fun getCurrentUser(): String? {
    var u: String? = null
    try {
      u = SecurityContextHolder.getContext().authentication.name
    } catch (e: Exception) {
//      LOG.debug("A problem occurred retrieving current user. This can happen if the session is not yet opened")
    }
    return u
  }

  open fun getText(key: String): String? {
    return textProvider.getText(key, null, emptyArray())
  }

  open fun getText(key: String, params: Array<String>): String? {
    return textProvider.getText(key, null, params)
  }
}

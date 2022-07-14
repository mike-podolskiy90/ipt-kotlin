package org.gbif.ipt.controller

import org.gbif.ipt.struts2.SimpleTextProvider
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

  open fun getText(key: String): String? {
    return textProvider.getText(key, null, emptyArray())
  }
}

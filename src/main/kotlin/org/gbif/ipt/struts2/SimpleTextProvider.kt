package org.gbif.ipt.struts2

import java.text.MessageFormat
import java.util.Locale
import java.util.MissingResourceException
import java.util.ResourceBundle
import org.apache.logging.log4j.LogManager
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

/**
 * A basic text provider for internationalised messages that can replace the native struts2 one. It uses only a single
 * bundle name to speed up the lookup which increases performance of page rendering with many text blocks by sometimes
 * more than 100%.
 */
@Component
class SimpleTextProvider {
  private val baseBundleNames: MutableSet<String> = HashSet()

  /**
   * Finds the given resource bundle by its name and locale.
   * <br/>
   * Will use `Thread.currentThread().getContextClassLoader()` as the classloader.
   *
   * @param aBundleName the name of the bundle (usually it's FQN classname).
   * @param locale      the locale.
   *
   * @return the bundle, defaulting to the English bundle if no match for locale found or if Exception occurred
   */
  fun findResourceBundle(aBundleName: String, locale: Locale): ResourceBundle {
    val currentLocale = Locale.getDefault()
    return try {
      // override default Locale in case incoming locale isn't matched - see ResourceBundle.getFallbackLocale()
      Locale.setDefault(Locale.ENGLISH)
      ResourceBundle.getBundle(aBundleName, locale, Thread.currentThread().contextClassLoader)
    } catch (e: Exception) {
      ResourceBundle.getBundle(aBundleName, Locale.ENGLISH, Thread.currentThread().contextClassLoader)
    } finally {
      Locale.setDefault(currentLocale)
    }
  }

  fun findText(bundle: ResourceBundle, aTextName: String, defaultMessage: String?, args: Array<Any?>?): String {
    try {
      val message = bundle.getString(aTextName)
      val text: String
      text = try {
        MessageFormat.format(message, args)
      } catch (e: IllegalArgumentException) {
        // message and arguments don't match?
        LOG.debug(e)
        message
      }
      return text
    } catch (e: MissingResourceException) {
      // return default message
    }
    return defaultMessage ?: aTextName
  }

  /**
   * Gets a message based on a key using the supplied args, as defined in [MessageFormat], or, if the
   * message is not found, a supplied default value is returned. Instead of using the value stack in the ActionContext
   * this version of the getText() method uses the provided value stack.
   *
   * @param key the resource bundle key that is to be searched for
   * @param defaultValue the default value which will be returned if no message is found. If null the key name will be
   * used instead
   * @param args a list args to be used in a [MessageFormat] message
   * @return the message as found in the resource bundle, or defaultValue if none is found
   */
  fun getText(key: String, defaultValue: String?, args: Array<Any?>?): String? {
    // Locale, defaulting to English if it cannot be determined
    val locale = LocaleContextHolder.getLocale()
    var text: String? = null
    for (resName in baseBundleNames) {
      val bundle = findResourceBundle(resName, locale)
      text = findText(bundle, key, defaultValue, args)
      break
    }
    return text
  }

  fun getText(key: String): String {
    // Locale, defaulting to English if it cannot be determined
    val locale = LocaleContextHolder.getLocale()
    var text: String? = null
    for (resName in baseBundleNames) {
      val bundle = findResourceBundle(resName, locale)
      text = findText(bundle, key, null, null)
      break
    }
    return text ?: key
  }

  fun getTexts(locale: Locale): ResourceBundle {
    return findResourceBundle(DEFAULT_BUNDLE, locale)
  }

  fun getTexts(bundleName: String, locale: Locale): ResourceBundle {
    return findResourceBundle(bundleName, locale)
  }

  companion object {
    private val LOG = LogManager.getLogger(
      SimpleTextProvider::class.java
    )
    private const val DEFAULT_BUNDLE = "i18n.ApplicationResources"
  }

  init {
    baseBundleNames.add(DEFAULT_BUNDLE)
  }
}


package org.gbif.ipt.model

import java.io.Serializable
import java.util.Date
import java.util.Locale
import java.util.Objects
import org.apache.commons.lang3.StringUtils

class User : Serializable, Cloneable {
  enum class Role {
    User, Manager, Publisher, Admin
  }

  var email: String? = null // unique
    set(value) {
      var inner = value
      if (inner != null) {
        inner = inner.lowercase(Locale.getDefault()).trim { it <= ' ' }
      }
      field = inner
    }
  var password: String? = null
  var firstname: String? = null
  var lastname: String? = null
  var role = Role.User
  var lastLogin: Date? = null
  val name: String
    get() = StringUtils.trimToNull(StringUtils.trimToEmpty(firstname) + " " + StringUtils.trimToEmpty(lastname))

  @Throws(CloneNotSupportedException::class)
  public override fun clone(): Any {
    val clone = super.clone() as User
    if (lastLogin != null) {
      clone.lastLogin = lastLogin!!.clone() as Date
    }
    return clone
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other !is User) {
      return false
    }
    return email == other.email
  }

  /**
   * Called in login page, redirected to on failed logins.
   */
  val nameWithEmail: String
    get() = StringUtils.trimToNull("$name <$email>")

  /**
   * @return true if user has admin rights
   */
  fun hasAdminRights(): Boolean {
    return Role.Admin == role
  }

  override fun hashCode(): Int {
    return Objects.hashCode(email)
  }

  /**
   * @return true if user has manager rights, ie is a manager or admin
   */
  fun hasManagerRights(): Boolean {
    return Role.User != role
  }

  /**
   * @return true if a user has rights to register resources with gbif
   */
  fun hasRegistrationRights(): Boolean {
    return Role.Publisher == role || Role.Admin == role
  }

  fun setLastLoginToNow() {
    lastLogin = Date()
  }

  override fun toString(): String {
    return "User $email"
  }

  companion object {
    private const val serialVersionUID = 3832626162173359411L
  }
}

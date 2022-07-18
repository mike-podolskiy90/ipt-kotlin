package org.gbif.ipt.service

import org.gbif.ipt.model.User

import java.io.IOException

interface UserAccountManager {

  fun authenticate(email: String?, password: String?): User?

  @Throws(AlreadyExistingException::class, IOException::class)
  fun create(user: User?)

  @Throws(DeletionNotAllowedException::class, IOException::class)
  fun delete(email: String?): User?

  operator fun get(email: String?): User?

  fun list(): List<User?>?

  fun list(role: User.Role?): List<User?>

  @Throws(InvalidConfigException::class)
  fun load()

  @Throws(IOException::class)
  fun save()

  @Throws(IOException::class)
  fun save(user: User?)

  fun getDefaultAdminEmail(): String?

  fun getSetupUser(): User?

  fun setSetupUser(setupLogin: User?)

  fun isLastAdmin(email: String?): Boolean
}


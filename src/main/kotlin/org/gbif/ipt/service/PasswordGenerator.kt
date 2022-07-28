package org.gbif.ipt.service

import org.apache.commons.text.RandomStringGenerator
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordGenerator(private var passwordEncoder: PasswordEncoder) {

  private val PASSWORD_LENGTH = 8
  private val PASSWORD_ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
  private val PASSWORD_GENERATOR: RandomStringGenerator = RandomStringGenerator.Builder()
    .selectFrom(*PASSWORD_ALLOWED_CHARS.toCharArray())
    .build()

  fun generateNewPassword(): String {
    return PASSWORD_GENERATOR.generate(PASSWORD_LENGTH)
  }

  fun hashPassword(password: String): String {
    return passwordEncoder.encode(password)
  }

  fun generateNewHashedPassword(): String {
    val newPassword = generateNewPassword()
    return hashPassword(newPassword)
  }
}

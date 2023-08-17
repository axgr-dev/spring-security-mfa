package dev.axgr.mfa

import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import java.util.UUID

interface OneTimePasswordDevice {

  val id: UUID

  val name: String

  fun secret(): String

  fun confirm(code: String): Boolean

  fun confirmed(): Boolean

  fun accepts(code: String): Boolean
}

class GoogleAuthenticatorDevice(
  override val id: UUID = UUID.randomUUID(),
  override val name: String,
  private val secret: String = GoogleAuthenticator.createRandomSecret(),
  private var confirmed: Boolean = false) : OneTimePasswordDevice {

  private val authenticator = GoogleAuthenticator(secret)

  override fun confirm(code: String): Boolean {
    if (accepts(code)) {
      confirmed = true
    }

    return confirmed()
  }

  override fun confirmed(): Boolean = confirmed

  override fun accepts(code: String): Boolean {
    return authenticator.generate() == code
  }

  override fun secret(): String = secret

}

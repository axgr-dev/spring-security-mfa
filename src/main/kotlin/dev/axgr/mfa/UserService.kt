package dev.axgr.mfa

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserService(private val encoder: PasswordEncoder) : UserDetailsService {

  private val users = mutableMapOf<String, TokenUser>()

  override fun loadUserByUsername(username: String): UserDetails = byUsername(username)

  fun byUsername(username: String): TokenUser =
    users[username] ?: throw IllegalArgumentException("User $username not found")

  fun register(username: String, password: String) {
    val user = User
      .withUsername(username)
      .password(encoder.encode(password))
      .authorities("root")
      .accountExpired(false)
      .disabled(false)
      .build()

    users[username] = TokenUser(user)
  }

  fun attachDevice(username: String, name: String): OneTimePasswordDevice {
    val user = byUsername(username)
    val device = GoogleAuthenticatorDevice(name = name)
    user.device = device

    return device
  }

  fun confirmDevice(username: String, code: String): OneTimePasswordDevice {
    val user = byUsername(username)
    val device = user.device ?: throw IllegalStateException("No device attached")
    device.confirm(code)

    return device
  }

  fun attachConfirmedDevice(username: String, name: String, secret: String): OneTimePasswordDevice {
    val user = byUsername(username)
    val device = GoogleAuthenticatorDevice(name = name, secret = secret, confirmed = true)
    user.device = device

    return device
  }

}

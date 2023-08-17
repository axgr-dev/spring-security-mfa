package dev.axgr.mfa

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class OneTimePasswordAuthProvider(val users: UserDetailsService) : AuthenticationProvider {

  override fun authenticate(authentication: Authentication): Authentication {
    val auth = authentication as OneTimePassword
    val user = users.loadUserByUsername(auth.name) as TokenUser
    val device = user.device ?: throw IllegalStateException("No device attached")

    if (user.requiresMfa()) {
      if (!device.accepts(auth.credentials.toString())) {
        throw BadCredentialsException("Incorrect OTP!")
      }
    }

    return auth.initial
  }


  override fun supports(authentication: Class<*>?): Boolean {
    return authentication == OneTimePassword::class.java
  }
}

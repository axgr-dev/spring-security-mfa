package dev.axgr.mfa

import org.springframework.security.core.userdetails.UserDetails

class TokenUser(private val details: UserDetails) : UserDetails by details {

  var device: OneTimePasswordDevice? = null

  override fun toString(): String = details.username

  fun requiresMfa(): Boolean {
    return device?.confirmed() == true
  }

}

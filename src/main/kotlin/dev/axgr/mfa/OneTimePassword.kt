package dev.axgr.mfa

import org.springframework.security.core.Authentication

class OneTimePassword(val initial: Authentication, val code: String) : Authentication by initial {

  override fun getCredentials(): Any {
    return this.code
  }

}

package dev.axgr.mfa

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationConverter

class OneTimePasswordConverter(private val initial: Authentication) : AuthenticationConverter {

  companion object {
    private const val OTP_HEADER_NAME = "otp"
  }

  override fun convert(request: HttpServletRequest): OneTimePassword? {
    val otp = request.getHeader(OTP_HEADER_NAME)

    if (otp == null || otp.isBlank()) {
      return null
    }

    return OneTimePassword(initial, otp)
  }

}

package dev.axgr.mfa

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter

class OneTimePasswordAuthFilter(private val manager: AuthenticationManager, private val users: UserDetailsService) : OncePerRequestFilter() {

  companion object {
    private val log = LoggerFactory.getLogger(OneTimePasswordAuthFilter::class.java)
  }

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
    val initial = SecurityContextHolder.getContext().authentication

    if (initial.isAuthenticated) {
      val user = users.loadUserByUsername(initial.name) as TokenUser
      if (user.requiresMfa()) {
        val otp = OneTimePasswordConverter(initial).convert(request)

        if (otp == null || otp.code.isBlank()) {
          log.info("No OTP code provided.")
          response.status = HttpServletResponse.SC_UNAUTHORIZED
          response.setHeader("WWW-Authenticate", "OTP")
          return
        }

        try {
          val authed = manager.authenticate(otp)
          SecurityContextHolder.getContext().authentication = authed
        } catch (cause: AuthenticationException) {
          response.status = HttpServletResponse.SC_UNAUTHORIZED
          return
        }
      } else {
        log.info("User ${initial.name} doesn't require a token.")
      }
    }

    chain.doFilter(request, response)
  }
}

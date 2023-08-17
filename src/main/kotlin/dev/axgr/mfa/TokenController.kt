package dev.axgr.mfa

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/tokens")
class TokenController(private val users: UserService) {

  @PostMapping
  fun register(@RequestBody body: TokenRegistrationRequest, principal: Principal): TokenRegistrationResponse {
    val attachment = users.attachDevice(principal.name, body.name)
    return TokenRegistrationResponse(attachment.id.toString(), attachment.name, attachment.secret())
  }

  @PostMapping("/confirm")
  fun confirm(@RequestBody body: TokenConfirmationRequest, principal: Principal): TokenConfirmationResponse {
    val confirmation = users.confirmDevice(principal.name, body.code)
    return TokenConfirmationResponse(confirmation.id.toString(), confirmation.name, confirmation.confirmed())
  }

}

data class TokenRegistrationRequest(val name: String)
data class TokenRegistrationResponse(val id: String, val name: String, val secret: String)

data class TokenConfirmationRequest(val code: String)
data class TokenConfirmationResponse(val id: String, val name: String, val confirmed: Boolean)

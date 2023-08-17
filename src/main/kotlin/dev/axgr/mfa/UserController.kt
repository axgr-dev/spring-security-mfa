package dev.axgr.mfa

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/users")
class UserController {

  @GetMapping("/me")
  fun me(principal: Principal): Principal {
    return principal
  }

}

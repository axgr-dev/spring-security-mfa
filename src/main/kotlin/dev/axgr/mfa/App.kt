package dev.axgr.mfa

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class App {

  @Bean
  fun run(users: UserService) = CommandLineRunner {
    val username = "hello@axgr.dev"
    users.register(username, "swordfish")
    users.attachConfirmedDevice(username, "Google Authenticator", "AKMW3WXXWBHAMAHC")
  }

}

fun main(args: Array<String>) {
  runApplication<App>(*args)
}

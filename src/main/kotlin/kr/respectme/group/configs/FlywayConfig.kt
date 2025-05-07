package kr.respectme.group.configs

import jakarta.activation.DataSource
import org.flywaydb.core.Flyway
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment

@Configuration
@Profile("local")
class LocalFlywayConfig {

    @Bean
    fun runFlywayOnStartup(flyway: Flyway, env: Environment): ApplicationRunner {
        return ApplicationRunner {
            flyway.clean()
            flyway.migrate()
        }
    }
}



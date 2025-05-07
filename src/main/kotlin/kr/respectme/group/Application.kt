package kr.respectme.group

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableAspectJAutoProxy
class Application {}
fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
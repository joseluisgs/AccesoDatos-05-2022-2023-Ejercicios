package mendoza.acosta.empresarestspringboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableCaching
@EnableR2dbcRepositories
class EmpresaRestSpringBootApplication

fun main(args: Array<String>) {
    runApplication<EmpresaRestSpringBootApplication>(*args)
}

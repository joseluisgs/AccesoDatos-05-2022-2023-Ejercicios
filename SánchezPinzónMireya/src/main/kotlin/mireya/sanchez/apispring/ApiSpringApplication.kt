package mireya.sanchez.apispring

import kotlinx.coroutines.runBlocking
import mireya.sanchez.apispring.controllers.UsuariosController
import mireya.sanchez.apispring.db.getUsuariosInit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiSpringApplication
@Autowired constructor(
    private val controller: UsuariosController
) : CommandLineRunner {
    override fun run(vararg args: String?): Unit = runBlocking {
        getUsuariosInit().forEach {
            controller.initialiceUsers(it)
        }
    }

}

fun main(args: Array<String>) {
    runApplication<ApiSpringApplication>(*args)
}

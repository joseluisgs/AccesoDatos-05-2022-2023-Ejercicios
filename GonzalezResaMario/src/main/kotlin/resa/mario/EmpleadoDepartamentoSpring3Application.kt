package resa.mario

/**
 * @Author Mario Resa
 */

import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import resa.mario.controller.UsuarioController
import resa.mario.db.getUsersInit

/**
 * Funcion principal del programa.
 *
 * @property controller
 */
@SpringBootApplication
@EnableCaching
@EnableR2dbcRepositories
class EmpleadoDepartamentoSpring3Application
@Autowired constructor(
    private val controller: UsuarioController
) : CommandLineRunner {

    override fun run(vararg args: String?): Unit = runBlocking {
        // Datos iniciales
        getUsersInit().forEach {
            controller.createByAdminInitializer(it)
        }
    }

}

fun main(args: Array<String>) {
    runApplication<EmpleadoDepartamentoSpring3Application>(*args)
}

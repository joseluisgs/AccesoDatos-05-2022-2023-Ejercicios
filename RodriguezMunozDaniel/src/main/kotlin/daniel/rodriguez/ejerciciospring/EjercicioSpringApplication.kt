package daniel.rodriguez.ejerciciospring

import daniel.rodriguez.ejerciciospring.controllers.DepartamentoController
import daniel.rodriguez.ejerciciospring.controllers.EmpleadoController
import daniel.rodriguez.ejerciciospring.controllers.UserController
import daniel.rodriguez.ejerciciospring.db.departamentos
import daniel.rodriguez.ejerciciospring.db.empleados
import daniel.rodriguez.ejerciciospring.db.users
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcRepositories
@EnableCaching
class EjercicioSpringApplication : CommandLineRunner {
    @Autowired
    lateinit var uController: UserController
    @Autowired
    lateinit var eController: EmpleadoController
    @Autowired
    lateinit var dController: DepartamentoController
    override fun run(vararg args: String?): Unit = runBlocking {
        users().forEach { uController.createInit(it) }
        departamentos().forEach { dController.createInit(it) }
        empleados().forEach { eController.createInit(it) }
    }

}

fun main(args: Array<String>) {
    runApplication<EjercicioSpringApplication>(*args)
}

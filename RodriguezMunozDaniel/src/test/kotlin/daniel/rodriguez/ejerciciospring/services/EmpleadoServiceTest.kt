package daniel.rodriguez.ejerciciospring.services

import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTOcreacion
import daniel.rodriguez.ejerciciospring.exception.DepartamentoExceptionNotFound
import daniel.rodriguez.ejerciciospring.exception.EmpleadoExceptionNotFound
import daniel.rodriguez.ejerciciospring.models.Departamento
import daniel.rodriguez.ejerciciospring.models.Empleado
import daniel.rodriguez.ejerciciospring.repositories.departamento.DepartamentoRepositoryCached
import daniel.rodriguez.ejerciciospring.repositories.empleado.EmpleadoRepositoryCached
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@ExtendWith(MockKExtension::class)
@SpringBootTest
class EmpleadoServiceTest {
    private val dto = EmpleadoDTOcreacion(
        id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea1234"),
        nombre = "Nombre Generico TEST", email = "empleadotest@gmail.com", avatar = "",
        departamentoId = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003")
    )
    private val entity = Empleado(1L, dto.id, dto.nombre, dto.email, dto.avatar, dto.departamentoId)
    private val departamento = Departamento(2L, dto.departamentoId, "departamento test", 69420.69)

    @MockK lateinit var repo: EmpleadoRepositoryCached
    @MockK lateinit var dRepo: DepartamentoRepositoryCached
    @InjectMockKs lateinit var service: EmpleadoService

    init { MockKAnnotations.init(this) }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAll() = runTest {
        coEvery { repo.findAll() } returns flowOf(entity)

        val result = service.findAllEmpleados()

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertEquals(dto.email, result[0].email) },
            { Assertions.assertEquals(dto.nombre, result[0].nombre) },
        )
        coVerify(exactly = 1) { repo.findAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAllNF() = runTest {
        coEvery { repo.findAll() } returns flowOf()

        val result = service.findAllEmpleados().toList()

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertTrue(result.isEmpty()) }
        )
        coVerify { repo.findAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findByUUID() = runTest {
        coEvery { repo.findByUUID(any()) } returns entity

        val result = service.findEmpleadoByUuid(dto.id)

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertEquals(dto.email, result.email) },
            { Assertions.assertEquals(dto.nombre, result.nombre) },
        )
        coVerify(exactly = 1) { repo.findByUUID(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findByUUIDNF() = runTest {
        coEvery { repo.findByUUID(any()) } returns null

        val result = assertThrows<EmpleadoExceptionNotFound> {
            service.findEmpleadoByUuid(dto.id)
        }

        Assertions.assertAll(
            { Assertions.assertEquals(
                "Couldn't find empleado with uuid ${dto.id}.", result.message
            ) }
        )
        coVerify { repo.findByUUID(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun save() = runTest {
        coEvery { repo.save(any()) } returns entity
        coEvery { dRepo.findByUUID(any()) } returns departamento

        val result = service.saveEmpleado(dto)

        Assertions.assertAll(
            { Assertions.assertEquals(dto.email, result.email) },
            { Assertions.assertEquals(dto.nombre, result.nombre) },
        )

        coVerify { repo.save(any()) }
        coVerify { dRepo.findByUUID(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveNF() = runTest {
        coEvery { dRepo.findByUUID(any()) } returns null

        val result = assertThrows<DepartamentoExceptionNotFound> {
            service.saveEmpleado(dto)
        }

        Assertions.assertAll(
            { Assertions.assertEquals(
                "Couldn't find departamento with uuid ${entity.departamentoId}.", result.message
            ) }
        )

        coVerify { dRepo.findByUUID(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun delete() = runTest {
        coEvery { repo.findByUUID(any()) } returns entity
        coEvery { repo.delete(any()) } returns entity

        val result = service.deleteEmpleado(dto.id)

        Assertions.assertAll(
            { Assertions.assertEquals(dto.email, result?.email) },
            { Assertions.assertEquals(dto.nombre, result?.nombre) },
        )

        coVerify { repo.delete(any()) }
        coVerify { repo.findByUUID(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteNF() = runTest {
        coEvery { repo.findByUUID(any()) } returns null
        coEvery { repo.delete(any()) } returns entity

        val result = assertThrows<EmpleadoExceptionNotFound> {
            service.deleteEmpleado(dto.id)
        }

        Assertions.assertAll(
            { Assertions.assertEquals(
                "Empleado with uuid ${dto.id} not found.", result.message
            ) }
        )

        coVerify { repo.findByUUID(any()) }
    }
}
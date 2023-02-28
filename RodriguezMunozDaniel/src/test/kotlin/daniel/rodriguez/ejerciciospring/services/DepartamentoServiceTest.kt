package daniel.rodriguez.ejerciciospring.services

import daniel.rodriguez.ejerciciospring.dto.DepartamentoDTOcreacion
import daniel.rodriguez.ejerciciospring.exception.DepartamentoExceptionBadRequest
import daniel.rodriguez.ejerciciospring.exception.DepartamentoExceptionNotFound
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
class DepartamentoServiceTest {
    private val dto = DepartamentoDTOcreacion(
        id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea2345"),
        nombre = "Departamento test", presupuesto = 69_420.69
    )
    private val entity = Departamento(1L, dto.id, dto.nombre, dto.presupuesto)
    private val empleado = Empleado(
        2L, UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea3456"),
        "Nombre Generico TEST", "empleadotest@gmail.com", "",
        dto.id
    )

    @MockK lateinit var repo: DepartamentoRepositoryCached
    @MockK lateinit var eRepo: EmpleadoRepositoryCached
    @InjectMockKs lateinit var service: DepartamentoService

    init { MockKAnnotations.init(this) }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAll() = runTest {
        coEvery { repo.findAll() } returns flowOf(entity)
        coEvery { eRepo.findAll() } returns flowOf(empleado)

        val result = service.findAllDepartamentos()

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertEquals(dto.presupuesto, result[0].presupuesto) },
            { Assertions.assertEquals(dto.nombre, result[0].nombre) },
        )
        coVerify(exactly = 1) { repo.findAll() }
        coVerify(exactly = 1) { eRepo.findAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAllNF() = runTest {
        coEvery { repo.findAll() } returns flowOf()

        val result = service.findAllDepartamentos().toList()

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
        coEvery { eRepo.findAll() } returns flowOf(empleado)

        val result = service.findDepartamentoByUuid(dto.id)

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertEquals(dto.presupuesto, result.presupuesto) },
            { Assertions.assertEquals(dto.nombre, result.nombre) },
        )
        coVerify(exactly = 1) { repo.findByUUID(any()) }
        coVerify(exactly = 1) { eRepo.findAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findByUUIDNF() = runTest {
        coEvery { repo.findByUUID(any()) } returns null
        coEvery { eRepo.findAll() } returns flowOf(empleado)

        val result = assertThrows<DepartamentoExceptionNotFound> {
            service.findDepartamentoByUuid(dto.id)
        }

        Assertions.assertAll(
            { Assertions.assertEquals(
                "Couldn't find departamento with uuid ${dto.id}.", result.message
            ) }
        )
        coVerify { repo.findByUUID(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun save() = runTest {
        coEvery { repo.save(any()) } returns entity
        coEvery { eRepo.findAll() } returns flowOf(empleado)

        val result = service.saveDepartamento(dto)

        Assertions.assertAll(
            { Assertions.assertEquals(dto.presupuesto, result.presupuesto) },
            { Assertions.assertEquals(dto.nombre, result.nombre) },
        )

        coVerify { repo.save(any()) }
        coVerify { eRepo.findAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun delete() = runTest {
        coEvery { repo.findByUUID(any()) } returns entity
        coEvery { eRepo.findAll() } returns flowOf()
        coEvery { repo.delete(any()) } returns entity

        val result = service.deleteDepartamento(dto.id)

        Assertions.assertAll(
            { Assertions.assertEquals(dto.presupuesto, result?.presupuesto) },
            { Assertions.assertEquals(dto.nombre, result?.nombre) },
        )

        coVerify { repo.delete(any()) }
        coVerify { eRepo.findAll() }
        coVerify { repo.findByUUID(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteNF() = runTest {
        coEvery { repo.findByUUID(any()) } returns null
        coEvery { eRepo.findAll() } returns flowOf()
        coEvery { repo.delete(any()) } returns entity

        val result = assertThrows<DepartamentoExceptionNotFound> {
            service.deleteDepartamento(dto.id)
        }

        Assertions.assertAll(
            { Assertions.assertEquals(
                "Departamento with uuid ${dto.id} not found.", result.message
            ) }
        )

        coVerify { repo.findByUUID(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteBR() = runTest {
        coEvery { repo.findByUUID(any()) } returns entity
        coEvery { eRepo.findAll() } returns flowOf(empleado)
        coEvery { repo.delete(any()) } returns entity

        val result = assertThrows<DepartamentoExceptionBadRequest> {
            service.deleteDepartamento(dto.id)
        }

        Assertions.assertAll(
            { Assertions.assertEquals(
                "Cannot delete departamento. It has empleados attached to it.", result.message
            ) }
        )

        coVerify { repo.findByUUID(any()) }
        coVerify { eRepo.findAll() }
    }
}
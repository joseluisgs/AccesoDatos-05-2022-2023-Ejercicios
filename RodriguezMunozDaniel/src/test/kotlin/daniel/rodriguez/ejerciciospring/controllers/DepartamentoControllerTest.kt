package daniel.rodriguez.ejerciciospring.controllers

import daniel.rodriguez.ejerciciospring.dto.DepartamentoDTOcreacion
import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTO
import daniel.rodriguez.ejerciciospring.exception.DepartamentoExceptionNotFound
import daniel.rodriguez.ejerciciospring.exception.DepartamentoExceptionBadRequest
import daniel.rodriguez.ejerciciospring.mappers.toDTO
import daniel.rodriguez.ejerciciospring.models.Departamento
import daniel.rodriguez.ejerciciospring.models.Role
import daniel.rodriguez.ejerciciospring.models.User
import daniel.rodriguez.ejerciciospring.services.DepartamentoService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import java.util.*

@ExtendWith(MockKExtension::class)
@SpringBootTest
class DepartamentoControllerTest {
    @MockK lateinit var service: DepartamentoService
    @InjectMockKs lateinit var controller: DepartamentoController

    private val user = User(1L,
        UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea83aa"),
        "loli", "loli@gmail.com",
        "loli1707", Role.ADMIN
    )
    private val dto = DepartamentoDTOcreacion(
        id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003"),
        nombre = "Departamento falso", presupuesto = 69_420.69
    )
    private val dtomalo1 = DepartamentoDTOcreacion(
        id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003"),
        nombre = "", presupuesto = 69_420.69
    )
    private val dtomalo2 = DepartamentoDTOcreacion(
        id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003"),
        nombre = "Departamento falso", presupuesto = -69_420.69
    )
    private val entity = Departamento(2L, dto.id, dto.nombre, dto.presupuesto)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAll() = runTest {
        coEvery { service.findAllDepartamentos() } returns listOf(entity.toDTO(setOf()))

        val result = controller.findAll(user)
        val res = result.body!!

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertNotNull(res) },
            { Assertions.assertEquals(result.statusCode, HttpStatus.OK) },
            { Assertions.assertEquals(1, res.size) },
            { Assertions.assertEquals(dto.nombre, res[0].nombre) },
            { Assertions.assertEquals(dto.presupuesto, res[0].presupuesto) },
            { Assertions.assertEquals(listOf<EmpleadoDTO>(), res[0].empleados) }
        )

        coVerify(exactly = 1) { service.findAllDepartamentos() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAllNF() = runTest {
        coEvery { service.findAllDepartamentos() } returns listOf()

        val result = controller.findAll(user)
        val res = result.body!!

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertNotNull(res) },
            { Assertions.assertEquals(result.statusCode, HttpStatus.OK) },
            { Assertions.assertTrue(res.isEmpty()) }
        )

        coVerify(exactly = 1) { service.findAllDepartamentos() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findById() = runTest {
        coEvery { service.findDepartamentoByUuid(any()) } returns entity.toDTO(setOf())

        val result = controller.findByUUID(user, dto.id)
        val res = result.body!!

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertNotNull(res) },
            { Assertions.assertEquals(result.statusCode, HttpStatus.OK) },
            { Assertions.assertEquals(dto.nombre, res.nombre) },
            { Assertions.assertEquals(dto.presupuesto, res.presupuesto) },
            { Assertions.assertEquals(listOf<EmpleadoDTO>(), res.empleados) }
        )

        coVerify(exactly = 1) { service.findDepartamentoByUuid(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findByIdNF() = runTest {
        coEvery { service.findDepartamentoByUuid(any()) } throws
                DepartamentoExceptionNotFound("Couldn't find departamento with uuid ${dto.id}.")

        val res = assertThrows<DepartamentoExceptionNotFound> {
            controller.findByUUID(user, dto.id)
        }

        Assertions.assertEquals(
            "Couldn't find departamento with uuid ${dto.id}.", res.message
        )

        coVerify(exactly = 1) { service.findDepartamentoByUuid(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun create() = runTest {
        coEvery { service.saveDepartamento(any()) } returns entity.toDTO(setOf())

        val result = controller.create(dto, user)
        val res = result.body!!

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertNotNull(res) },
            { Assertions.assertEquals(result.statusCode, HttpStatus.CREATED) },
            { Assertions.assertEquals(dto.nombre, res.nombre) },
            { Assertions.assertEquals(dto.presupuesto, res.presupuesto) },
            { Assertions.assertEquals(listOf<EmpleadoDTO>(), res.empleados) }
        )

        coVerify(exactly = 1) { service.saveDepartamento(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createIncorrectoI() = runTest {
        val res = assertThrows<DepartamentoExceptionBadRequest> {
            controller.create(dtomalo1, user)
        }

        Assertions.assertEquals(
            "Nombre cannot be blank.", res.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createIncorrectoII() = runTest {
        val res = assertThrows<DepartamentoExceptionBadRequest> {
            controller.create(dtomalo2, user)
        }

        Assertions.assertEquals(
            "Presupuesto must be a positive number.", res.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun delete() = runTest {
        coEvery { service.deleteDepartamento(any()) } returns entity.toDTO(setOf())

        val result = controller.delete(user, dto.id)
        val res = result.body!!

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertNotNull(res) },
            { Assertions.assertEquals(result.statusCode, HttpStatus.OK) },
            { Assertions.assertEquals(dto.nombre, res.nombre) },
            { Assertions.assertEquals(dto.presupuesto, res.presupuesto) },
            { Assertions.assertEquals(listOf<EmpleadoDTO>(), res.empleados) }
        )

        coVerify(exactly = 1) { service.deleteDepartamento(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteNotFound() = runTest {
        coEvery { service.deleteDepartamento(any()) } throws
                DepartamentoExceptionNotFound("Departamento with uuid ${dto.id} not found.")

        val res = assertThrows<DepartamentoExceptionNotFound> {
            controller.delete(user, dto.id)
        }

        Assertions.assertEquals(
            "Departamento with uuid ${dto.id} not found.",
            res.message
        )
    }
}
package daniel.rodriguez.ejerciciospring.controllers

import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTOcreacion
import daniel.rodriguez.ejerciciospring.exception.EmpleadoExceptionBadRequest
import daniel.rodriguez.ejerciciospring.exception.EmpleadoExceptionNotFound
import daniel.rodriguez.ejerciciospring.mappers.toDTO
import daniel.rodriguez.ejerciciospring.models.Empleado
import daniel.rodriguez.ejerciciospring.models.Role
import daniel.rodriguez.ejerciciospring.models.User
import daniel.rodriguez.ejerciciospring.services.EmpleadoService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import java.util.*

@ExtendWith(MockKExtension::class)
@SpringBootTest
class EmpleadoControllerTest {
    @MockK lateinit var service: EmpleadoService
    @InjectMockKs lateinit var controller: EmpleadoController

    private val user = User(1L,
    UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea83aa"),
    "loli", "loli@gmail.com",
    "loli1707", Role.ADMIN
    )
    private val dto = EmpleadoDTOcreacion(
        id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0001"),
        nombre = "Nombre Generico", email = "empleado1@gmail.com", avatar = "",
        departamentoId = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003")
    )
    private val dtomalo1 = EmpleadoDTOcreacion(
        id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0001"),
        nombre = "", email = "empleado1@gmail.com", avatar = "",
        departamentoId = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003")
    )
    private val dtomalo2 = EmpleadoDTOcreacion(
        id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0001"),
        nombre = "Nombre Generico", email = "", avatar = "",
        departamentoId = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003")
    )
    private val dtomalo3 = EmpleadoDTOcreacion(
        id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0001"),
        nombre = "Nombre generico", email = "empleado1", avatar = "",
        departamentoId = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003")
    )
    private val entity = Empleado(
        2L, dto.id, dto.nombre, dto.email, dto.avatar, dto.departamentoId
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAll() = runTest {
        coEvery { service.findAllEmpleados() } returns listOf(entity.toDTO())

        val result = controller.findAll(user)
        val res = result.body!!

        assertAll(
            { assertNotNull(result) },
            { assertNotNull(res) },
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertEquals(1, res.size) },
            { assertEquals(dto.email, res[0].email) },
            { assertEquals(dto.nombre, res[0].nombre) },
            { assertEquals(dto.avatar, res[0].avatar) },
            { assertEquals(dto.departamentoId, res[0].departamentoId) }
        )

        coVerify(exactly = 1) { service.findAllEmpleados() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAllNF() = runTest {
        coEvery { service.findAllEmpleados() } returns listOf()

        val result = controller.findAll(user)
        val res = result.body!!

        assertAll(
            { assertNotNull(result) },
            { assertNotNull(res) },
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertTrue(res.isEmpty()) }
        )

        coVerify(exactly = 1) { service.findAllEmpleados() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findById() = runTest {
        coEvery { service.findEmpleadoByUuid(any()) } returns entity

        val result = controller.findByUUID(user, dto.id)
        val res = result.body!!

        assertAll(
            { assertNotNull(result) },
            { assertNotNull(res) },
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertEquals(dto.email, res.email) },
            { assertEquals(dto.nombre, res.nombre) },
            { assertEquals(dto.avatar, res.avatar) },
            { assertEquals(dto.departamentoId, res.departamentoId) }
        )

        coVerify(exactly = 1) { service.findEmpleadoByUuid(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findByIdNF() = runTest {
        coEvery { service.findEmpleadoByUuid(any()) } throws
                EmpleadoExceptionNotFound("Couldn't find empleado with uuid ${dto.id}.")

        val res = assertThrows<EmpleadoExceptionNotFound> {
            controller.findByUUID(user, dto.id)
        }

        assertEquals(
            "Couldn't find empleado with uuid ${dto.id}.", res.message
        )

        coVerify(exactly = 1) { service.findEmpleadoByUuid(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun create() = runTest {
        coEvery { service.saveEmpleado(any()) } returns entity.toDTO()

        val result = controller.create(dto, user)
        val res = result.body!!

        assertAll(
            { assertNotNull(result) },
            { assertNotNull(res) },
            { assertEquals(result.statusCode, HttpStatus.CREATED) },
            { assertEquals(dto.avatar, res.avatar) },
            { assertEquals(dto.nombre, res.nombre) },
            { assertEquals(dto.email, res.email) },
            { assertEquals(dto.departamentoId, res.departamentoId) }
        )

        coVerify(exactly = 1) { service.saveEmpleado(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createIncorrectoI() = runTest {
        val res = assertThrows<EmpleadoExceptionBadRequest> {
            controller.create(dtomalo1, user)
        }

        assertEquals(
            "Nombre cannot be blank.", res.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createIncorrectoII() = runTest {
        val res = assertThrows<EmpleadoExceptionBadRequest> {
            controller.create(dtomalo2, user)
        }

        assertEquals(
            "Email cannot be blank.", res.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createIncorrectoIII() = runTest {
        val res = assertThrows<EmpleadoExceptionBadRequest> {
            controller.create(dtomalo3, user)
        }

        assertEquals(
            "Invalid email.", res.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun delete() = runTest {
        coEvery { service.deleteEmpleado(any()) } returns entity.toDTO()

        val result = controller.delete(user, dto.id)
        val res = result.body!!

        assertAll(
            { assertNotNull(result) },
            { assertNotNull(res) },
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertEquals(dto.email, res.email) },
            { assertEquals(dto.nombre, res.nombre) },
            { assertEquals(dto.avatar, res.avatar) },
            { assertEquals(dto.departamentoId, res.departamentoId) }
        )

        coVerify(exactly = 1) { service.deleteEmpleado(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteNotFound() = runTest {
        coEvery { service.deleteEmpleado(any()) } throws
                EmpleadoExceptionNotFound("Empleado with uuid ${dto.id} not found.")

        val res = assertThrows<EmpleadoExceptionNotFound> {
            controller.delete(user, dto.id)
        }

        assertEquals(
            "Empleado with uuid ${dto.id} not found.",
            res.message
        )
    }
}
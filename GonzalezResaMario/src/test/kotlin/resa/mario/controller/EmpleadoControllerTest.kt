package resa.mario.controller

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import resa.mario.dto.EmpleadoDTO
import resa.mario.mappers.toEmpleado
import resa.mario.services.empleado.EmpleadoServiceImpl
import resa.mario.services.storage.StorageServiceImpl

@ExtendWith(MockKExtension::class)
@SpringBootTest
internal class EmpleadoControllerTest {

    private val entity = EmpleadoDTO(
        name = "Test3",
        email = "test3@example.com"
    )

    private val entityModel = entity.toEmpleado().copy(
        id = 99999
    )

    @MockK
    private lateinit var service: EmpleadoServiceImpl

    @MockK
    private lateinit var service2: StorageServiceImpl

    @InjectMockKs
    private lateinit var controller: EmpleadoController

    init {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAll() = runTest {
        coEvery { service.findAll() } returns flowOf(entityModel)

        val response = controller.findAll()

        assertEquals(response.statusCode, HttpStatus.OK)

        coVerify { service.findAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findById() = runTest {
        coEvery { service.findById(any()) } returns entityModel

        val response = controller.findById(entityModel.id.toString())

        assertEquals(response.statusCode, HttpStatus.OK)

        coVerify { service.findById(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun create() = runTest {
        coEvery { service.save(any()) } returns entityModel

        val response = controller.create(entity)

        assertEquals(response.statusCode, HttpStatus.OK)

        coVerify { service.save(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun update() = runTest {
        coEvery { service.update(any(), any()) } returns entityModel

        val response = controller.update(entityModel.id.toString(), entity)

        assertEquals(response.statusCode, HttpStatus.OK)

        coVerify { service.update(any(), any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun delete() = runTest {
        coEvery { service.deleteById(any()) } returns entityModel

        val response = controller.delete(entityModel.id.toString())

        assertEquals(response.statusCode, HttpStatus.OK)

        coVerify { service.deleteById(any()) }
    }
}
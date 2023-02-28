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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import resa.mario.dto.DepartamentoDTO
import resa.mario.mappers.toDepartamento
import resa.mario.services.departamento.DepartamentoServiceImpl

@ExtendWith(MockKExtension::class)
@SpringBootTest
internal class DepartamentoControllerTest {

    private val entity = DepartamentoDTO(
        nombre = "Test3",
        presupuesto = 500.0
    )

    private val entityModel = entity.toDepartamento().copy(
        id = 99999
    )

    @MockK
    private lateinit var service: DepartamentoServiceImpl

    @InjectMockKs
    private lateinit var controller: DepartamentoController

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

}
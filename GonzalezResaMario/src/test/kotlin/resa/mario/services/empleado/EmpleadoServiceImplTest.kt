package resa.mario.services.empleado

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import resa.mario.models.Empleado
import resa.mario.repositories.departamento.DepartamentoCachedRepositoryImpl
import resa.mario.repositories.empleado.EmpleadoCachedRepositoryImpl

@ExtendWith(MockKExtension::class)
@SpringBootTest
internal class EmpleadoServiceImplTest {

    private val empleado = Empleado(
        id = 9999,
        name = "Test2",
        email = "test2@test.com",
        avatar = "test2"
    )

    @MockK
    private lateinit var repository: EmpleadoCachedRepositoryImpl

    @MockK
    lateinit var repository2: DepartamentoCachedRepositoryImpl

    @InjectMockKs
    lateinit var service: EmpleadoServiceImpl

    init {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAll() = runTest {
        coEvery { repository.findAll() } returns flowOf(empleado)

        val res = service.findAll().toList()

        assertTrue(res.isNotEmpty())

        coVerify { repository.findAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findById() = runTest {
        coEvery { repository.findById(any()) } returns empleado

        val res = service.findById(empleado.id!!)

        assertEquals(empleado.name, res!!.name)

        coVerify { repository.findById(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun save() = runTest {
        coEvery { repository.save(any()) } returns empleado

        val res = service.save(empleado)

        assertEquals(empleado.name, res.name)

        coVerify { repository.save(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun update() = runTest {
        coEvery { repository.findById(any()) } returns empleado
        coEvery { repository.update(any(), any()) } returns empleado

        val res = service.update(empleado.id!!, empleado)

        assertEquals(empleado.name, res!!.name)

        coVerify { repository.update(any(), any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteById() = runTest {
        coEvery { repository.findById(any()) } returns empleado
        coEvery { repository.deleteById(any()) } returns empleado
        coEvery { repository2.findAll() } returns flowOf()

        val res = service.deleteById(empleado.id!!)

        assertEquals(empleado.name, res!!.name)

        coVerify { repository.deleteById(any()) }
    }
}
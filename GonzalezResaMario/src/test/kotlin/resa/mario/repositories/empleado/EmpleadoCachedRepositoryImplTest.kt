package resa.mario.repositories.empleado

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

@ExtendWith(MockKExtension::class)
@SpringBootTest
internal class EmpleadoCachedRepositoryImplTest {

    private val empleado = Empleado(
        id = 999,
        name = "Test",
        email = "test@test.com",
        avatar = "test"
    )

    @MockK
    lateinit var rep: EmpleadoRepository

    @InjectMockKs
    lateinit var repository: EmpleadoCachedRepositoryImpl

    init {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAll() = runTest {
        coEvery { rep.findAll() } returns flowOf(empleado)

        val res = repository.findAll().toList()

        assertTrue(res.isNotEmpty())

        coVerify { rep.findAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findById() = runTest {
        coEvery { rep.findById(any()) } returns empleado

        val res = repository.findById(empleado.id!!)

        assertEquals(empleado.name, res!!.name)

        coVerify { rep.findById(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun save() = runTest {
        coEvery { rep.save(any()) } returns empleado

        val res = repository.save(empleado)

        assertEquals(empleado.name, res.name)

        coVerify { rep.save(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun update() = runTest {
        coEvery { rep.findById(any()) } returns empleado
        coEvery { rep.save(any()) } returns empleado

        val res = repository.update(empleado.id!!, empleado)

        assertEquals(empleado.name, res!!.name)

        coVerify { rep.save(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteById() = runTest {
        coEvery { rep.findById(any()) } returns empleado
        coEvery { rep.delete(any()) } returns Unit

        repository.deleteById(empleado.id!!)

        coVerify { rep.findById(any()) }
        coVerify { rep.delete(any()) }
    }
}
package daniel.rodriguez.ejerciciospring.repositories.empleado

import daniel.rodriguez.ejerciciospring.models.Empleado
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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@ExtendWith(MockKExtension::class)
@SpringBootTest
internal class EmpleadoRepositoryCachedTest {
    private val entity = Empleado(
        uuid = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0002"),
        nombre = "Juan Manodeobra", email = "empleado2@gmail.com", avatar = "",
        departamentoId = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003")
    )

    @MockK
    lateinit var repo: EmpleadoRepository

    @InjectMockKs
    lateinit var repository: EmpleadoRepositoryCached

    init { MockKAnnotations.init(this) }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAll() = runTest {
        coEvery { repo.findAll() } returns flowOf(entity)

        val result = repository.findAll().toList()

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertEquals(entity.id, result[0].id) },
            { Assertions.assertEquals(entity.uuid, result[0].uuid) },
        )
        coVerify(exactly = 1) { repo.findAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAllNF() = runTest {
        coEvery { repo.findAll() } returns flowOf()

        val result = repository.findAll().toList()

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertTrue(result.isEmpty()) }
        )
        coVerify { repo.findAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findByUUID() = runTest {
        coEvery { repo.findByUuid(any()) } returns flowOf(entity)

        val result = repository.findByUUID(entity.uuid!!)

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertEquals(entity.id, result?.id) },
            { Assertions.assertEquals(entity.uuid, result?.uuid) },
        )
        coVerify(exactly = 1) { repo.findByUuid(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findByUUIDNF() = runTest {
        coEvery { repo.findByUuid(any()) } returns flowOf()

        val result = repository.findByUUID(entity.uuid!!)

        Assertions.assertAll(
            { Assertions.assertNull(result) }
        )
        coVerify { repo.findByUuid(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findByID() = runTest {
        coEvery { repo.findById(any()) } returns entity

        val result = repository.findById(1)

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertEquals(entity.id, result?.id) },
            { Assertions.assertEquals(entity.uuid, result?.uuid) },
        )
        coVerify { repo.findById(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findByIDNF() = runTest {
        coEvery { repo.findById(any()) } returns null

        val result = repository.findById(1)

        Assertions.assertAll(
            { Assertions.assertNull(result) }
        )
        coVerify { repo.findById(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun save() = runTest {
        coEvery { repo.save(any()) } returns entity

        val result = repository.save(entity)

        Assertions.assertAll(
            { Assertions.assertEquals(entity.id, result.id) },
            { Assertions.assertEquals(entity.uuid, result.uuid) },
        )

        coVerify { repo.save(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun delete() = runTest {
        coEvery { repo.findById(any()) } returns entity
        coEvery { repo.deleteById(any()) } returns Unit

        val result = repository.delete(1)

        Assertions.assertAll(
            { Assertions.assertEquals(entity.id, result?.id) },
            { Assertions.assertEquals(entity.uuid, result?.uuid) },
        )

        coVerify { repo.deleteById(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteNF() = runTest {
        coEvery { repo.findById(any()) } returns null
        coEvery { repo.deleteById(any()) } returns Unit

        val result = repository.delete(1)

        Assertions.assertAll(
            { Assertions.assertNull(result) }
        )

        coVerify { repo.findById(any()) }
    }
}
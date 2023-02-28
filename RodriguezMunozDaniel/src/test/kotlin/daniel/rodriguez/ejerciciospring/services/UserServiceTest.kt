package daniel.rodriguez.ejerciciospring.services

import daniel.rodriguez.ejerciciospring.dto.UserDTOcreacion
import daniel.rodriguez.ejerciciospring.exception.UserExceptionNotFound
import daniel.rodriguez.ejerciciospring.models.Role
import daniel.rodriguez.ejerciciospring.models.User
import daniel.rodriguez.ejerciciospring.repositories.user.UserRepository
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
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
@SpringBootTest
class UserServiceTest {
    private val dto = UserDTOcreacion(
        uuid = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea1707"),
        username = "user test", email = "usertest@gmail.com",
        password = "1234567", role = Role.EMPLEADO
    )
    private val entity = User(1L, dto.uuid, dto.username, dto.email, dto.password, dto.role)

    @MockK lateinit var repo: UserRepository
    @MockK lateinit var passwordEncoder: PasswordEncoder
    @InjectMockKs lateinit var service: UserService

    init { MockKAnnotations.init(this) }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAll() = runTest {
        coEvery { repo.findAll() } returns flowOf(entity)

        val result = service.findAllUsers()

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertEquals(dto.email, result[0].email) },
            { Assertions.assertEquals(dto.username, result[0].username) },
        )
        coVerify(exactly = 1) { repo.findAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findAllNF() = runTest {
        coEvery { repo.findAll() } returns flowOf()

        val result = service.findAllUsers().toList()

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

        val result = service.findUserByUuid(dto.uuid)

        Assertions.assertAll(
            { Assertions.assertNotNull(result) },
            { Assertions.assertEquals(dto.email, result.email) },
            { Assertions.assertEquals(dto.username, result.username) },
        )
        coVerify(exactly = 1) { repo.findByUuid(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findByUUIDNF() = runTest {
        coEvery { repo.findByUuid(any()) } returns flowOf()

        val result = assertThrows<UserExceptionNotFound> {
            service.findUserByUuid(dto.uuid)
        }

        Assertions.assertAll(
            { Assertions.assertEquals(
                "Couldn't find user with uuid ${dto.uuid}.", result.message
            ) }
        )
        coVerify { repo.findByUuid(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun save() = runTest {
        coEvery { repo.save(any()) } returns entity
        coEvery { passwordEncoder.encode(any()) } returns entity.password

        val result = service.saveUser(dto)

        Assertions.assertAll(
            { Assertions.assertEquals(dto.email, result.email) },
            { Assertions.assertEquals(dto.username, result.username) },
        )

        coVerify { repo.save(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun delete() = runTest {
        coEvery { repo.findByUuid(any()) } returns flowOf(entity)
        coEvery { repo.deleteById(any()) } returns Unit

        val result = service.deleteUser(dto.uuid)

        Assertions.assertAll(
            { Assertions.assertEquals(dto.email, result.email) },
            { Assertions.assertEquals(dto.username, result.username) },
        )

        coVerify { repo.deleteById(any()) }
        coVerify { repo.findByUuid(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteNF() = runTest {
        coEvery { repo.findByUuid(any()) } returns flowOf()
        coEvery { repo.deleteById(any()) } returns Unit

        val result = assertThrows<UserExceptionNotFound> {
            service.deleteUser(dto.uuid)
        }

        Assertions.assertAll(
            { Assertions.assertEquals(
                "User with uuid ${dto.uuid} not found.", result.message
            ) }
        )

        coVerify { repo.findByUuid(any()) }
    }
}
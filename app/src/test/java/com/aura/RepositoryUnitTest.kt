package com.aura

import com.aura.model.Account
import com.aura.model.LoginResponse
import com.aura.model.Transfer
import com.aura.model.TransferResponse
import com.aura.model.User
import com.aura.repository.Repository
import com.aura.service.HomeApiService
import com.aura.service.LoginApiService
import com.aura.service.TransferApiService
import com.aura.viewmodel.home.UserStateManager
import io.mockk.coEvery
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

/**
 * Repository unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner::class)
class RepositoryUnitTest {
    @Mock
    private lateinit var repository: Repository

    @Mock
    private lateinit var loginApiService: LoginApiService

    @Mock
    private lateinit var userStateManager: UserStateManager

    @Mock
    private lateinit var homeApiService: HomeApiService

    @Mock
    private lateinit var transferApiService: TransferApiService

    /**
     * Create an instance of Repository class using multiple dependencies such as homeApiService, userStateManager,
     * loginApiService and transferApiService.
     */
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = Repository(
            homeApiService,
            userStateManager,
            loginApiService,
            transferApiService
        )
    }

    /**
     * Test of the loginRequest method with result true.
     */

    @Test
    fun testLoginRequestGrantedTrue() {

        val identifier = "1234"
        val password = "p@sswOrd"
        val response = Response.success(LoginResponse(true))
        coEvery { loginApiService.postLogin(user = User(identifier, password)) } returns response
        val actualResponse = runBlocking { repository.loginRequest(identifier, password) }

        assertNotNull(response)

        assertEquals(true, actualResponse?.granted)
    }

    /**
     * Test of the loginRequest method with result false.
     */
    @Test
    fun testLoginRequestGrantedFalse() {

        val identifier = "4444"
        val password = "jocelyn"
        val response = Response.success(LoginResponse(false))
        coEvery { loginApiService.postLogin(user = User(identifier, password)) } returns response
        val actualResponse = runBlocking { repository.loginRequest(identifier, password) }

        assertNotNull(response)

        assertEquals(false, actualResponse?.granted)
    }

    /**
     * Test of the getUserAccount method with result ok to catch user account response.
     */
    @Test
    fun getUserAccountsOk() {
        val userId = "1234"
        coEvery { userStateManager.getUserId() } returns userId
        coEvery { homeApiService.getAccounts(userId) } returns Response.success(listOf())

        val result = runBlocking { repository.getUserAccounts() }

        assertEquals(Response.success<List<String>>(listOf()), result)

    }

    /**
     * Test of the getUserAccount method with empty list to result.
     */
    @Test
    fun getUserAccountsEmpty() {
        coEvery { userStateManager.getUserId() } returns null

        val result = runBlocking { repository.getUserAccounts() }

        assertEquals(emptyList<Account>(), result)

    }

    /**
     * Test of the transferRequest method is successful
     */
    @Test
    fun transferRequestSuccess() = runBlocking {
        val recipient = "recipientID"
        val amount = 100.0
        val sender = "senderID"
        val transfer = Transfer(sender, recipient, amount)
        coEvery { UserStateManager.getUserId() } returns sender
        coEvery { transferApiService.postTransfer(transfer) } returns Response.success(
            200,
            TransferResponse(result = true)
        )

        val result = repository.transferRequest(recipient, amount)

        assertNotNull(result)
        // The Elvis operator is useful for providing an alternative value when an expression is nullable, thus avoiding NullPointerException.
        assertTrue(result?.result ?: false)
    }
}



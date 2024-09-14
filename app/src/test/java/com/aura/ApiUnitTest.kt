package com.aura


import com.aura.model.AccountResponse
import com.aura.model.LoginResponse
import com.aura.model.Transfer
import com.aura.model.TransferResponse
import com.aura.model.User
import com.aura.service.HomeApiService
import com.aura.service.LoginApiService
import com.aura.service.TransferApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class ApiUnitTest {

    @Mock
    private lateinit var loginApiService: LoginApiService

    @Mock
    private lateinit var homeApiService: HomeApiService

    @Mock
    private lateinit var transferApiService: TransferApiService

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Test function login Api with valid credentials.
     * @return granted true.
     */
    @Test
    fun `login with valid credentials returns granted true`() = runTest {
        val request = User("1234", "validPassword")
        val response = Response.success(LoginResponse(true))
        `when`(loginApiService.postLogin(request)).thenReturn(response)

        val result = loginApiService.postLogin(request)

        assertEquals(true, result.body()?.granted)
    }

    /**
     * Test function login Api with invalid credentials.
     * @return granted false.
     */
    @Test
    fun `login with invalid credentials returns granted false`() = runTest {
        val request = User("invalidId", "invalidPassword")
        val response = Response.success(LoginResponse(false))
        `when`(loginApiService.postLogin(request)).thenReturn(response)

        val result = loginApiService.postLogin(request)

        assertEquals(false, result.body()?.granted)
    }

    /**
     * Test function login Api with valid credentials
     * @return HTTP code 200 OK.
     */
    @Test
    fun `login with valid credentials returns 200 OK`() = runTest {
        val request = User("1234", "validPassword")
        val response = Response.success(LoginResponse(true))
        `when`(loginApiService.postLogin(request)).thenReturn(response)

        val result = loginApiService.postLogin(request)

        assertEquals(200, result.code())
    }

    /**
     * Test function login Api with invalid credentials
     * @return HTTP code 401 Unauthorized.
     */
    @Test
    fun `login with invalid credentials returns 401 Unauthorized`() = runTest {
        val request = User("invalidId", "invalidPassword")
        val errorResponse = "Unauthorized".toResponseBody()
        val response = Response.error<LoginResponse>(401, errorResponse)
        `when`(loginApiService.postLogin(request)).thenReturn(response)

        val result = loginApiService.postLogin(request)

        assertEquals(401, result.code())
    }

    /**
     * Test function login Api with empty credentials.
     * @return HTTP code 400 Bad Request.
     */
    @Test
    fun `login with empty credentials returns 400 Bad Request`() = runTest {
        val request = User("", "")
        val errorResponse = "Bad Request".toResponseBody()
        val response = Response.error<LoginResponse>(400, errorResponse)
        `when`(loginApiService.postLogin(request)).thenReturn(response)

        val result = loginApiService.postLogin(request)

        assertEquals(400, result.code())
    }

    /**
     * Test function home Api with valid credentials.
     * @return list AccountResponse.
     */
    @Test
    fun `Get account with valid credentials returns list AccountResponse`() = runTest {
        val request = "1234"
        val response = Response.success(listOf(AccountResponse("1234", true, 2454.23)))
        `when`(homeApiService.getAccounts("1234")).thenReturn(response)
        val result = homeApiService.getAccounts(request)
        assertEquals(response.body(), result.body())
    }

    /**
     * Test function home Api with empty credentials.
     * @return empty list.
     */
    @Test
    fun `Get account with empty credentials returns empty list`() = runTest {
        val request = ""
        val response = Response.success(emptyList<AccountResponse>())
        `when`(homeApiService.getAccounts("")).thenReturn(response)
        val result = homeApiService.getAccounts(request)
        assertEquals(response.body(), result.body())
    }

    /**
     * Test function transfer Api with valid credentials.
     * @return result true.
     */
    @Test
    fun `transfer with valid credentials returns result true`() = runTest {
        val request = Transfer("1234", "5678", 25.00)
        val response = Response.success(TransferResponse(true))
        `when`(
            transferApiService.postTransfer(
                Transfer(
                    "1234",
                    "5678",
                    25.00
                )
            )
        ).thenReturn(response)
        val result = transferApiService.postTransfer(request)
        assertEquals(true, result.body()?.result ?: true)


    }
}

package com.aura

import com.aura.model.Transfer
import com.aura.model.TransferResponse
import com.aura.service.TransferApiService
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

/**
 * TransferApiService unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class TransferApiServiceUnitTest {

    /**
     * Create an instance of TransferApiService interface.
     */
    @Mock
    private lateinit var apiService: TransferApiService

    /**
     * Test PostTransfer Method witch correctly elements.
     */
    @Test
    suspend fun testPostTransfer() {
        // Mock Response
        val transferResponse = TransferResponse(true)
        val response = Response.success(200, transferResponse)

        // Stubbing
        Mockito.`when`(apiService.postTransfer(Mockito.any(Transfer::class.java)))
            .thenReturn(response)

        // Create Transfer object
        val transfer = Transfer("5678", "1234", 100.00)

        // Make API call
        val actualResponse = apiService.postTransfer(transfer)

        // Assertion
        assertEquals(response, actualResponse)
    }
}

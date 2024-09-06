package com.aura.service

import com.aura.model.Transfer
import com.aura.model.TransferResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *  * interface Api to post a bank transfer between two users.
 *  * @return transfer result.
 */
interface TransferApiService {
    @POST("/transfer")
    suspend fun postTransfer(@Body transfer: Transfer): Response<TransferResponse>
}
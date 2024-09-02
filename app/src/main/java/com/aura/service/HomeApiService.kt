package com.aura.service

import com.aura.model.AccountResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * interface Api to get with user id, accounts.
 * @return id, main and balance user.
 */
interface HomeApiService {
    @GET("/accounts/{id}")
    suspend fun getAccounts(@Path("id") id : String) : Response<List<AccountResponse>>


}
package com.aura.service

import com.aura.bo.AccountResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApiService {
    @GET("/accounts/{id}")
    suspend fun getAccounts(@Path("id") id : String) : Response<List<AccountResponse>>


}
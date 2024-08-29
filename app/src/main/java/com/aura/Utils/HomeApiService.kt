package com.aura.Utils

import com.aura.Bo.Account
import com.aura.Bo.AccountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeApiService {
    @GET("/accounts/{id}")
    suspend fun getAccounts(@Path("id") id : String) : Response<List<AccountResponse>>


}
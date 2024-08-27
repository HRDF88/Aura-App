package com.aura.Utils

import com.aura.Bo.LoginResponse
import com.aura.Bo.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response

interface LoginApiService {
    @POST("/login")
    suspend fun postLogin(@Body user : User) : Response<LoginResponse>
}
package com.aura.service

import com.aura.bo.LoginResponse
import com.aura.bo.User
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface LoginApiService {
    @POST("/login")
    suspend fun postLogin(@Body user : User) : Response<LoginResponse>
}
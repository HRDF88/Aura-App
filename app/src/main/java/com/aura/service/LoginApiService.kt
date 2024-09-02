package com.aura.service

import com.aura.model.LoginResponse
import com.aura.model.User
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

/**
 * Api interface to post id and password to identify the user on the api.
 * @return LoginResponse (granted : true or false).
 */
interface LoginApiService {
    @POST("/login")
    suspend fun postLogin(@Body user : User) : Response<LoginResponse>
}
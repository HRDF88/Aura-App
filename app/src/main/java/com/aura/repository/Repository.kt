package com.aura.repository

import com.aura.model.AccountResponse
import com.aura.model.LoginResponse
import com.aura.model.Transfer
import com.aura.model.TransferResponse
import com.aura.model.User
import com.aura.service.HomeApiService
import com.aura.service.LoginApiService
import com.aura.service.TransferApiService
import com.aura.viewmodel.home.UserStateManager

/**
 * Data repository to account of User, catch list of account by User.
 */

class Repository(
    private val homeApiService: HomeApiService,
    private val userStateManager: UserStateManager,
    private val apiServiceLoginApiService: LoginApiService,
    private val transferApiService: TransferApiService
) {
    /**
     *Call the Api to log user.
     * @return response to loginApiService (granted).
     */
    suspend fun loginRequest(identifier: String, password: String): LoginResponse? {
        val user = User(identifier, password)
        val response = apiServiceLoginApiService.postLogin(user)
        return response.body()
    }

    /**
     * Call the Api to catch the user's accounts.
     * @return response from homeApiService (id, main and balance).
     */

    suspend fun getUserAccounts(): List<AccountResponse> {
        val userId = userStateManager.getUserId()
        // Check if userId is null.
        return if (userId != null) {
            val response = homeApiService.getAccounts(userId)
            response.body() ?: emptyList()
        } else {
            // Handles the case where userId is null.
            emptyList()
        }
    }

    /**
     * Call the Api to transfer money between users.
     * @return result to transferApiService.
     */
    suspend fun transferRequest(
        recipient: String,
        amount: Double
        /*result can be null if the api does not find the beneficiary
          and in upstream cannot find the sender (user id).*/
    ): TransferResponse? {
        val sender = UserStateManager.getUserId()
        val transfer = sender?.let { Transfer(it, recipient, amount) }
        return transfer?.let { transferApiService.postTransfer(it) }?.body()
    }
}
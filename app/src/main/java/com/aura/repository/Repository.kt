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

        // Vérifier si userId est null
        if (userId != null) {
            val response = homeApiService.getAccounts(userId)
            return response.body() ?: emptyList()
        } else {
            // Gérer le cas où userId est null
            return emptyList()
        }
    }

    suspend fun transferRequest(
        recipient: String,
        amount: Double
    ): TransferResponse? {
        val sender = UserStateManager.getUserId()
        val transfer = sender?.let { Transfer(it, recipient, amount) }
        val response = transfer?.let { transferApiService.postTransfer(it) }
        if (response != null) {
            return response.body()
        } else {
            return null
        }
    }


}
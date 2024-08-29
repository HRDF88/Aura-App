package com.aura.data

import com.aura.Bo.AccountResponse
import com.aura.Utils.HomeApiService
import com.aura.viewmodel.home.UserStateManager

/**
 * Data repository to account of User, catch list of account by User.
 */

class HomeRepository(
    private val apiService: HomeApiService,
    private val userStateManager: UserStateManager
) {

    /**
     * Call the Api to catch the user's accounts.
     */
    suspend fun getUserAccounts(): List<AccountResponse> {
        val userId = userStateManager.getUserId()
        userId?.let {
            val response = apiService.getAccounts(it)
            return response.body() ?: emptyList()
        }
        return emptyList()
    }
}
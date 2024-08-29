package com.aura.viewmodel.home

/**
 * this object catch the user ID after the login is successful.
 */
object UserStateManager {
    private var userId: String? = null

    fun setUserId(id: String) {
        userId = id
    }

    fun getUserId(): String? {
        return userId
    }

    fun clearUserId() {
        userId = null
    }
}
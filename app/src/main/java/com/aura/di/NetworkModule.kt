package com.aura.di

import com.aura.service.HomeApiService
import com.aura.service.LoginApiService
import com.aura.service.RetrofitClient
import com.aura.service.TransferApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt NetworkModule for Api Class.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideLoginApiService(): LoginApiService {
        return RetrofitClient.loginApiService
    }

    @Provides
    fun provideHomeApiService(): HomeApiService {
        return RetrofitClient.homeApiService
    }

    @Provides
    fun provideTransferApiService(): TransferApiService {
        return RetrofitClient.transferApiService
    }
}
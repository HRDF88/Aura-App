package com.aura.di

import com.aura.Utils.LoginApiService
import com.aura.Utils.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideLoginApiService():LoginApiService{
        return RetrofitClient.loginApiService
    }
}
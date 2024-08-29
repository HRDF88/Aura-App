package com.aura.di

import com.aura.service.HomeApiService
import com.aura.service.LoginApiService
import com.aura.service.RetrofitClient
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
    @Provides
    fun provideHomeApiService():HomeApiService{
        return  RetrofitClient.homeApiService
    }
}
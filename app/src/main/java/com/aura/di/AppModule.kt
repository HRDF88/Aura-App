package com.aura.di

import android.app.Application
import android.content.Context
import com.aura.service.HomeApiService
import com.aura.repository.Repository
import com.aura.service.LoginApiService
import com.aura.service.TransferApiService
import com.aura.viewmodel.home.UserStateManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt AppModule to dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
    @Provides
    fun provideHomeRepository(apiService: HomeApiService, userStateManager: UserStateManager,apiServiceLoginApiService: LoginApiService,transferApiService: TransferApiService):Repository{
        return Repository(apiService, userStateManager,apiServiceLoginApiService, transferApiService)
    }
    @Provides
    fun provideUserStateManager(): UserStateManager{
        return UserStateManager
    }
}
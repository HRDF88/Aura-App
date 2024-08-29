package com.aura.di

import android.app.Application
import android.content.Context
import com.aura.Utils.HomeApiService
import com.aura.data.HomeRepository
import com.aura.viewmodel.home.UserStateManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
    @Provides
    fun provideHomeRepository(apiService: HomeApiService, userStateManager: UserStateManager):HomeRepository{
        return HomeRepository(apiService, userStateManager)
    }
    @Provides
    fun provideUserStateManager(): UserStateManager{
        return UserStateManager
    }
}
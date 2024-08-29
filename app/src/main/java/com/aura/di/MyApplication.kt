package com.aura.di

import android.app.Application
import com.aura.data.HomeRepository
import com.aura.viewmodel.home.HomeViewModel
import dagger.Component
import dagger.hilt.android.HiltAndroidApp
import viewmodel.login.LoginViewModel
import javax.inject.Singleton

/**
 * NotesApplication is responsible for providing application-level dependencies.
 */
@HiltAndroidApp
class MyApplication : Application() {



    @Singleton
    @Component(modules = [NetworkModule::class])
    interface AppComponent {

    }

    @Component(modules = [AppModule::class])
    interface LoginComponent{
        fun inject(viewModel: LoginViewModel)
    }
    @Component(modules = [AppModule::class])
    interface HomeComponent{
        fun inject(repository: HomeRepository)
        fun inject(homeViewModel: HomeViewModel)
    }
}
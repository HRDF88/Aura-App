package com.aura.di

import android.app.Application
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
}
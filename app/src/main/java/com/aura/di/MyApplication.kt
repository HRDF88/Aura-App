package com.aura.di

import android.app.Application
import com.aura.repository.Repository
import com.aura.viewmodel.home.HomeViewModel
import dagger.Component
import dagger.hilt.android.HiltAndroidApp
import com.aura.viewmodel.login.LoginViewModel
import com.aura.viewmodel.transfer.TransferViewModel
import javax.inject.Singleton

/**
 * NotesApplication is responsible for providing application-level dependencies.
 */
@HiltAndroidApp
class MyApplication : Application() {
    /**
     * Hilt Singleton and Component to dependency injection.
     */

    @Singleton
    @Component(modules = [NetworkModule::class])
    interface AppComponent {

    }

    @Component(modules = [AppModule::class])
    interface LoginComponent {
        fun inject(viewModel: LoginViewModel)
    }

    @Component(modules = [AppModule::class])
    interface HomeComponent {
        fun inject(repository: Repository)
        fun inject(homeViewModel: HomeViewModel)
    }

    @Component(modules = [AppModule::class])
    interface TransferComponent {
        fun inject(repository: Repository)
        fun inject(transferViewModel: TransferViewModel)
    }
}
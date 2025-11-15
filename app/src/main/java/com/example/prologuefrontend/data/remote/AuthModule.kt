package com.example.prologuefrontend.data.remote

import android.content.Context
import com.example.prologuefrontend.data.AuthLocalDataSource
import com.example.prologuefrontend.data.AuthRepository
import com.example.prologuefrontend.network.auth.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthLocalDataSource(
    @ApplicationContext context: Context
    ): AuthLocalDataSource = AuthLocalDataSource(context)


    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi,
        local: AuthLocalDataSource
    ): AuthRepository {
        return AuthRepository(
            localDataSource = local,
            api = api
        )
    }
}
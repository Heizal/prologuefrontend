package com.example.prologuefrontend.data.remote

import android.content.Context
import com.example.prologuefrontend.data.repository.BookRepository
import com.example.prologuefrontend.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideBookRepository(
        api: ApiService,
        @ApplicationContext context: Context
    ): BookRepository {
        return BookRepository(api, context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: ApiService): UserRepository {
        return UserRepository(api)
    }
}
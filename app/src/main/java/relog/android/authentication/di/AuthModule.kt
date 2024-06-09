package relog.android.authentication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import relog.android.authentication.data.repository.AuthRepository
import relog.android.authentication.data.repository.AuthRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @Provides
    @ViewModelScoped
    fun provideAuthRepository() = AuthRepositoryImpl() as AuthRepository
}
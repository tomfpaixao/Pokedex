package com.tomasfp.pokedex.di

import com.tomasfp.pokedex.repository.HomeRepository
import com.tomasfp.pokedex.repository.HomeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class ApplicationDataModule {

    @Binds
    @Singleton
    abstract fun bindsRepository(repository: HomeRepositoryImpl) : HomeRepository

}
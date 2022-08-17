package com.tomasfp.pokedex.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tomasfp.pokedex.data.db.AppDB
import com.tomasfp.pokedex.data.remote.PokemonService
import com.tomasfp.pokedex.repository.HomeRepository
import com.tomasfp.pokedex.ui.home.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            ).build()
    }

    @Provides
    @Singleton
    fun providePokemonService(retrofit: Retrofit) : PokemonService {
        return retrofit.create(PokemonService::class.java)
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    object HomeModule {

        @Provides
        fun providesHomeViewModel(homeRepository: HomeRepository) = HomeViewModel(repository = homeRepository)
    }


    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ): AppDB {
        return Room.databaseBuilder(
            app,
            AppDB::class.java,
            "pokedex_db"
        ).build()
    }
}
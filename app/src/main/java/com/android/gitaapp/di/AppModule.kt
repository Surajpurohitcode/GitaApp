package com.android.gitaapp.di

import android.content.Context
import com.android.gitaapp.data.remote.ApiService
import com.android.gitaapp.data.remote.ElevenLabsApi
import com.android.gitaapp.data.repository.ElevenLabsRepository
import com.android.gitaapp.data.repository.ElevenLabsRepositoryImpl
import com.android.gitaapp.data.repository.GitaRepository
import com.android.gitaapp.data.repository.GitaRepositoryImpl
import com.android.gitaapp.utils.ElevenLabsRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = "https://vedicscriptures.github.io"

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideGitaRepository(apiService: ApiService): GitaRepository =
        GitaRepositoryImpl(apiService)

    // ElevenLabs specific
    @Provides
    @Singleton
    @ElevenLabsRetrofit
    fun provideElevenLabsRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.elevenlabs.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideElevenLabsApi(
        @ElevenLabsRetrofit retrofit: Retrofit
    ): ElevenLabsApi = retrofit.create(ElevenLabsApi::class.java)

    @Provides
    @Singleton
    fun provideElevenLabsRepository(
        api: ElevenLabsApi,
        @ApplicationContext context: Context
    ): ElevenLabsRepository = ElevenLabsRepositoryImpl(api, context)
}
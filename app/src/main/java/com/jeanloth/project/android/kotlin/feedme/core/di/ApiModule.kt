package com.jeanloth.project.android.kotlin.feedme.core.di

import com.jeanloth.project.android.kotlin.feedme.features.command.data.external.services.NominatimApi
import com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl.GoogleMapRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val BASE_URL = "https://nominatim.openstreetmap.org/"

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideNominatimApiService(retrofit: Retrofit): NominatimApi = retrofit.create(NominatimApi::class.java)

    @Singleton
    @Provides
    fun providesRepository(nominatimApi: NominatimApi) = GoogleMapRepositoryImpl(nominatimApi)
}
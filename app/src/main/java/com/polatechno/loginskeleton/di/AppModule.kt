package com.polatechno.loginskeleton.di

import com.google.gson.GsonBuilder
import com.polatechno.loginskeleton.common.Constants
import com.polatechno.loginskeleton.data.remote.PravoeDeloApi
import com.polatechno.loginskeleton.data.repository.MyRepositoryImpl
import com.polatechno.loginskeleton.domain.repository.MyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMyApi(): PravoeDeloApi {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PravoeDeloApi::class.java)
    }


    @Provides
    @Singleton
    fun provideMyRepository(api: PravoeDeloApi): MyRepository {
        return MyRepositoryImpl(api)
    }
}
package com.example.fitness.di

import android.app.Application
import com.example.fitness.network.api.apihelper.ApiHelper
import com.example.fitness.network.api.apihelperimpl.ApiHelperImpl
import com.example.fitness.network.api.apiservice.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/* It inform hilt how to provide instance of creation type  */
@Module
/* To tell hilt which android class each module will be used */
@InstallIn(ApplicationComponent::class)

object ApiModule {
    //* Tell hilt how to provide instances of this type by creating a function inside a hilt module *//*
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()/* The method returns the Gson object */
    }

    /* The method returns the Cache object */
    @Provides
    @Singleton
    fun provideCache(application: Application): Cache {
        val cacheSize: Long = 10 * 1024 * 1024 //10MB
        return Cache(File(application.cacheDir, "http-cache"), cacheSize)
    }

    /* The method returns the Okhttp object */
    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache): OkHttpClient {
        val httpClient = OkHttpClient.Builder().cache(cache)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        httpClient.connectTimeout(5, TimeUnit.MINUTES)
        httpClient.readTimeout(5, TimeUnit.MINUTES)
        return httpClient.build()
    }


    /* The method returns the Retrofit object */
    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://192.168.11.43/dietplan/")
            .client(okHttpClient)
            .build()
    }
    /* We need the MovieApiService module.
    * For this, We need the Retrofit object, Gson, Cache and OkHttpClient .
    * So we will define the providers for these objects here in this module.*/

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper
}
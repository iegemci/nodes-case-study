package com.enesgemci.mymovies.di

import android.app.Application
import com.enesgemci.mymovies.R
import com.enesgemci.mymovies.network.MovieAPI
import com.enesgemci.mymovies.network.RequestInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor())
            .addInterceptor(StethoInterceptor())
            .addInterceptor(HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger { message -> Timber.e(message) }).apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, context: Application): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()
    }

    @Provides
    @Singleton
    fun provideTaxiAPI(retrofit: Retrofit): MovieAPI {
        return retrofit.create(MovieAPI::class.java)
    }
}

package com.example.app_drawer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providerNotionClient(): OkHttpClient {
        return OkHttpClient.Builder().run {
            addInterceptor { chain ->
                with(chain) {
                    val newRequest = request().newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer secret_7bZz1bsybczodqK8pC2dCkhVoHer7DJNfLH0zntaK36"
                        )
                        .addHeader("Notion-Version", "2022-06-28")
                        .addHeader("Content-Type", "application/json")
                        .build()
                    proceed(newRequest)
                }
            }
        }.build()
    }

    @Provides
    @Singleton
    fun providerRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .client(client).baseUrl("https://api.notion.com")
            .build()
    }

}
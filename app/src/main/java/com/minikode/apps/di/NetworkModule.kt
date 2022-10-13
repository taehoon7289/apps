package com.minikode.apps.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
                    try {
                        val newRequest = request().newBuilder()
                            .addHeader(
                                "Authorization",
                                "Bearer secret_7bZz1bsybczodqK8pC2dCkhVoHer7DJNfLH0zntaK36"
                            )
                            .addHeader("Notion-Version", "2022-06-28")
                            .addHeader("Content-Type", "application/json")
                            .build()
                        proceed(newRequest)
                    } catch (e: SocketTimeoutException) {
                        e.printStackTrace()
                        throw SocketTimeoutException()
                    } catch (e: UnknownHostException) {
                        e.printStackTrace()
                        throw UnknownHostException()
                    }
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
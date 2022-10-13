package com.minikode.apps.di

import com.minikode.apps.NotionApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun providerNotionApiService(retrofit: Retrofit): NotionApiService {
        return retrofit.create(NotionApiService::class.java)
    }

}
package com.minikode.apps.di

import com.minikode.apps.NotionApiService
import com.minikode.apps.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providerAlarmRepository() = AlarmRepository()

    @Provides
    @Singleton
    fun providerLikeRepository() = LikeRepository()

    @Provides
    @Singleton
    fun providerNotificationRepository(service: NotionApiService): NotificationRepository {
        return NotificationRepository(service)
    }

    @Provides
    @Singleton
    fun providerUsageStatsRepository() = UsageStatsRepository()

    @Provides
    @Singleton
    fun providerDonationRepository() = DonationRepository()

}
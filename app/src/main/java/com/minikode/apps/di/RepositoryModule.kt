package com.minikode.apps.di

import com.minikode.apps.NotionApiService
import com.minikode.apps.repository.AlarmRepository
import com.minikode.apps.repository.NotificationRepository
import com.minikode.apps.repository.UsageStatsRepository
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
    fun providerNotificationRepository(service: NotionApiService): NotificationRepository {
        return NotificationRepository(service)
    }

    @Provides
    @Singleton
    fun providerUsageStatsRepository() = UsageStatsRepository()


}
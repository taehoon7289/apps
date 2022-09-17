package com.example.app_drawer.di

import com.example.app_drawer.NotionApiService
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.repository.NotificationRepository
import com.example.app_drawer.repository.UsageStatsRepository
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
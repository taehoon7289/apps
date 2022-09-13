package com.example.app_drawer.module

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.repository.AppNotificationRepository
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
    fun providerAlarmState() = AlarmRepository()

    @Provides
    @Singleton
    fun providerNotificationState() = AppNotificationRepository()

    @Provides
    @Singleton
    fun providerUsageStatsState() = UsageStatsRepository()



    @Provides
    @Singleton
    fun providerDataStore() = dataStore

}
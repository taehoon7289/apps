package com.example.app_drawer.state

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppStateModule {

    @Provides
    @Singleton
    fun providerAlarmState() = AppAlarmState()

    @Provides
    @Singleton
    fun providerNotificationState() = AppNotificationState()

    @Provides
    @Singleton
    fun providerUsageStatsState() = AppUsageStatsState()

}
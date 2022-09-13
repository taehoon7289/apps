package com.example.app_drawer.module

import com.example.app_drawer.grid_view.adapter.AppGridViewAdapter
import com.example.app_drawer.recycler_view.adapter.AppAlarmRecyclerViewAdapter
import com.example.app_drawer.recycler_view.adapter.AppRecyclerViewAdapter
import com.example.app_drawer.view_pager2.adapter.AppNotificationViewPagerAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

//    @Provides
//    @Singleton
//    fun providerNotificationViewPagerAdapter() = AppNotificationViewPagerAdapter()

//    @Provides
//    @Singleton
//    fun providerAlarmRecyclerViewAdapter() = AppAlarmRecyclerViewAdapter()

//    @Provides
//    @Singleton
//    @Named("recent")
//    fun providerRecentRecyclerViewAdapter() = AppRecyclerViewAdapter()

//    @Provides
//    @Singleton
//    @Named("often")
//    fun providerOftenRecyclerViewAdapter() = AppRecyclerViewAdapter()

//    @Provides
//    @Singleton
//    @Named("un")
//    fun providerUnRecyclerViewAdapter() = AppRecyclerViewAdapter()

//    @Provides
//    @Singleton
//    fun providerRecyclerViewAdapter() = AppRecyclerViewAdapter()

//    @Provides
//    @Singleton
//    fun providerGridViewAdapter() = AppGridViewAdapter()
}
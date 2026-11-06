package com.taqiyyaghazi.eventapp.di

import android.content.Context
import com.taqiyyaghazi.eventapp.data.local.room.EventDatabase
import com.taqiyyaghazi.eventapp.data.pref.SettingPreferences
import com.taqiyyaghazi.eventapp.data.pref.dataStore
import com.taqiyyaghazi.eventapp.data.remote.retrofit.ApiConfig
import com.taqiyyaghazi.eventapp.data.repository.EventRepository
import com.taqiyyaghazi.eventapp.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getDatabase(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }

    fun providePreferences(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}

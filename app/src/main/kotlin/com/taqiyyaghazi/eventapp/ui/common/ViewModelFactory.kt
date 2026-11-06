package com.taqiyyaghazi.eventapp.ui.common

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.taqiyyaghazi.eventapp.data.pref.SettingPreferences
import com.taqiyyaghazi.eventapp.data.repository.EventRepository
import com.taqiyyaghazi.eventapp.di.Injection
import com.taqiyyaghazi.eventapp.ui.detail.DetailViewModel
import com.taqiyyaghazi.eventapp.ui.favorite.FavoriteViewModel
import com.taqiyyaghazi.eventapp.ui.finished.FinishedViewModel
import com.taqiyyaghazi.eventapp.ui.home.HomeViewModel
import com.taqiyyaghazi.eventapp.ui.setting.SettingViewModel
import com.taqiyyaghazi.eventapp.ui.upcoming.UpcomingViewModel

class ViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val settingPreferences: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> {
                UpcomingViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> {
                FinishedViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(settingPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.providePreferences(context)
                )
            }.also { instance = it }
    }
}

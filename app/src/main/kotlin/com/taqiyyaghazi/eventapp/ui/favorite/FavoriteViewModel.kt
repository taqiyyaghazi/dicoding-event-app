package com.taqiyyaghazi.eventapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.taqiyyaghazi.eventapp.data.local.entity.EventEntity
import com.taqiyyaghazi.eventapp.data.repository.EventRepository

class FavoriteViewModel(private val repository: EventRepository) : ViewModel() {
    val favoriteEvents: LiveData<List<EventEntity>> = repository.getFavoriteEvents()
}

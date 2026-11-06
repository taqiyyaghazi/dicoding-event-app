package com.taqiyyaghazi.eventapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taqiyyaghazi.eventapp.data.local.entity.EventEntity
import com.taqiyyaghazi.eventapp.data.remote.response.ListEventsItem
import com.taqiyyaghazi.eventapp.data.repository.EventRepository
import com.taqiyyaghazi.eventapp.utils.Result
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {

    fun getDetailEvent(id: String): LiveData<Result<ListEventsItem>> =
        repository.getDetailEvent(id)

    fun isFavorite(id: String): LiveData<Boolean> =
        repository.isFavorite(id)

    fun setFavorite(event: EventEntity, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.setFavorite(event, isFavorite)
        }
    }
}

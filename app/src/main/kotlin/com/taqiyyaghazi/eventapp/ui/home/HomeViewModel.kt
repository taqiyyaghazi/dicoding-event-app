package com.taqiyyaghazi.eventapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.taqiyyaghazi.eventapp.data.remote.response.ListEventsItem
import com.taqiyyaghazi.eventapp.data.repository.EventRepository
import com.taqiyyaghazi.eventapp.utils.Result

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    val upcomingEvents: LiveData<Result<List<ListEventsItem>>> = repository.getUpcomingEvents(limit = 5)
    val finishedEvents: LiveData<Result<List<ListEventsItem>>> = repository.getFinishedEvents(limit = 5)

    private val searchQuery = MutableLiveData<String>()

    val searchResults: LiveData<Result<List<ListEventsItem>>> = searchQuery.switchMap { query ->
        repository.searchEvents(query)
    }

    fun search(query: String) {
        if (query.isNotBlank()) {
            searchQuery.value = query
        }
    }
}

package com.taqiyyaghazi.eventapp.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.taqiyyaghazi.eventapp.data.remote.response.ListEventsItem
import com.taqiyyaghazi.eventapp.data.repository.EventRepository
import com.taqiyyaghazi.eventapp.utils.Result

class UpcomingViewModel(private val repository: EventRepository) : ViewModel() {
    val upcomingEvents: LiveData<Result<List<ListEventsItem>>> = repository.getUpcomingEvents()
}

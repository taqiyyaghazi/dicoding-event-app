package com.taqiyyaghazi.eventapp.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.taqiyyaghazi.eventapp.data.remote.response.ListEventsItem
import com.taqiyyaghazi.eventapp.data.repository.EventRepository
import com.taqiyyaghazi.eventapp.utils.Result

class FinishedViewModel(private val repository: EventRepository) : ViewModel() {
    val finishedEvents: LiveData<Result<List<ListEventsItem>>> = repository.getFinishedEvents()
}

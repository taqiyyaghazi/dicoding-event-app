package com.taqiyyaghazi.eventapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.taqiyyaghazi.eventapp.data.local.entity.EventEntity
import com.taqiyyaghazi.eventapp.data.local.room.EventDao
import com.taqiyyaghazi.eventapp.data.remote.response.ListEventsItem
import com.taqiyyaghazi.eventapp.data.remote.retrofit.ApiService
import com.taqiyyaghazi.eventapp.utils.AppExecutors
import com.taqiyyaghazi.eventapp.utils.Result
import kotlinx.coroutines.Dispatchers

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    fun getUpcomingEvents(limit: Int? = null): LiveData<Result<List<ListEventsItem>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(active = 1)
            val events = if (limit != null) response.listEvents.take(limit) else response.listEvents
            emit(Result.Success(events))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Gagal memuat event aktif"))
        }
    }

    fun getFinishedEvents(limit: Int? = null): LiveData<Result<List<ListEventsItem>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(active = 0)
            val events = if (limit != null) response.listEvents.take(limit) else response.listEvents
            emit(Result.Success(events))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Gagal memuat event selesai"))
        }
    }

    fun searchEvents(query: String): LiveData<Result<List<ListEventsItem>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.searchEvents(active = -1, query = query)
            emit(Result.Success(response.listEvents))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Pencarian gagal"))
        }
    }

    fun getDetailEvent(id: String): LiveData<Result<ListEventsItem>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailEvent(id)
            val event = response.event
            if (event != null) {
                emit(Result.Success(event))
            } else {
                emit(Result.Error("Detail event tidak ditemukan"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Gagal memuat detail event"))
        }
    }

    fun getFavoriteEvents(): LiveData<List<EventEntity>> = eventDao.getAllFavoriteEvents()

    fun isFavorite(id: String): LiveData<Boolean> = eventDao.isFavoriteEvent(id)

    suspend fun setFavorite(event: EventEntity, isFavorite: Boolean) {
        if (isFavorite) {
            eventDao.insertFavorite(event)
        } else {
            eventDao.deleteFavorite(event)
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}

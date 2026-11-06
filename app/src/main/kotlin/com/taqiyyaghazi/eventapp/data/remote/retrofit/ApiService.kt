package com.taqiyyaghazi.eventapp.data.remote.retrofit

import com.taqiyyaghazi.eventapp.data.remote.response.DetailEventResponse
import com.taqiyyaghazi.eventapp.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int // 1 = upcoming, 0 = finished, -1 = all
    ): EventResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String
    ): EventResponse

    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: String
    ): DetailEventResponse
}

package com.taqiyyaghazi.eventapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.taqiyyaghazi.eventapp.data.remote.response.ListEventsItem
import com.taqiyyaghazi.eventapp.data.repository.EventRepository
import com.taqiyyaghazi.eventapp.utils.Result
import com.taqiyyaghazi.eventapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var eventRepository: EventRepository
    private lateinit var homeViewModel: HomeViewModel

    private val dummyEvents = listOf(
        ListEventsItem(
            id = 1,
            name = "Dicoding Conference",
            summary = "Ringkasan Conference",
            description = "Deskripsi Conference",
            imageLogo = "https://example.com/logo.jpg",
            mediaCover = "https://example.com/cover.jpg",
            category = "Technology",
            ownerName = "Dicoding",
            cityName = "Bandung",
            quota = 100,
            registrants = 40,
            beginTime = "2026-12-01 09:00:00",
            endTime = "2026-12-01 12:00:00",
            link = "https://dicoding.com"
        )
    )

    @Before
    fun setUp() {
        val expectedUpcoming = MutableLiveData<Result<List<ListEventsItem>>>()
        expectedUpcoming.value = Result.Success(dummyEvents)
        `when`(eventRepository.getUpcomingEvents(5)).thenReturn(expectedUpcoming)

        val expectedFinished = MutableLiveData<Result<List<ListEventsItem>>>()
        expectedFinished.value = Result.Success(dummyEvents)
        `when`(eventRepository.getFinishedEvents(5)).thenReturn(expectedFinished)

        homeViewModel = HomeViewModel(eventRepository)
    }

    @Test
    fun `when getUpcomingEvents successfully should return Success with dummy data`() {
        val actualResult = homeViewModel.upcomingEvents.getOrAwaitValue()

        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Success)
        Assert.assertEquals(1, (actualResult as Result.Success).data.size)
        Assert.assertEquals("Dicoding Conference", actualResult.data[0].name)
        verify(eventRepository).getUpcomingEvents(5)
    }

    @Test
    fun `when getFinishedEvents successfully should return Success with dummy data`() {
        val actualResult = homeViewModel.finishedEvents.getOrAwaitValue()

        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Success)
        Assert.assertEquals(1, (actualResult as Result.Success).data.size)
        Assert.assertEquals("Dicoding Conference", actualResult.data[0].name)
        verify(eventRepository).getFinishedEvents(5)
    }
}

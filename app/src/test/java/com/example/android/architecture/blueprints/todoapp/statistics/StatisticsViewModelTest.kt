package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeRepository
import com.example.android.architecture.blueprints.todoapp.tasks.getOrAwaitValue

import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StatisticsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var statisticsViewModel: StatisticsViewModel
    private lateinit var tasksRepository: FakeRepository

    @Before
    fun setupStatisticsViewModel() {
        tasksRepository = FakeRepository()
        statisticsViewModel = StatisticsViewModel(tasksRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadTasks_loading() {

        // Pause dispatcher so we can verify initial values.
        mainCoroutineRule.pauseDispatcher()

        // Load the task in the view model.
        statisticsViewModel.refresh()

        // Then progress indicator is shown.
        assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(true))

        // Execute pending coroutine actions.
        mainCoroutineRule.resumeDispatcher()

        // Then progress indicator is hidden.

        assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun loadStatisticsWhenTasksAreUnabailable_callErrorToDisplay() {
        tasksRepository.setShouldReturnError(true)
        statisticsViewModel.refresh()

        assertThat(statisticsViewModel.empty.getOrAwaitValue(), `is`(true))
        assertThat(statisticsViewModel.error.getOrAwaitValue(), `is`(true))
    }
}
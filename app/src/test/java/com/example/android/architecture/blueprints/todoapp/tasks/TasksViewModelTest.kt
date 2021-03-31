package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.R

import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeRepository

import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.hamcrest.core.IsNot.not

import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.robolectric.annotation.Config

@Config(manifest=Config.NONE)
@ExperimentalCoroutinesApi
class TasksViewModelTest {

    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var tasksRepository: FakeRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        tasksRepository = FakeRepository()
        val task0 = Task("Title0", "Description2")
        val task1 = Task("Title1", "Description1", true)
        val task2 = Task("Title2", "Description0", true)
        tasksRepository.addTasks(task0, task1, task2)
        tasksViewModel = TasksViewModel(tasksRepository)
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
        tasksViewModel.addNewTask()
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), (not(nullValue())))
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // WHEN the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)
        // THEN the "Add task" action is visible
        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is`(true))
    }

    @Test
    fun doneTask_dataSnackUpdated() {
        val task = Task("TitleQ", "DescriptionQ")
        tasksRepository.addTasks(task)

        // Mark the task as complete.
        tasksViewModel.completeTask(task, true)

        // Verify the task is complete.
        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))

        // Assert that the snackbar has been updated with the correct text.
        val snackbarText: Event<Int> = tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
    }
}
package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.data.Result

import com.example.android.architecture.blueprints.todoapp.data.Task

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(manifest=Config.NONE)
class DefaultTasksRepositoryTest {
    private val task0 = Task("Title0", "Description2")
    private val task1 = Task("Title1", "Description1")
    private val task2 = Task("Title2", "Description0")
    private val remoteTasks = listOf(task0, task1).sortedBy { it.id }
    private val localTasks = listOf(task2).sortedBy { it.id }
    private val newTasks = listOf(task2).sortedBy { it.id }

    private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var tasksLocalDataSource: FakeDataSource

    // Class under test
    private lateinit var tasksRepository: DefaultTasksRepository

    @Before
    fun createRepository() {
        tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())
        // Get a reference to the class being tested.
        tasksRepository = DefaultTasksRepository(
            tasksRemoteDataSource,
            tasksLocalDataSource,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun getTasks_requestsAllTasksFromRemoteDataSource() = runBlockingTest {
        val tasks = tasksRepository.getTasks(true) as Result.Success
        assertThat(tasks.data, IsEqual(remoteTasks))
    }
}
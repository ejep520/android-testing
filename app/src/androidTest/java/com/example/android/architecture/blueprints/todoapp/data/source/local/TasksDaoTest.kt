package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest

import com.example.android.architecture.blueprints.todoapp.data.Task

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {

    private lateinit var database: ToDoDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            ToDoDatabase::class.java
        ).build()
    }

    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        // Given - Insert a task.
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        // When - Get the task by ID from the database.
        val loaded = database.taskDao().getTaskById(task.id)

        // Then - The loaded data contains the expected values.
        assertThat(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(task.id))
        assertThat(loaded.title, `is`(task.title))
    }

    @Test
    fun updateTaskAndGetById() = runBlockingTest{
        // 1. Insert a task into the DAO.
        val originalTask = Task("oldTitle", "oldDescription")
        database.taskDao().insertTask(originalTask)

        // 2. Update the task by creating a new task with the same ID but different attributes.
        val newTask = Task("boldTitle", "bigShinyDescription", id=originalTask.id)
        database.taskDao().updateTask(newTask)
        val loaded = database.taskDao().getTaskById(originalTask.id)

        // 3. Check that when you get the task by its ID, it has the updated values.
        assertThat(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(newTask.id))
        assertThat(loaded.title, `is`("boldTitle"))
        assertThat(loaded.description, `is`("bigShinyDescription"))
    }

    @After
    fun closeDb() = database.close()
}
package com.example.android.architecture.blueprints.todoapp.tasks

import android.content.Context
import android.os.Bundle

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest

import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class TasksFragmentTest {
    private lateinit var repository: TasksRepository

    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @Test
    fun clickTask_navigateToDetailFragmentOne() = runBlockingTest {
        repository.saveTask(Task(
            "Title0",
            "Description0",
            false,
            "id0")
        )
        repository.saveTask(Task(
            "Title1",
            "Description1",
            true,
            "id1")
        )

        // Given On the home screen
        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)
        val navControl = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navControl)
        }

        // When - Click on the first list item
        onView(withId(R.id.tasks_list))
            .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText("Title0")), click()
            ))

        // Then - Verify that we navigate to the first detail screen.
        verify(navControl).navigate(
            TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment("id0")
        )
    }

    @Test
    fun clickAddTaskButton_navigateToAddEditFragment() = runBlockingTest {
        repository.saveTask(
            Task(
                "Title0",
                "Description0",
                false,
                "id0"
            )
        )
        repository.saveTask(
            Task(
                "Title1",
                "Description1",
                true,
                "id1"
            )
        )

        // Given On the home screen
        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)
        val navControl = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navControl)
        }

        // When - Click on the + button.
        onView(withId(R.id.add_task_fab)).perform(click())

        // Then - Verify that we navigate to the add screen.
        verify(navControl).navigate(
            TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                null, getApplicationContext<Context>().getString(R.string.add_task)
            )
        )
    }

    @After
    fun cleanupDb() {
        ServiceLocator.resetRepository()
    }
}
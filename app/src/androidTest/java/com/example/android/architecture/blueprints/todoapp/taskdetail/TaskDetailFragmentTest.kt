package com.example.android.architecture.blueprints.todoapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest

import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

import org.hamcrest.core.IsNot.not

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class TaskDetailFragmentTest {
    private lateinit var tasksRepository: TasksRepository

    @Before
    fun initRepository() {
        tasksRepository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = tasksRepository
    }

    @Test
    fun activeTaskDetails_DisplayedInUi() = runBlockingTest {
        // Given add active, incomplete task to the DB.
        val activeTask = Task("Active Task", "AndroidX Rox0rs", false)
        tasksRepository.saveTask(activeTask)

        // When details fragment launched to display task
        val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)

        // Then Task details are displayed on the screen.
        // Make sure that the title and description are both shown and correct.
        onView(withId(R.id.task_detail_title_text))
            .check(matches(isDisplayed()))
            .check(matches(withText("Active Task")))
        onView(withId(R.id.task_detail_description_text))
            .check(matches(isDisplayed()))
            .check(matches(withText("AndroidX Rox0rs")))
        // And make sure the "active" checkbox is unchecked.
        onView(withId(R.id.task_detail_complete_checkbox))
            .check(matches(isDisplayed()))
            .check(matches(not(isChecked())))
    }

    @Test
    fun completedTaskDetails_DisplayedInUi() = runBlockingTest {
        // Given - Add completed task to the DB
        val completedTitle = "Completed Task"
        @Suppress("SpellCheckingInspection")
        val completedDescription = "This task is all done. Done done donesky. Yup!"
        val completedTask = Task(completedTitle, completedDescription, true)
        tasksRepository.saveTask(completedTask)

        // When - Details fragment launched to display task
        val bundle = TaskDetailFragmentArgs(completedTask.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)

        // Then - Task details are displayed on the screen.
        // Make sure that the title and description are both shown and correct.
        onView(withId(R.id.task_detail_title_text))
            .check(matches(isDisplayed()))
            .check(matches(withText(completedTitle)))
        onView(withId(R.id.task_detail_description_text))
            .check(matches(isDisplayed()))
            .check(matches(withText(completedDescription)))
        onView(withId(R.id.task_detail_complete_checkbox))
            .check(matches(isDisplayed()))
            .check(matches(isChecked()))
    }

    @After
    fun cleanupDb() {
        runBlockingTest {
            ServiceLocator.resetRepository()
        }
    }
}
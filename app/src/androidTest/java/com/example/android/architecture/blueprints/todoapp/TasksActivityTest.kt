package com.example.android.architecture.blueprints.todoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest

import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.ITasksRepository
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity

import kotlinx.coroutines.runBlocking

import org.hamcrest.core.IsNot.not

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
@LargeTest
class TasksActivityTest {
    private lateinit var repository: ITasksRepository

    @BeforeEach
    fun initRepository() {
        repository = ServiceLocator.provideTasksRepository(getApplicationContext())
        runBlocking { repository.deleteAllTasks() }
    }

    @RepeatedTest(10)
    fun editTask() = runBlocking {
        // Set the initial state
        repository.saveTask(Task("Title0", "Description0"))

        // Start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)

        // Click on the task on the list and verify that all the data is correct.
        onView(withText("Title0")).perform(click())
        onView(withId(R.id.task_detail_title_text)).check(matches(withText("Title0")))
        onView(withId(R.id.task_detail_description_text)).check(matches(withText("Description0")))
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))

        // Click on the edit button, edit, and save.
        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("newTitle1"))
        onView(withId(R.id.add_task_description_edit_text))
            .perform(replaceText("newDescription1"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // Verify task is displayed on screen in the task list
        onView(withText("newTitle1")).check(matches(isDisplayed()))

        // Verify previous task is not displayed.
        onView(withText("Title0")).check(doesNotExist())

        // Make sure the activity is closed before resetting the DB.
        activityScenario.close()
    }

    @AfterEach
    fun resetRepository() = ServiceLocator.resetRepository()
}
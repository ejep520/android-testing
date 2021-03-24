package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

import org.junit.Test

class StatisticsUtilsTest {
    @Test
    fun getActiveAndCompletedStats_zeroCompleted_returnsZeroHundred() {
        // GIVEN a list of tasks where all are incomplete
        val tasks = listOf(Task("Title", "Description", isCompleted = false))

        // WHEN requesting percentages of complete and incomplete
        val result = getActiveAndCompletedStats(tasks)

        // THEN complete should be 0 and incomplete should be 100.
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(100f))
    }
    @Test
    fun getActiveAndCompletedStats_both_returnsFortySixty() {
        // GIVEN a list of tasks where 2 are complete and 3 are incomplete.
        val tasks = listOf(Task("Title0", "Description4", isCompleted = true),
            Task("Title1", "Description3", isCompleted = true),
            Task("Title2", "Description2", isCompleted = false),
            Task("Title3", "Description1", isCompleted = false),
            Task("Title4", "Description0", isCompleted = false))

        // WHEN requesting percentages of complete and incomplete
        val result = getActiveAndCompletedStats(tasks)

        // THEN complete should be 40% and incomplete should be 60%
        assertThat(result.completedTasksPercent, `is`(40f))
        assertThat(result.activeTasksPercent, `is`(60f))
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZeroZero() {
        // GIVEN an empty list of tasks
        val tasks = listOf<Task>()

        // WHEN requesting percentages of complete and incomplete
        val result = getActiveAndCompletedStats(tasks)

        // THEN complete and incomplete should return 0%
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_error_returnsZeroZero() {
        // GIVEN a null token in place of a list
        val tasks = null

        // WHEN requesting percentages of complete and incomplete
        val result = getActiveAndCompletedStats(tasks)

        // THEN complete and incomplete should return 0%
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }
}

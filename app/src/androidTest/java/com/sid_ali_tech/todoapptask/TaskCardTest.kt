package com.sid_ali_tech.todoapptask

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.sid_ali_tech.todoapptask.domain.model.Todo
import com.sid_ali_tech.todoapptask.presentation.task.TaskCard
import org.junit.Rule
import org.junit.Test

class TaskCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun taskCard_Rendering() {
        val task = Todo(
            id = 1,
            Title = "Sample Task",
            date = "2023-12-31",
            completed = false
        )
        composeTestRule.setContent {
            TaskCard(
                task = task,
                navController = rememberNavController(),
                onDeleteTask = {  },
                onFinishTask = { _, _ -> }
            )
        }
    }
    @Test
    fun taskCard_SwipeActions() {
        val task = Todo(
            id = 1,
            Title = "Swipe Task",
            date = "2023-12-31",
            completed = false
        )

        composeTestRule.setContent {
            TaskCard(
                task = task,
                navController = rememberNavController(),
                onDeleteTask = {  },
                onFinishTask = { _, _ ->  }
            )
        }
    }
}

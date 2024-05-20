package com.example.myfitfriend.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myfitfriend.MainActivity
import com.example.myfitfriend.presentation.dietarylogs.DietaryLogsScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class DietaryLogsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testDisplayNutritionalInfo() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            DietaryLogsScreen(navController = navController)
        }

        // Assertions
        composeTestRule.onNodeWithText("Total Calories:").assertExists()
        composeTestRule.onNodeWithText("Total Carbs:").assertExists()
        composeTestRule.onNodeWithText("Total Protein:").assertExists()
        composeTestRule.onNodeWithText("Total Fats:").assertExists()
    }
}
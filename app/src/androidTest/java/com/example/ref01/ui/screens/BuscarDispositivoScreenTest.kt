package com.example.ref01.ui.screens


import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import com.example.ref01.MainActivity
import org.junit.Rule
import org.junit.Test


class BuscarDispositivoScreenTest {


    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()


    @Test
    fun click_find_button_triggers_search() {
        composeRule.onNodeWithTag("find_button").assertIsDisplayed()
        composeRule.onNodeWithTag("find_button").performClick()
// Verifica un cambio visible tras el click (texto/indicador/snackbar)
        composeRule.onNodeWithTag("location_text").assertIsDisplayed()
    }
}
package com.example.ref01.ui.components

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.ref01.MainActivity
import org.junit.Rule
import org.junit.Test

class AppTopBarTest {
    @get:Rule val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun themeToggle_y_ttsPlayPause() {
        // El TopBar aparece donde lo uses; aquí solo demostramos eventos
        rule.onNodeWithTag("theme_toggle_btn").performClick()
        rule.onNodeWithTag("tts_play_btn").performClick()
        // tras play, debería mostrarse el botón de pause
        rule.onNodeWithTag("tts_pause_btn").assertExists()
    }
}


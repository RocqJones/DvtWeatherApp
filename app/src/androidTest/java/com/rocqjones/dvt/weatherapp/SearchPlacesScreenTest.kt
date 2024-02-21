package com.rocqjones.dvt.weatherapp

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rocqjones.dvt.weatherapp.fakes.FakePlacesProvider
import com.rocqjones.dvt.weatherapp.ui.design.screens.SearchPlacesScreen
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class SearchPlacesScreenTest {

    private var fakePlacesProvider = FakePlacesProvider()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val apiKey = context.resources.getString(R.string.api_key)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        fakePlacesProvider.initialize(context = context, apiKey = apiKey)
    }

    @Test
    fun verifyPlacesIsInitialized() {
        val isInitialized = fakePlacesProvider.isInitialized()
        assertTrue(isInitialized)
    }

    @Test
    fun whenInitializedDisplayContent() {
        composeTestRule.setContent {
            SearchPlacesScreen(
                navController = rememberNavController(),
                placesProvider = fakePlacesProvider
            )
        }

        // Then verify that content is displayed
        composeTestRule.onNodeWithText(
            "Tap here to search Location..."
        ).assertIsDisplayed()
    }

    @Test
    fun testButtonClickLaunchesMapInputOverlay() {
        // Mocked ActivityResultLauncher
        val fakeLauncher = mock(
            ActivityResultLauncher::class.java
        ) as ActivityResultLauncher<*>

        // When SearchPlacesScreen is launched
        composeTestRule.setContent {
            SearchPlacesScreen(
                navController = rememberNavController(),
                placesProvider = fakePlacesProvider
            )
        }

        composeTestRule.onNodeWithText(
            "Tap here to search Location..."
        ).assertExists()

        composeTestRule.onNodeWithText(
            "Tap here to search Location..."
        ).performClick()

        // Verify that the intent is launched
        verify(fakeLauncher)
    }
}
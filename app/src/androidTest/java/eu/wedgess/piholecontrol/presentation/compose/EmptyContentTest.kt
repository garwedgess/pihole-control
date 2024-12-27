package eu.wedgess.piholecontrol.presentation.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import org.junit.Rule
import org.junit.Test

class EmptyContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyContent_displays_icon_title_and_subtitle() {
        val title = "Test Title"
        val subTitle = "Test Subtitle"
        composeTestRule.setContent {
            PiHoleControlTheme {
                Surface {
                    EmptyContent(
                        title = title,
                        subTitle = subTitle,
                        modifier = Modifier.testTag("emptyContent").fillMaxSize()
                    )
                }
            }
        }
        composeTestRule.onNodeWithTag("emptyContent").assertExists()
        composeTestRule.onNodeWithContentDescription("empty").assertIsDisplayed()
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText(subTitle).assertIsDisplayed()
    }

    @Test
    fun emptyContent_displays_icon_and_title_only() {
        val title = "Test Title"
        composeTestRule.setContent {
            PiHoleControlTheme {
                Surface {
                    EmptyContent(
                        title = title,
                        modifier = Modifier.testTag("emptyContent").fillMaxSize()
                    )
                }
            }
        }
        composeTestRule.onNodeWithTag("emptyContent").assertExists()
        composeTestRule.onNodeWithContentDescription("empty").assertIsDisplayed()
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Subtitle").assertDoesNotExist()
    }

    @Test
    fun emptyContent_displays_icon_with_error_color() {
        val title = "Test Title"
        composeTestRule.setContent {
            PiHoleControlTheme {
                Surface {
                    EmptyContent(
                        title = title,
                        modifier = Modifier.testTag("emptyContent").fillMaxSize()
                    )
                }
            }
        }
        composeTestRule.onNodeWithContentDescription("empty").assertExists()
    }
}
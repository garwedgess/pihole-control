package eu.wedgess.piholecontrol.presentation.common.components

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.utils.UiText

private const val BACK_PRESS_INTERVAL = 2_000L

@Composable
fun DoublePressToExitBackHandler(
    exitMessage: UiText = UiText.StringResource(R.string.all_back_press_msg),
    backPressInterval: Long = BACK_PRESS_INTERVAL
) {
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < backPressInterval) {
            (context as? Activity)?.finish()
        } else {
            backPressedTime = currentTime
            Toast.makeText(context, exitMessage.asString(context), Toast.LENGTH_SHORT).show()
        }
    }
}
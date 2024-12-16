package eu.wedgess.piholecontrol.utils.extensions

import android.os.Build
import androidx.compose.ui.graphics.Color

fun Color.toColorInt() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    android.graphics.Color.rgb(this.red, this.green, this.blue)
} else {
    android.graphics.Color.rgb(
        (this.red * 255).toInt(),
        (this.green * 255).toInt(),
        (this.blue * 255).toInt()
    )
}

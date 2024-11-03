package eu.wedgess.piholecontrol.ui.statistics.view.donutchart

import androidx.compose.ui.geometry.Offset
import eu.wedgess.piholecontrol.ui.statistics.view.donutchart.extensions.DONUT_CHART_TOTAL_ANGLE
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Find the distance based on two points in a graph. Calculated using the pythagorean theorem.
 */
internal fun findTouchDistanceFromCenter(center: Offset, touch: Offset) =
    sqrt((touch.x - center.x).pow(2) + (touch.y - center.y).pow(2))

/**
 * The touch point start from Canvas top left which ranges from (0,0) -> (canvas.width, canvas.height).
 * We need to normalize this point so that it's based on the canvas center instead.
 */
internal fun Offset.findNormalizedPointFromTouch(canvasCenter: Offset) =
    Offset(this.x, canvasCenter.y + (canvasCenter.y - this.y))

/**
 * Calculate the touch angle based on the canvas center. Then adjust the angle so that
 * drawing starts from the 4th quadrant instead of the first.
 */
internal fun calculateTouchAngleAccordingToCanvas(
    canvasCenter: Offset,
    normalizedPoint: Offset
): Float {
    val angle = calculateTouchAngleInDegrees(canvasCenter, normalizedPoint)
    return adjustAngleToCanvas(angle).toFloat()
}

/**
 * Calculate touch angle in radian using atan2(). Afterwards, convert the radian to degrees to be
 * compared to other data points.
 */
private fun calculateTouchAngleInDegrees(canvasCenter: Offset, normalizedPoint: Offset): Double {
    val touchInRadian = kotlin.math.atan2(normalizedPoint.y - canvasCenter.y,
        normalizedPoint.x - canvasCenter.x)
    return touchInRadian * -180 / Math.PI // Convert radians to angle in degrees
}

/**
 * Start from 4th quadrant going to 1st quadrant, degrees ranging from 0 to 360
 */
private fun adjustAngleToCanvas(angle: Double) =
    (angle + DONUT_CHART_TOTAL_ANGLE) % DONUT_CHART_TOTAL_ANGLE
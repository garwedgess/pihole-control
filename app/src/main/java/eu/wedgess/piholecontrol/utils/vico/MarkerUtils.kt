package eu.wedgess.piholecontrol.utils.vico

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.LayeredComponent
import com.patrykandpatrick.vico.compose.common.component.ShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import java.text.DecimalFormat
import kotlin.math.roundToInt

val xDisplayValuesKey = ExtraStore.Key<Map<Int, String>>()
val yDisplayLabelsKey = ExtraStore.Key<List<String?>>()

@Composable
fun rememberMarker(): CartesianMarker {
    val valueFormatter = remember {
        DefaultCartesianMarker.ValueFormatter { context, targets ->
            val pattern = DecimalFormat("#.##;-#.##")
            val xDisplayValues = context.model.extraStore.getOrNull(xDisplayValuesKey).orEmpty()
            val yDisplayLabels = context.model.extraStore.getOrNull(yDisplayLabelsKey).orEmpty()
            val xValue = targets.firstOrNull()?.x ?: return@ValueFormatter ""
            val xLabel = xDisplayValues[xValue.roundToInt()] ?: pattern.format(xValue)
            val points = targets
                .filterIsInstance<LineCartesianLayerMarkerTarget>()
                .flatMap { it.points }

            buildAnnotatedString {
                if (points.size > ONE_LINE_POINT_LIMIT) {
                    append(xLabel)
                    append(" ")
                    points.forEachIndexed { index, point ->
                        if (index > 0) {
                            append("\n")
                        }
                        appendPointLabel(point, yDisplayLabels, pattern)
                    }
                } else {
                    append(xLabel)
                    append(if (points.size > 1) " (" else " ")
                    points.forEachIndexed { index, point ->
                        if (index > 0) {
                            append(" : ")
                        }
                        appendPointLabel(point, yDisplayLabels, pattern)
                    }
                    if (points.size > 1) {
                        append(")")
                    }
                }
            }
        }
    }
    val labelBackground = rememberShapeComponent(
        fill = Fill(MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(50)
    )
    val indicatorHoleFill = Fill(MaterialTheme.colorScheme.surface)
    val indicator = remember(indicatorHoleFill) {
        { color: Color ->
            LayeredComponent(
                back = ShapeComponent(
                    fill = Fill(color.copy(alpha = INDICATOR_GLOW_ALPHA)),
                    shape = CircleShape
                ),
                front = LayeredComponent(
                    back = ShapeComponent(
                        fill = Fill(color.copy(alpha = INDICATOR_OUTER_ALPHA)),
                        shape = CircleShape
                    ),
                    front = ShapeComponent(
                        fill = indicatorHoleFill,
                        shape = CircleShape,
                        strokeFill = Fill(color),
                        strokeThickness = INDICATOR_STROKE_THICKNESS
                    ),
                    padding = Insets(INDICATOR_RING_PADDING)
                ),
                padding = Insets(INDICATOR_GLOW_PADDING)
            )
        }
    }

    return rememberDefaultCartesianMarker(
        label = rememberTextComponent(
            MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = FontFamily.Monospace
            ),
            lineCount = LABEL_LINE_COUNT,
            padding = Insets(LABEL_HORIZONTAL_PADDING, LABEL_VERTICAL_PADDING),
            background = labelBackground
        ),
        valueFormatter = valueFormatter,
        indicator = indicator,
        indicatorSize = INDICATOR_SIZE,
        guideline = rememberLineComponent(
            fill = Fill(MaterialTheme.colorScheme.onSurface.copy(alpha = GUIDELINE_ALPHA)),
            thickness = GUIDELINE_THICKNESS
        )
    )
}

private fun AnnotatedString.Builder.appendPointLabel(
    point: LineCartesianLayerMarkerTarget.Point,
    yDisplayLabels: List<String?>,
    pattern: DecimalFormat
) {
    withStyle(SpanStyle(color = point.color)) {
        append(pattern.format(point.entry.y))
        val label = yDisplayLabels.getOrNull(point.entry.seriesIndex)
        if (label != null) {
            append(" ")
            append(label)
        }
    }
}

private const val ONE_LINE_POINT_LIMIT = 2
private const val LABEL_LINE_COUNT = 3
private const val GUIDELINE_ALPHA = .2f
private const val INDICATOR_GLOW_ALPHA = .1f
private const val INDICATOR_OUTER_ALPHA = .22f
private val LABEL_HORIZONTAL_PADDING = 12.dp
private val LABEL_VERTICAL_PADDING = 8.dp
private val GUIDELINE_THICKNESS = 2.dp
private val INDICATOR_SIZE = 26.dp
private val INDICATOR_GLOW_PADDING = 3.dp
private val INDICATOR_RING_PADDING = 4.dp
private val INDICATOR_STROKE_THICKNESS = 3.dp

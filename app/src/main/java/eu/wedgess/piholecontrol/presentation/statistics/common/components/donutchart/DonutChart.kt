package eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.extensions.calculateGapAngle
import eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model.DonutChartDataCollection
import eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model.DonutChartState
import eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model.QueryTypeChartData
import eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model.STROKE_SIZE_UNSELECTED
import eu.wedgess.piholecontrol.presentation.theme.isDark
import eu.wedgess.piholecontrol.utils.extensions.degreeToRadian
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private data class DrawingAngles(val start: Float, val end: Float)

private fun DrawingAngles.isInsideAngle(angle: Float) =
    angle > this.start && angle < this.start + this.end

@Composable
fun DonutChart(
    data: DonutChartDataCollection,
    modifier: Modifier = Modifier,
    chartSize: Dp = 350.dp,
    minDisplayPercentage: Float = 5f,
    gapPercentage: Float = 0.00f,
    onSelectedIndexChange: (Int) -> Unit
) {
    val isDarkTheme = MaterialTheme.colorScheme.isDark
    var selectedIndex by remember { mutableIntStateOf(-1) }

    // Use tween with easing for smoother animations
    val animationSpec = remember {
        tween<Float>(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    }

    // Track total percentage for smooth transitions when items are removed
    val totalPercentage = remember(data) {
        data.items.sumOf { it.percentage.toDouble() }.toFloat()
    }

    // Animate percentages with the new spec
    val animatedPercentages = data.items.map { item ->
        animateFloatAsState(
            targetValue = item.percentage,
            animationSpec = animationSpec,
            label = "percentage animation"
        )
    }

    val animationTargetState = (0..data.items.size).map {
        remember { mutableStateOf(DonutChartState()) }
    }

    val animValues = (0..data.items.size).map {
        animateDpAsState(
            targetValue = animationTargetState[it].value.stroke,
            animationSpec = TweenSpec(700),
            label = "slice animation"
        )
    }
    val anglesList: MutableList<DrawingAngles> = remember { mutableListOf() }
    val gapAngle = data.calculateGapAngle(gapPercentage)
    var center = Offset(0f, 0f)

    val textMeasurer = rememberTextMeasurer()
    val textMeasureResults: List<TextLayoutResult> = data.items.map { item ->
        val index = data.items.indexOf(item)
        val animatedPercentage = animatedPercentages[index].value

        if (item.percentage < minDisplayPercentage) {
            textMeasurer.measure(text = "")
        } else {
            textMeasurer.measure(
                text = "${animatedPercentage.roundToInt()}%",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(chartSize)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            handleCanvasTap(
                                center = center,
                                tapOffset = tapOffset,
                                anglesList = anglesList,
                                currentSelectedIndex = selectedIndex,
                                currentStrokeValues = animationTargetState.map { it.value.stroke.toPx() },
                                onItemSelected = { index ->
                                    selectedIndex = index
                                    animationTargetState[index].value = DonutChartState(
                                        DonutChartState.State.Selected
                                    )
                                },
                                onItemDeselected = { index ->
                                    animationTargetState[index].value = DonutChartState(
                                        DonutChartState.State.Unselected
                                    )
                                },
                                onNoItemSelected = {
                                    selectedIndex = -1
                                }
                            )
                        }
                    )
                },
            onDraw = {
                val defaultStrokeWidth = STROKE_SIZE_UNSELECTED.toPx()
                center = this.center
                anglesList.clear()
                var lastAngle = 0f

                data.items.forEachIndexed { ind, item ->
                    val animatedPercentage = animatedPercentages[ind].value
                    // Calculate sweep angle using total percentage for smoother transitions
                    val sweepAngle = (animatedPercentage / totalPercentage) *
                        (360f - (data.items.size * gapAngle))

                    anglesList.add(DrawingAngles(lastAngle, sweepAngle))
                    val strokeWidth = animValues[ind].value.toPx()

                    // Draw the arc with the animated values
                    drawArc(
                        color = item.color(isDarkTheme),
                        startAngle = lastAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(defaultStrokeWidth / 2, defaultStrokeWidth / 2),
                        style = Stroke(strokeWidth, cap = StrokeCap.Butt),
                        size = Size(
                            size.width - defaultStrokeWidth,
                            size.height - defaultStrokeWidth
                        )
                    )

                    val colorInner =
                        Color(
                            ColorUtils.blendARGB(
                                item.color(isDarkTheme).toArgb(),
                                Color.Black.toArgb(),
                                0.1f
                            )
                        )

                    drawArc(
                        color = colorInner,
                        startAngle = lastAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(
                            x = (defaultStrokeWidth / 2) + (defaultStrokeWidth / 2),
                            y = (defaultStrokeWidth / 2) + (defaultStrokeWidth / 2)
                        ),
                        style = Stroke(strokeWidth / 4, cap = StrokeCap.Butt),
                        size = Size(
                            size.width - (defaultStrokeWidth * 2),
                            size.height - (defaultStrokeWidth * 2)
                        )
                    )

                    val textMeasureResult = textMeasureResults[ind]
                    val textSize = textMeasureResult.size
                    val textCenter = textSize.center
                    val angleInRadians = (lastAngle + sweepAngle / 2).degreeToRadian

                    drawText(
                        textLayoutResult = textMeasureResult,
                        color = Color.White,
                        topLeft = Offset(
                            x = -textCenter.x + center.x +
                                ((size.width - defaultStrokeWidth) / 2) * cos(angleInRadians),
                            y = -textCenter.y + center.y +
                                ((size.height - defaultStrokeWidth) / 2) * sin(angleInRadians)
                        )
                    )

                    lastAngle += sweepAngle + gapAngle
                }
            }
        )
        onSelectedIndexChange(selectedIndex)
    }
}

private fun handleCanvasTap(
    center: Offset,
    tapOffset: Offset,
    anglesList: List<DrawingAngles>,
    currentSelectedIndex: Int,
    currentStrokeValues: List<Float>,
    onItemSelected: (Int) -> Unit = {},
    onItemDeselected: (Int) -> Unit = {},
    onNoItemSelected: () -> Unit = {}
) {
    val normalized = tapOffset.findNormalizedPointFromTouch(center)
    val touchAngle =
        calculateTouchAngleAccordingToCanvas(center, normalized)
    val distance = findTouchDistanceFromCenter(center, normalized)

    var selectedIndex = -1
    var newDataTapped = false

    anglesList.forEachIndexed { ind, angle ->
        val stroke = currentStrokeValues[ind]
        if (angle.isInsideAngle(touchAngle)) {
            if (
                distance > (center.x - stroke) &&
                distance < (center.x)
            ) {
                // since it's a square center.x or center.y will be the same
                selectedIndex = ind
                newDataTapped = true
            }
        }
    }

    if (selectedIndex >= 0 && newDataTapped) {
        onItemSelected(selectedIndex)
    }
    if (currentSelectedIndex >= 0) {
        onItemDeselected(currentSelectedIndex)
        if (currentSelectedIndex == selectedIndex || !newDataTapped) {
            onNoItemSelected()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DonutChartPreview() {
    DonutChart(
        data = DonutChartDataCollection(
            items = listOf(
                QueryTypeChartData(percentage = 20f, title = "ABC"),
                QueryTypeChartData(percentage = 10f, title = "A"),
                QueryTypeChartData(percentage = 20f, title = "B"),
                QueryTypeChartData(percentage = 50f, title = "C")
            )
        ),
        onSelectedIndexChange = {}
    )
}

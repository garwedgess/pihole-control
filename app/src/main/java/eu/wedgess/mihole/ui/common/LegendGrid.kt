package eu.wedgess.mihole.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.mihole.ui.theme.MiHoleTheme

data class LegendData(
    val title: String,
    val subTitle: String? = null,
    val color: Color,
    val isSelected: Boolean = false
)

@Composable
private fun LegendGrid(
    modifier: Modifier,
    title: @Composable (titleText: String) -> Unit,
    subTitle: @Composable (subTitleText: String) -> Unit,
    shape: @Composable (color: Color) -> Unit,
    itemsPerRow: Int = 2,
    items: List<LegendData>
) {
    val titleItems = @Composable { items.forEach { title(it.title) } }
    val subTitleItems = @Composable { items.forEach { subTitle(it.subTitle ?: "") } }
    val shapeItems = @Composable { items.forEach { shape(it.color) } }

    val rowCount = remember(items, itemsPerRow) {
        (items.size / itemsPerRow).plus(if (items.size % itemsPerRow != 0) 1 else 0)
    }

    val xAxisShapeSpacing =
        with(LocalDensity.current) { MiHoleTheme.dimens.padding.itemContent.roundToPx() }
    val yAxisSpacing =
        with(LocalDensity.current) { MiHoleTheme.dimens.padding.itemContentSmall.roundToPx() }

    Layout(
        contents = listOf(titleItems, subTitleItems, shapeItems),
        modifier = modifier
    ) { (titleMeasurables, subTitleMeasurables, colorMeasurables), constraints ->

        val columnWidth = constraints.maxWidth / itemsPerRow

        val colorPlaceables = colorMeasurables.map {
            it.measure(constraints)
        }

        val titlePlaceables = titleMeasurables.mapIndexed { index, measurable ->
            measurable.measure(
                constraints.copy(
                    maxWidth = if (index % itemsPerRow == 0) columnWidth.minus(
                        colorPlaceables.first().width.times(2)
                    ) else columnWidth
                )
            )
        }

        val subTitlePlaceables = subTitleMeasurables.mapIndexed { index, measurable ->
            measurable.measure(
                constraints.copy(
                    maxWidth = if (index % itemsPerRow == 0) columnWidth.minus(
                        colorPlaceables.first().width.times(2)
                    ) else columnWidth
                )
            )
        }


        val totalHeight =
            titlePlaceables.maxOf { it.height }
                .plus(subTitlePlaceables.maxOf { it.height }
                    .plus(yAxisSpacing)).times(rowCount)
        val lastColumnLongestWidth =
            titlePlaceables.filterIndexed { index, placeable -> index % 2 != 0 }.plus(
                subTitlePlaceables.filterIndexed { index, placeable -> index % 2 != 0 }
            ).maxBy { it.width }.width


        layout(constraints.maxWidth, totalHeight) {

            var yPosition = 0
            var xPosition = 0

            colorPlaceables.forEachIndexed { index, placeable ->
                val currentRow = index / itemsPerRow
                val currentColumn = (index % itemsPerRow)

                val columnWidth = constraints.maxWidth / itemsPerRow

                xPosition = if (currentColumn == 1) {
                    constraints.maxWidth.minus(
                        lastColumnLongestWidth.plus(xAxisShapeSpacing).plus(placeable.width)
                    )
                } else {
                    columnWidth.times(currentColumn)
                }
                yPosition = if (subTitlePlaceables[index].width == 0) {
                    totalHeight.div(rowCount).times(currentRow)
                        .plus(placeable.height.plus(yAxisSpacing))
                } else {
                    totalHeight.div(rowCount).times(currentRow)
                        .plus(placeable.height.plus(yAxisSpacing.div(2)))
                }
                placeable.placeRelative(xPosition, yPosition)
            }

            titlePlaceables.forEachIndexed { index, placeable ->
                val currentRow = index / itemsPerRow
                val currentColumn = (index % itemsPerRow)

                val columnWidth = constraints.maxWidth / itemsPerRow

                xPosition = if (currentColumn == 1) {
                    constraints.maxWidth.minus(lastColumnLongestWidth)
                } else {
                    columnWidth.times(currentColumn)
                        .plus(colorPlaceables.first().width.plus(xAxisShapeSpacing))
                }


                yPosition = if (subTitlePlaceables[index].width == 0) {
                    totalHeight.div(rowCount).times(currentRow)
                        .plus(colorPlaceables.first().height.plus(yAxisSpacing.div(2)))
                } else {
                    totalHeight.div(rowCount).times(currentRow).plus(yAxisSpacing)
                }
                placeable.placeRelative(xPosition, yPosition)
            }

            subTitlePlaceables.forEachIndexed { index, placeable ->
                val currentRow = index / itemsPerRow
                val currentColumn = (index % itemsPerRow)

                val columnWidth = constraints.maxWidth / itemsPerRow

                xPosition = if (currentColumn == 1) {
                    constraints.maxWidth.minus(lastColumnLongestWidth)
                } else {
                    columnWidth.times(currentColumn)
                        .plus(colorPlaceables.first().width.plus(xAxisShapeSpacing))
                }

                yPosition = totalHeight.div(rowCount).times(currentRow)
                    .plus(titlePlaceables.first().height).plus(yAxisSpacing.div(2))
                placeable.placeRelative(xPosition, yPosition)
            }

        }
    }
}

@Composable
fun LegendGridImpl(legendData: List<LegendData>, modifier: Modifier = Modifier) {
    val selectedItem = remember(legendData) {
        legendData.find { it.isSelected }
    }

    val defaultTitleText = MaterialTheme.typography.bodySmall.copy(
        fontWeight = FontWeight.Bold,
        fontSize = MiHoleTheme.dimens.fontSize.legendTitle,
        color = MaterialTheme.colorScheme.onSurface
    )

    val selectedTitleText = MaterialTheme.typography.bodySmall.copy(
        fontWeight = FontWeight.Bold,
        fontSize = MiHoleTheme.dimens.fontSize.legendTitleSelected,
        color = selectedItem?.color ?: MaterialTheme.colorScheme.onSurface
    )

    LegendGrid(
        modifier = modifier,
        title = { title ->
            Text(
                text = title,
                style = if (selectedItem?.title == title) selectedTitleText else defaultTitleText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        subTitle = {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = MiHoleTheme.dimens.fontSize.legendSubTitle,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = MiHoleTheme.dimens.weight.secondaryTextAlpha)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        shape = {
            Box(
                modifier = Modifier
                    .size(MiHoleTheme.dimens.size.legendIcon)
                    .clip(CircleShape)
                    .background(it)
            )
        },
        items = legendData
    )
}

@Preview
@Composable
private fun LegendGridPreview() {
    MiHoleTheme {
        Surface {
            LegendGrid(
                modifier = Modifier.padding(horizontal = MiHoleTheme.dimens.padding.itemContentSmall),
                title = {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = MiHoleTheme.dimens.fontSize.legendTitle,
                            color = Color.Black
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                },
                subTitle = {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = MiHoleTheme.dimens.fontSize.legendSubTitle,
                            color = Color.Black
                        )
                    )
                },
                shape = {
                    Box(
                        modifier = Modifier
                            .size(MiHoleTheme.dimens.size.legendIcon)
                            .clip(CircleShape)
                            .background(it)
                    )
                },
                items = listOf(
                    LegendData(
                        title = "Title1 that is really long and does not fit",
                        subTitle = "Subtitle1 long",
                        color = Color.Red
                    ),
                    LegendData(
                        title = "Title2",
                        subTitle = "Subtitle2 longest",
                        color = Color.Green
                    ),
                    LegendData(title = "Title3", subTitle = "Subtitle3", color = Color.Blue),
                    LegendData(title = "Title4", color = Color.Yellow),
                    LegendData(title = "Title5", subTitle = "Subtitle5", color = Color.Cyan)
                )
            )
        }
    }
}
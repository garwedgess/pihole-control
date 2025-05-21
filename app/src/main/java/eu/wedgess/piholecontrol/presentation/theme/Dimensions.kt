package eu.wedgess.piholecontrol.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

interface Dimensions {
    val padding: Padding
    val size: Size
    val weight: Weight
    val fontSize: FontSize
}

class Padding(
    val screenContent: Dp,
    val dialogContent: Dp,
    val itemContent: Dp,
    val itemContentSmall: Dp,
    val itemContentXSmall: Dp,
    val itemContentXXSmall: Dp,
    val itemContentLarge: Dp,
    val itemContentXLarge: Dp
)

class Size(
    val pieChart: Dp,
    val lineChartHeight: Dp,
    val legendIcon: Dp,
    val cornerRadiusLarge: Dp,
    val cornerRadius: Dp,
    val radioGroupSelectedIcon: Dp,
    val summaryIcon: Dp,
    val defaultIcon: Dp,
    val dialogTonalElevation: Dp,
    val logsStickyHeaderHeight: Dp,
    val topBarHeight: Dp,
    val bottomNavHeight: Dp,
    val statusBarHeight: Dp,
    val timeButtonWidth: Dp,
    val listPercentageBarWidth: Dp,
    val statisticsTitleIcon: Dp,
    val timeInputWidth: Dp
) {
    fun logsBottomSheetHeight(screenHeight: Dp, statusBarHeightDp: Dp): Dp =
        screenHeight.minus(
            logsStickyHeaderHeight
                .times(2)
                .plus(topBarHeight)
                .plus(bottomNavHeight)
                .plus(statusBarHeightDp)
        )
}

class Weight(
    val radioGroupUnSelectedBorderAlpha: Float,
    val minAlpha: Float,
    val full: Float,
    val point8: Float,
    val point2: Float,
    val half: Float,
    val bottomNavUnselectedAlpha: Float,
    val secondaryTextAlpha: Float,
    val tertiaryTextAlpha: Float,
    val disabledTextAlpha: Float
)

class FontSize(
    val legendTitle: TextUnit,
    val legendSubTitle: TextUnit
)

class AppDimens : Dimensions {
    override val padding: Padding =
        Padding(
            screenContent = 16.dp,
            dialogContent = 24.dp,
            itemContent = 12.dp,
            itemContentSmall = 8.dp,
            itemContentXSmall = 4.dp,
            itemContentXXSmall = 2.dp,
            itemContentLarge = 16.dp,
            itemContentXLarge = 24.dp
        )
    override val size: Size
        get() = Size(
            pieChart = 250.dp,
            lineChartHeight = 300.dp,
            legendIcon = 16.dp,
            cornerRadiusLarge = 28.dp,
            cornerRadius = 16.dp,
            radioGroupSelectedIcon = 16.dp,
            summaryIcon = 52.dp,
            dialogTonalElevation = 6.dp,
            logsStickyHeaderHeight = 44.dp,
            topBarHeight = 64.dp,
            bottomNavHeight = 80.dp,
            statusBarHeight = 32.dp,
            timeButtonWidth = 164.dp,
            listPercentageBarWidth = 100.dp,
            statisticsTitleIcon = 28.dp,
            timeInputWidth = 84.dp,
            defaultIcon = 24.dp
        )
    override val weight: Weight
        get() = Weight(
            radioGroupUnSelectedBorderAlpha = 0.75f,
            minAlpha = 0.1f,
            secondaryTextAlpha = 0.8f,
            full = 1f,
            half = 0.5f,
            point8 = 0.8f,
            point2 = 0.2f,
            bottomNavUnselectedAlpha = 0.6f,
            disabledTextAlpha = 0.4f,
            tertiaryTextAlpha = 0.6f
        )
    override val fontSize: FontSize
        get() = FontSize(
            legendTitle = 16.sp,
            legendSubTitle = 14.sp
        )
}

val appDimens = AppDimens()

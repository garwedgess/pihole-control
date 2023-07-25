package eu.wedgess.mihole.ui.theme

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
    val none: Dp,
    val screenContent: Dp,
    val dialogContent: Dp,
    val searchBarIconPaddingStart: Dp,
    val itemContent: Dp,
    val itemContentSmall: Dp,
    val itemContentLarge: Dp,
    val itemContentXLarge: Dp,
)

class Size(
    val pieChart: Dp,
    val searchBarHeight: Dp,
    val searchProgress: Dp,
    val legendIcon: Dp,
    val cornerRadius: Dp,
    val radioGroupSelectedIcon: Dp,
    val summaryIcon: Dp,
    val dialogTonalElevation: Dp,
    val timeDialogButtonRowHeight: Dp,
    val logsStickyHeaderHeight: Dp,
    val topBarHeight: Dp,
    val bottomNavHeight: Dp,
    val statusBarHeight: Dp,
    val timeButtonWidth: Dp,
    val listPercentageBarWidth: Dp,
    val statisticsTitleIcon: Dp,
    val timeInputWidth: Dp,
) {
    fun logsBottomSheetHeight(screenHeight: Dp): Dp =
        screenHeight.minus(logsStickyHeaderHeight.plus(topBarHeight).plus(bottomNavHeight.plus(statusBarHeight)))
}

class Weight(
    val searchTextField: Float,
    val searchHintAlpha: Float,
    val radioGroupUnSelectedBorderAlpha: Float,
    val minAlpha: Float,
    val full: Float,
    val half: Float,
    val bottomNavUnselectedAlpha: Float,
    val secondaryTextAlpha: Float,
    val disabledTextAlpha: Float,
)

class FontSize(
    val legendTitleSelected: TextUnit,
    val legendTitle: TextUnit,
    val legendSubTitle: TextUnit,
)

class AppDimens: Dimensions {
    override val padding: Padding =
        Padding(
            none = 0.dp,
            screenContent = 12.dp,
            dialogContent = 24.dp,
            itemContent = 8.dp,
            itemContentSmall = 4.dp,
            itemContentLarge = 16.dp,
            itemContentXLarge = 24.dp,
            searchBarIconPaddingStart = 2.dp
        )
    override val size: Size
        get() = Size(
            pieChart = 250.dp,
            searchBarHeight = 56.dp,
            searchProgress = 36.dp,
            legendIcon = 16.dp,
            cornerRadius = 16.dp,
            radioGroupSelectedIcon = 16.dp,
            summaryIcon = 52.dp,
            dialogTonalElevation = 6.dp,
            timeDialogButtonRowHeight = 40.dp,
            logsStickyHeaderHeight = 44.dp,
            topBarHeight = 80.dp,
            bottomNavHeight = 80.dp,
            statusBarHeight = 32.dp,
            timeButtonWidth = 164.dp,
            listPercentageBarWidth = 100.dp,
            statisticsTitleIcon = 32.dp,
            timeInputWidth = 84.dp
        )
    override val weight: Weight
        get() = Weight(
            searchTextField = 1f,
            searchHintAlpha = 0.6f,
            radioGroupUnSelectedBorderAlpha = 0.75f,
            minAlpha = 0.1f,
            secondaryTextAlpha = 0.8f,
            full = 1f,
            half = 0.5f,
            bottomNavUnselectedAlpha = 0.6f,
            disabledTextAlpha = 0.4f
        )
    override val fontSize: FontSize
        get() = FontSize(
            legendTitleSelected = 18.sp,
            legendTitle = 16.sp,
            legendSubTitle = 14.sp
        )
}

val appDimens = AppDimens()
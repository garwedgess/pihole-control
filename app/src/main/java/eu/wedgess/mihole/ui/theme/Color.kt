package eu.wedgess.mihole.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val DarkTotalQueriesBackground = Color(0xFF005C32)
val DarkQueriesBlockedBackground = Color(0xFF007997)
val DarkPercentageBlockedBackground = Color(0xFFB1720C)
val DarkDomainsOnAdListBackground = Color(0xFF913225)

val LightTotalQueriesBackground = Color(0xFF00A65A)
val LightQueriesBlockedBackground = Color(0xFF00C0EF)
val LightPercentageBlockedBackground = Color(0xFFF39C12)
val LightDomainsOnAdListBackground = Color(0xFFDD4B39)

val ColorScheme.totalQueriesBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) DarkTotalQueriesBackground else LightTotalQueriesBackground

val ColorScheme.queriesBlockedBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) DarkQueriesBlockedBackground else LightQueriesBlockedBackground

val ColorScheme.percentageBlockedBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) DarkPercentageBlockedBackground else LightPercentageBlockedBackground

val ColorScheme.domainsOnAdListBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) DarkDomainsOnAdListBackground else LightDomainsOnAdListBackground

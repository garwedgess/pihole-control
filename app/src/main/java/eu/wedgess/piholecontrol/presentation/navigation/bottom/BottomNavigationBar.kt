package eu.wedgess.piholecontrol.presentation.navigation.bottom

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun BottomNavigationBar(
    onNavigateTo: (route: Screens) -> Unit,
    selectedItemRoute: String? = null
) {
    NavigationBar {
        BottomNavItem.all().forEach { item ->
            val selected = item.route == (selectedItemRoute ?: Screens.Dashboard)

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateTo(item.route) },
                label = { Text(text = item.title.asString()) },
                icon = { Icon(item.icon, contentDescription = "") },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = PiHoleControlTheme.dimens.weight.bottomNavUnselectedAlpha
                    ),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = PiHoleControlTheme.dimens.weight.bottomNavUnselectedAlpha
                    ),
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                    selectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

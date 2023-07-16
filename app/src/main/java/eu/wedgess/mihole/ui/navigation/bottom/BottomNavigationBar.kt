package eu.wedgess.mihole.ui.navigation.bottom

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun BottomNavigationBar(
    onNavigateTo: (route: String) -> Unit,
    selectedItemRoute: String? = null
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        BottomNavItem.all().forEach { item ->
            val selected = item.route == (selectedItemRoute ?: Screens.Dashboard.route)

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateTo(item.route) },
                label = { Text(text = item.title.asString()) },
                icon = { Icon(item.icon, contentDescription = "") },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = MiHoleTheme.dimens.weight.bottomNavUnselectedAlpha),
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = MiHoleTheme.dimens.weight.bottomNavUnselectedAlpha),
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                    selectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}
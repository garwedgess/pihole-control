package eu.wedgess.mihole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.common.MainAppBar
import eu.wedgess.mihole.ui.navigation.NavigationGraph
import eu.wedgess.mihole.ui.navigation.bottom.BottomNavigationBar
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@AndroidEntryPoint
class MiHoleEntryPointActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navHostController = rememberNavController()
            val backStackEntry = navHostController.currentBackStackEntryAsState()
            var appBarState by remember { mutableStateOf(AppBarState()) }

            MiHoleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            MainAppBar(
                                appBarState = appBarState,
                                onNavigateBack = { navHostController.navigateUp() })
                        },
                        bottomBar = {
                            BottomNavigationBar(
                                selectedItemRoute = backStackEntry.value?.destination?.route,
                                onNavigateTo = { route ->
                                    if (route != backStackEntry.value?.destination?.route) {
                                        navHostController.navigate(route)
                                    }
                                }
                            )
                        },
                        content = { contentPadding ->
                            NavigationGraph(
                                modifier = Modifier.padding(contentPadding),
                                navController = navHostController,
                                onComposing = { appBarState = it })
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MiHoleTheme {
        Greeting("Android")
    }
}
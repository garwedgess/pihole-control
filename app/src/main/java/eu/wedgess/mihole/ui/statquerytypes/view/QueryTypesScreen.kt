package eu.wedgess.mihole.ui.statquerytypes.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.data.model.responses.QueryTypes
import eu.wedgess.mihole.ui.compose.Compose
import eu.wedgess.mihole.ui.compose.ErrorScreen
import eu.wedgess.mihole.ui.compose.LoadingScreen
import eu.wedgess.mihole.ui.compose.UIResult

@Composable
fun QueryTypesScreen(uiResult: UIResult<QueryTypes>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        uiResult.Compose(
            onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
            onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) },
            onLoaded = { QueryTypesContent(it) }
        )
    }
}
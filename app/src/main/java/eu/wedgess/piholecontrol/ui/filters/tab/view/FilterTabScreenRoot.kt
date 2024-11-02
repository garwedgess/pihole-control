package eu.wedgess.piholecontrol.ui.filters.tab.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRules
import eu.wedgess.piholecontrol.di.FilterTabViewModelFactory
import eu.wedgess.piholecontrol.ui.compose.Compose
import eu.wedgess.piholecontrol.ui.compose.EmptyScreen
import eu.wedgess.piholecontrol.ui.compose.ErrorScreen
import eu.wedgess.piholecontrol.ui.compose.LoadingScreen
import eu.wedgess.piholecontrol.ui.filters.model.FilterScreenTabType
import eu.wedgess.piholecontrol.ui.filters.tab.viewmodel.FilterTabViewModel

@Composable
fun FilterTabScreenRoot(
    filterScreenTabType: FilterScreenTabType,
    onFilterRuleClick: (PiHoleFilterRules.PiHoleFilterRule) -> Unit,
    onRefreshFilters: (() -> Unit) -> Unit,
    searchQuery: String? = null
) {
    val viewModel: FilterTabViewModel = hiltViewModel(
        key = "FilterTabViewModel-${filterScreenTabType.name}",
        creationCallback = { factory: FilterTabViewModelFactory ->
            factory.create(filterScreenTabType)
        }
    )

    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

    LaunchedEffect(searchQuery) {
        viewModel.setSearchQuery(searchQuery)
    }

    LaunchedEffect(Unit) {
        onRefreshFilters {
            viewModel.onRefreshData()
        }
    }

    uiResult.Compose(
        onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
        onEmpty = { EmptyScreen(modifier = Modifier.fillMaxSize(), it) },
        onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) },
        onLoaded = {
            FilterListContent(filtersList = it.filterRules, onFilterRuleClick = onFilterRuleClick)
        }
    )

}
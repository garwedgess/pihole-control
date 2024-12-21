package eu.wedgess.piholecontrol.presentation.filters.tab.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.piholecontrol.di.FilterTabViewModelFactory
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.EmptyScreen
import eu.wedgess.piholecontrol.presentation.compose.ErrorScreen
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.filters.model.FilterScreenTabType
import eu.wedgess.piholecontrol.presentation.filters.tab.FilterTabContract
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo
import eu.wedgess.piholecontrol.presentation.filters.tab.viewmodel.FilterTabViewModel
import eu.wedgess.piholecontrol.utils.UiText

@Composable
fun FilterTabScreenRoot(
    filterScreenTabType: FilterScreenTabType,
    onFilterRuleClick: (FilterRuleInfo) -> Unit,
    onRefreshFilters: (() -> Unit) -> Unit,
    searchQuery: String? = null,
    showSnackBarText: (UiText) -> Unit
) {
    val context = LocalContext.current
    val viewModel: FilterTabViewModel = hiltViewModel(
        key = "FilterTabViewModel-${filterScreenTabType.name}",
        creationCallback = { factory: FilterTabViewModelFactory ->
            factory.create(filterScreenTabType)
        }
    )

    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()
    val sideEffect = viewModel.sideEffect

    LaunchedEffect(searchQuery) {
        viewModel.setSearchQuery(searchQuery)
    }

    LaunchedEffect(Unit) {
        onRefreshFilters {
            viewModel.onRefreshData()
        }
    }

    CollectSideEffect(sideEffect) { effect ->
        if (effect is FilterTabContract.Effect.ShowErrorSnackbar) {
            showSnackBarText(
                UiText.DynamicString(
                    effect.errorMessages.joinToString(separator = "\n") {
                        it.asString(context)
                    }
                )
            )
        }
    }

    uiResult.Compose(
        onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
        onEmpty = { EmptyScreen(modifier = Modifier.fillMaxSize(), it) },
        onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) },
        onLoaded = {
            FilterListContent(
                filtersList = it.filterRules,
                onFilterRuleClick = onFilterRuleClick
            )
        }
    )
}

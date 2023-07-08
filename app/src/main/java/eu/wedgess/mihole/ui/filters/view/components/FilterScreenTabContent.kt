package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.ui.base.Resource
import eu.wedgess.mihole.ui.common.ErrorMessage
import eu.wedgess.mihole.ui.common.LoadingContent
import eu.wedgess.mihole.ui.common.search.SearchState
import eu.wedgess.mihole.ui.filters.FiltersContract

@Composable
fun FilterScreenTabContent(
    filterList: Resource<List<PiHoleFilterRules.PiHoleFilterRule>>,
    searchState: SearchState<PiHoleFilterRules.PiHoleFilterRule>,
    onEvent: (FiltersContract.Event) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (filterList) {
            is Resource.Loading -> LoadingContent(message = "Loading filters")
            is Resource.Error -> ErrorMessage(errorMessage = filterList.errorMessage, onRetry = {})
            is Resource.Success -> FilterListContent(filtersList = filterList.data, searchState, onEvent)
        }
    }
}
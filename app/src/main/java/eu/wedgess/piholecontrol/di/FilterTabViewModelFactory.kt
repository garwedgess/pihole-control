package eu.wedgess.piholecontrol.di

import dagger.assisted.AssistedFactory
import eu.wedgess.piholecontrol.presentation.filters.tab.viewmodel.FilterTabViewModel
import eu.wedgess.piholecontrol.presentation.navigation.tabs.FilterTab

@AssistedFactory
interface FilterTabViewModelFactory {
    fun create(filterRuleType: FilterTab): FilterTabViewModel
}

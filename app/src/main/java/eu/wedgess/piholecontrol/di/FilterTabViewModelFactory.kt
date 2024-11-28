package eu.wedgess.piholecontrol.di

import dagger.assisted.AssistedFactory
import eu.wedgess.piholecontrol.presentation.filters.model.FilterScreenTabType
import eu.wedgess.piholecontrol.presentation.filters.tab.viewmodel.FilterTabViewModel

@AssistedFactory
interface FilterTabViewModelFactory {
    fun create(filterRuleType: FilterScreenTabType): FilterTabViewModel
}
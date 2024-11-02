package eu.wedgess.piholecontrol.di

import dagger.assisted.AssistedFactory
import eu.wedgess.piholecontrol.ui.filters.model.FilterScreenTabType
import eu.wedgess.piholecontrol.ui.filters.tab.viewmodel.FilterTabViewModel

@AssistedFactory
interface FilterTabViewModelFactory {
    fun create(filterRuleType: FilterScreenTabType): FilterTabViewModel
}
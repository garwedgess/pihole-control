package eu.wedgess.mihole.di

import dagger.assisted.AssistedFactory
import eu.wedgess.mihole.ui.filters.model.FilterScreenTabType
import eu.wedgess.mihole.ui.filters.tab.viewmodel.FilterTabViewModel

@AssistedFactory
interface FilterTabViewModelFactory {
    fun create(filterRuleType: FilterScreenTabType): FilterTabViewModel
}
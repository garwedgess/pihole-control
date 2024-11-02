package eu.wedgess.piholecontrol.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.ui.filters.controller.FiltersController
import eu.wedgess.piholecontrol.ui.filters.controller.FiltersControllerImpl

@Module
@InstallIn(SingletonComponent::class)
interface ControllerModule {
    @Binds
    fun provideFiltersController(impl: FiltersControllerImpl): FiltersController
}
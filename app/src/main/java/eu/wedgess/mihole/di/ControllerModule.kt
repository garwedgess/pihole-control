package eu.wedgess.mihole.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.mihole.ui.filters.controller.FiltersController
import eu.wedgess.mihole.ui.filters.controller.FiltersControllerImpl

@Module
@InstallIn(SingletonComponent::class)
interface ControllerModule {
    @Binds
    fun provideFiltersController(impl: FiltersControllerImpl): FiltersController
}
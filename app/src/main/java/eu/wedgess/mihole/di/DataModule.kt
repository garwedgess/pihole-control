package eu.wedgess.mihole.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.data.MiHoleDatabase
import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.api.PiHoleApiImpl
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.utils.DefaultDispatchers
import eu.wedgess.mihole.utils.DispatcherProvider
import io.ktor.client.HttpClient

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideDispatcherProvider(): DispatcherProvider =
        DefaultDispatchers()

    @Provides
    fun provideDao(database: MiHoleDatabase) =
        MiHolesDao(database)

    @Provides
    fun provideApi(httpClient: HttpClient): PiHoleApi =
        PiHoleApiImpl(httpClient)

    @Provides
    fun provideRepository(api: PiHoleApi, dao: MiHolesDao, dispatcherProvider: DispatcherProvider) =
        PiHoleRepository(api, dao, dispatcherProvider)
}
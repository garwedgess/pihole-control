package eu.wedgess.mihole.di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.wedgess.mihole.MiHoles
import eu.wedgess.mihole.data.MiHoleDatabase
import eu.wedgess.mihole.data.utils.sqldelight.portAdapter
import eu.wedgess.mihole.data.utils.sqldelight.protocolAdapter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "miholes.db"

    @Singleton
    @Provides
    fun dbDriver(@ApplicationContext context: Context): AndroidSqliteDriver =
        AndroidSqliteDriver(MiHoleDatabase.Schema, context, DB_NAME)

    @Singleton
    @Provides
    fun provideDb(driver: AndroidSqliteDriver) = MiHoleDatabase(
        driver,
        MiHolesAdapter = MiHoles.Adapter(
            ProtocolAdapter = protocolAdapter,
            PortAdapter = portAdapter
        )
    )

}
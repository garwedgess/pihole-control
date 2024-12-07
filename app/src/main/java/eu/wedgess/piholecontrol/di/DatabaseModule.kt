package eu.wedgess.piholecontrol.di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import eu.wedgess.piholecontrol.data.utils.sqldelight.portAdapter
import eu.wedgess.piholecontrol.data.utils.sqldelight.protocolAdapter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "piholecontrol.db"

    @Singleton
    @Provides
    fun dbDriver(@ApplicationContext context: Context): AndroidSqliteDriver =
        AndroidSqliteDriver(PiHoleControlDatabase.Schema, context, DB_NAME)

    @Singleton
    @Provides
    fun provideDb(driver: AndroidSqliteDriver) = PiHoleControlDatabase(
        driver,
        ConnectionAdapter = Connection.Adapter(
            ProtocolAdapter = protocolAdapter,
            PortAdapter = portAdapter
        )
    )

}
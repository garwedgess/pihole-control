package eu.wedgess.piholecontrol.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.data.utils.serializers.UserPreferencesSerializer
import eu.wedgess.piholecontrol.utils.DefaultDispatchers
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import javax.inject.Singleton

private const val DATA_STORE_FILE_NAME = "user_prefs.pb"

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideDispatcherProvider(): DispatcherProvider =
        DefaultDispatchers()

    @Singleton
    @Provides
    fun provideDao(database: PiHoleControlDatabase, dispatcherProvider: DispatcherProvider) =
        ConnectionDao(database, dispatcherProvider)

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(@ApplicationContext context: Context): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME) },
            corruptionHandler = null
        )

    @Provides
    fun provideBarcodeScanner(): BarcodeScanner {
        val options =
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        return BarcodeScanning.getClient(options)
    }
}

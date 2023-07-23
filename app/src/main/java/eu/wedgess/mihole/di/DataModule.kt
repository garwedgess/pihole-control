package eu.wedgess.mihole.di

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
import eu.wedgess.mihole.data.MiHoleDatabase
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.data.api.PiHoleApi
import eu.wedgess.mihole.data.api.PiHoleApiImpl
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.data.utils.serializers.UserPreferencesSerializer
import eu.wedgess.mihole.di.annotations.DefaultHttpClient
import eu.wedgess.mihole.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.mihole.utils.DefaultDispatchers
import eu.wedgess.mihole.utils.DispatcherProvider
import io.ktor.client.HttpClient
import javax.inject.Singleton

private const val DATA_STORE_FILE_NAME = "user_prefs.pb"

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
    fun provideApi(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): PiHoleApi =
        PiHoleApiImpl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    fun provideRepository(
        api: PiHoleApi,
        dao: MiHolesDao,
        dataStore: DataStore<UserPreferences>,
        dispatcherProvider: DispatcherProvider
    ) =
        PiHoleRepository(api, dao, dataStore, dispatcherProvider)

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
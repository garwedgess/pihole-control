package eu.wedgess.piholecontrol.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TrustAllCertificatesHttpClient
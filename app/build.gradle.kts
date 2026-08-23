plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    id("android-jacoco-convention")
    id("android-spotless-convention")
    id("android-detekt-convention")
}

android {
    namespace = "eu.wedgess.piholecontrol"
    compileSdk = 37

    defaultConfig {
        applicationId = "eu.wedgess.piholecontrol"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }

        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

sqldelight {
    databases {
        create("PiHoleControlDatabase") {
            packageName.set("eu.wedgess.piholecontrol.data")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.animations)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.bundles.sqldelight)

    implementation(libs.timber)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.javalite)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.android.date.backport)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.robolectric)
    testImplementation(libs.core.ktx)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.splashscreen)
    implementation(libs.mlkit.barcode.scanning)

    implementation(libs.bundles.vico)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.camera)
    implementation(libs.logging.interceptor)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.29.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

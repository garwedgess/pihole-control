plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.tools.build.gradle)
    implementation(libs.detekt.gradle)
    implementation(libs.spotless.plugin.gradle)

    gradleApi()
}

gradlePlugin {
    plugins {
        register("detekt") {
            id = "android-detekt-convention"
            implementationClass = "eu.wedgess.detekt.DetektConventionPlugin"
        }
    }
}

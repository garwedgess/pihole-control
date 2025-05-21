plugins {
    `kotlin-dsl`
}

dependencies {
    api(libs.jetbrains.kotlin.gradle.plugin)
    api(libs.android.tools.build.gradle)
    gradleApi()
    compileOnly(libs.detekt.gradle)
    implementation(libs.spotless.plugin.gradle)
}

gradlePlugin {
    plugins {
        register("detekt") {
            id = "android-detekt-convention"
            implementationClass = "eu.wedgess.detekt.DetektConventionPlugin"
        }
    }
}

import org.apache.tools.ant.util.JavaEnvUtils.VERSION_1_8

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.aura"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aura"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        viewBinding = true
    }

    buildToolsVersion = "34.0.0"

}

dependencies {

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    implementation("androidx.test:runner:1.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("androidx.test.ext:junit:1.2.1")
    testImplementation ("junit:junit:4.13.2")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // Coroutines for asynchronous programming
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")

    // Retrofit + Moshi
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // mockk
    testImplementation("io.mockk:mockk:1.4.1")


    // JUnit for testing
    testImplementation("junit:junit:4.13.2")

    // Mockito for mocking dependencies
    testImplementation("org.mockito:mockito-core:3.12.4")

    // Mockito Kotlin for easier mocking of Kotlin classes
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")

    // MockWebServer
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    // Existing dependencies
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.5")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0")
    implementation("androidx.test:core:1.6.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.annotation:annotation:1.8.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")


}
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
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  buildFeatures {
    viewBinding = true
  }
  buildToolsVersion = "34.0.0"
}

dependencies {

  // Hilt
  implementation("com.google.dagger:hilt-android:2.51.1")
  kapt("com.google.dagger:hilt-android-compiler:2.51.1")

  // Retrofit + Moshi
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
  implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
  implementation("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

  // Existing dependencies
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")
  implementation("androidx.activity:activity-ktx:1.9.1")
  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
  implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
  implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.annotation:annotation:1.8.2")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.2.1")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
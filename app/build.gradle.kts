plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.myfitfriend"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myfitfriend"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }

    kotlinOptions {
        jvmTarget = "19"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.retrofit)
    implementation(libs.retrofitgsonconverter)
    implementation ("javax.inject:javax.inject:1")
    implementation("androidx.startup:startup-runtime:1.1.1")

    implementation ("com.google.dagger:hilt-android:2.48")
    kapt ("com.google.dagger:hilt-compiler:2.48")



    // For ViewModel injections with Hilt in Compose

    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation( "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation( "androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    // For Hilt with Jetpack Compose navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")



    implementation ("androidx.security:security-crypto:1.1.0-alpha02")

    implementation ("androidx.security:security-identity-credential:1.0.0-alpha02")
    implementation("androidx.navigation:navigation-compose:2.5.0")
    // AndroidX Navigation Fragment
    implementation ("androidx.navigation:navigation-fragment-ktx:2.3.5")
    // AndroidX Navigation UI
    implementation ("androidx.navigation:navigation-ui-ktx:2.3.5")

    implementation("androidx.compose.material:material:1.6.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    // Kotlin Coroutines Android library for Android specific functionalities
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
}
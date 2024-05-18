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
        minSdk = 26
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
        jvmTarget = "1.8"
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
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
    javacOptions {
        option("-Adagger.hilt.disableModulesHaveInstallInCheck=true")
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
    implementation("javax.inject:javax.inject:1")
    implementation("androidx.startup:startup-runtime:1.1.1")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")



    // Hilt dependencies
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    // For ViewModel injections with Hilt in Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    // For Hilt with Jetpack Compose navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("androidx.security:security-crypto:1.1.0-alpha02")
    implementation("androidx.security:security-identity-credential:1.0.0-alpha02")
    implementation("androidx.navigation:navigation-compose:2.5.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.0")

    implementation("androidx.compose.material:material:1.6.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    // ML Kit Barcode Scanning
    implementation("com.google.mlkit:barcode-scanning:17.0.3")

    // CameraX dependencies
    implementation("androidx.camera:camera-core:1.1.0-beta03")
    implementation("androidx.camera:camera-camera2:1.1.0-beta03")
    implementation("androidx.camera:camera-lifecycle:1.1.0-beta03")
    implementation("androidx.camera:camera-view:1.1.0-beta03")
}

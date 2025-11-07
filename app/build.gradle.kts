plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
//    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    namespace = "com.yhyzgn.tv.rubbish"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.yhyzgn.tv.rubbish"
        minSdk = 24
        targetSdk = 36
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
    // ... 启用 compose
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.6.0" }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.core.ktx.v1170)
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui:1.8.1")
    implementation("androidx.compose.material:material:1.8.1")
    implementation("androidx.compose.foundation:foundation:1.8.1")
    implementation("androidx.compose.animation:animation:1.8.1")
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("androidx.room:room-runtime:2.8.3")
    kapt("androidx.room:room-compiler:2.8.3")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation(libs.jsoup)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
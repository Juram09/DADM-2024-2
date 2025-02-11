plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.aiapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.aiapp"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx.v1101)
    implementation(libs.androidx.lifecycle.runtime.ktx.v261)
    implementation(libs.androidx.activity.compose.v172)
    implementation(libs.androidx.ui.v140)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.material.v140)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.v153)
    implementation(libs.androidx.ui.tooling.preview.v153)
    implementation(libs.androidx.material.v153)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.appcompat)
    implementation (libs.okhttp3.okhttp) // Para llamadas HTTP
    implementation (libs.gson)   // Para manejar JSON
    implementation (libs.androidx.recyclerview) // Para mostrar el chat
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.junit.junit)
}
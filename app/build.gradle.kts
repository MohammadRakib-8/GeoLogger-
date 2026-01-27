import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

// Load local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    namespace = "com.example.geologger"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.geologger"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        buildConfigField(
            "String",
            "WEATHER_API_KEY",
            "\"${localProperties["WEATHER_API_KEY"]}\""
        )
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // AndroidX Core & UI Components
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Coroutines for async operations
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // Room Database
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler) // Use the alias for the compiler
    implementation(libs.androidx.room.ktx)

    // Location Services
    implementation(libs.play.services.location)

    // Retrofit for API Integration
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson) // Explicitly add Gson

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}

//dependencies {
//
//        // Kotlin Standard Library (already included with the Kotlin plugin, but explicit is fine)
//        implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")
//
//        // AndroidX Core & UI Components (using version catalog)
//        implementation(libs.androidx.core.ktx)
//        implementation(libs.androidx.appcompat)
//        implementation(libs.material)
//        implementation(libs.androidx.activity)
//        implementation(libs.androidx.constraintlayout)
//
//        // Coroutines for async operations (using version catalog)
//        implementation(libs.kotlinx.coroutines.core)
//        implementation(libs.kotlinx.coroutines.android)
//
//        // RecyclerView (using version catalog)
//        implementation(libs.androidx.recyclerview)
//
//        // Room Database (using version catalog)
//        implementation(libs.androidx.room.runtime)
//        kapt("androidx.room:room-compiler:2.6.1") // Kapt dependency needs the version explicitly or its own catalog entry
//        implementation(libs.androidx.room.ktx)
//
//        // Location Services (using version catalog)
//        implementation(libs.play.services.location)
//
//        // Retrofit for API Integration (using version catalog)
//        implementation(libs.retrofit)
//        implementation(libs.converter.gson)
//        implementation(libs.gson) // Explicitly add Gson
//
//        // Lifecycle (using version catalog)
//        implementation(libs.androidx.lifecycle.runtime.ktx)
//        implementation(libs.androidx.lifecycle.viewmodel.ktx)
//
//        // Testing (using version catalog)
//        testImplementation(libs.junit)
//        androidTestImplementation(libs.androidx.junit)
//        androidTestImplementation(libs.androidx.espresso.core)
//
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.activity)
//    implementation(libs.androidx.constraintlayout)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//}
//

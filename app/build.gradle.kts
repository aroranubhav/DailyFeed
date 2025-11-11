import java.util.Base64
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.maxi.dailyfeed"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.maxi.dailyfeed"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val properties = Properties().apply {
        rootProject.file("local.properties").takeIf { it.exists() }?.reader()?.use(::load)
    }

    val apiKey = properties["API_KEY"] ?: error("Error: API KEY not found!")
    val encodedKey = Base64.getEncoder().encodeToString(apiKey.toString().toByteArray())

    buildTypes {
        all {
            buildConfigField("String", "API_KEY", "\"$encodedKey\"")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //swipe refresh layout
    implementation(libs.androidx.swipe.refresh)

    //lifecycle
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)

    //room
    implementation(libs.room.ktx)
    implementation(libs.room)
    ksp(libs.room.compiler)

    //interceptor
    implementation(libs.logging.interceptor)

    //browser
    implementation(libs.browser)

    //glide
    implementation(libs.glide)

    //worker + hilt-work
    implementation(libs.work.runtime)
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)

    //datastore
    implementation(libs.data.store)

    //paging
    implementation(libs.androidx.paging)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
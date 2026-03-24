import java.util.Base64
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.maxi.dailyfeed.data"
    compileSdk = 36
    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    val properties = Properties().apply {
        rootProject.file("local.properties")
            .takeIf {
                it.exists()
            }?.reader()
            .use {
                load(it)
            }
    }

    val apiKey = properties["API_KEY"] ?: error("Missing API Key")
    val encodedApiKey = Base64.getEncoder().encodeToString(apiKey.toString().toByteArray())
    val userAgent = properties["USER_AGENT"] ?: error("Missing User Agent")

    buildTypes {
        all {
            buildConfigField("String", "API_KEY", "\"$encodedApiKey\"")
            buildConfigField("String", "USER_AGENT", "\"$userAgent\"")
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
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //retrofit
    implementation(libs.retrofit)

    //kotlinx-serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.converter)

    //room
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //okhttp-logging
    implementation(libs.logging.interceptor)

    //datastore
    implementation(libs.androidx.datastore)
}
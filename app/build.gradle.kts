plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "uz.gka.tapyoutest"
    compileSdk = 35

    defaultConfig {
        applicationId = "uz.gka.tapyoutest"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    viewBinding {
        enable = true
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://hr-challenge.dev.tapyou.com/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://hr-challenge.dev.tapyou.com/\"")

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.kotlinx.serialization.json)

    // Moxy and Cicerone for MVP and navigation
    implementation(libs.moxy)
    implementation(libs.moxy.ktx)
    kapt(libs.moxy.compiler)
    implementation(libs.cicerone)

    // Retrofit и Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.retrofit.adapter.rxjava2)

    // RxJava
    implementation(libs.rxjava2)
    implementation(libs.rxandroid)

    // MPAndroidChart
    implementation(libs.mpandroidchart)

    // Dagger 2
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.rxjava2)
    kapt(libs.room.compiler)
}


kapt {
    correctErrorTypes = true
}
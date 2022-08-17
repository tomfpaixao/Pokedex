plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
}

android {
    compileSdk = Configs.compileSdkVersion
    buildToolsVersion = Configs.buildToolVersion

    defaultConfig {
        applicationId = Configs.applicationId
        minSdk = Configs.minSdkVersion
        targetSdk =  Configs.targetSdkVersion
        versionCode = Configs.versionCode + 1
        versionName = Configs.versionName
        multiDexEnabled = true

        testInstrumentationRunner = Configs.testInstrumentationRunner
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
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
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kapt {
        generateStubs = true
    }
}

dependencies {
// Kotlin
    implementation(Dependencies.Kotlin.kotlin)
    implementation(Dependencies.Kotlin.Coroutines.android)

// AndroidX
    implementation(Dependencies.AndroidX.Core.core)
    implementation(Dependencies.AndroidX.SplashScreen.splashscreen)
    implementation(Dependencies.AndroidX.AppCompat.appcompat)
    implementation(Dependencies.AndroidX.ConstraintLayout.constraintLayout)
    implementation(Dependencies.AndroidX.Fragment.fragment)
    implementation(Dependencies.AndroidX.Lifecycle.viewModel)
    implementation(Dependencies.AndroidX.Lifecycle.liveData)
    implementation(Dependencies.AndroidX.Lifecycle.runtime)
    implementation(Dependencies.AndroidX.RecyclerView.recyclerview)
    implementation(Dependencies.AndroidX.Navigation.fragment)
    implementation(Dependencies.AndroidX.Navigation.ui)
    implementation(Dependencies.AndroidX.Paging3.paging3)
    implementation(Dependencies.AndroidX.Paging3.pagingCompose)

// Material
    implementation(Dependencies.Material.material)

// Dagger-Hilt
    implementation(Dependencies.Dagger.hiltAndroid)
    implementation("androidx.compose.ui:ui:1.2.0")
    implementation("androidx.compose.material:material:1.2.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.0")
    implementation("androidx.navigation:navigation-compose:2.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.0")
    kapt(Dependencies.Dagger.hiltAndroidCompiler)
    implementation(Dependencies.AndroidX.Hilt.hiltNavigationCompose)

// Networking
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.OkHttp.okhttp)
    implementation(Dependencies.OkHttp.loggingInterceptor)

// Moshi
    implementation(Dependencies.Moshi.moshi)
    implementation(Dependencies.Retrofit.moshiConverter)
    kapt(Dependencies.Moshi.moshiCodegen)

// Image Loading
    implementation(Dependencies.Coil.coil)
    implementation(Dependencies.Coil.coilCompose)

    //Lottie
    implementation(Dependencies.Lottie.lottie)

    //Room
    implementation(Dependencies.AndroidX.Room.roomKtx)
    implementation(Dependencies.AndroidX.Room.runtime)
    implementation(Dependencies.AndroidX.Room.roomPaging)
    kapt(Dependencies.AndroidX.Room.compiler)

// Test
    testImplementation(Dependencies.Test.Junit.junit)
    androidTestImplementation(Dependencies.Test.Ext.ext)
    androidTestImplementation(Dependencies.Test.Espresso.espresso)

}
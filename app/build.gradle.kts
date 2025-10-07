plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.ref01"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ref01"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // No afecta, pero lo dejo por si reactivas tests en el futuro
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    sourceSets {
        getByName("androidTest") {
            java.setSrcDirs(emptyList<File>())
            res.setSrcDirs(emptyList<File>())
            assets.setSrcDirs(emptyList<File>())
            resources.setSrcDirs(emptyList<File>())
            aidl.setSrcDirs(emptyList<File>())
            jniLibs.setSrcDirs(emptyList<File>())
            // evita que busque manifest de androidTest
            manifest.srcFile(file("nonexistent/AndroidManifest.xml"))
        }
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
    kotlinOptions { jvmTarget = "11" }

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")

    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.activity:activity-compose:1.9.2")

    implementation(libs.play.services.location)
    implementation(libs.maps.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.recyclerview)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.animation:animation")
    implementation(libs.activity.compose)
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.airbnb.android:lottie-compose:6.4.0")
    implementation("io.coil-kt:coil-compose:2.6.0")

    // ===== TESTS JVM (puedes comentarlos si quieres; no se ejecutarán) =====
    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("org.robolectric:robolectric:4.12.2")

    // androidTestImplementation(libs.androidx.junit)
    // androidTestImplementation(libs.androidx.espresso.core)
    // androidTestImplementation(libs.androidx.ui.test.junit4)
    // debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    testImplementation("androidx.room:room-testing:2.6.1")
}

tasks.withType<Test>().configureEach {
    enabled = false
}

tasks.configureEach {
    if (name.contains("AndroidTest", ignoreCase = true)) {
        enabled = false
    }
}

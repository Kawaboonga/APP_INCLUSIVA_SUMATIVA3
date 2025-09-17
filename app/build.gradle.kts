plugins {


    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

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
}

dependencies {

    implementation("io.coil-kt:coil-compose:2.6.0")

    // Compose BOM (alinea todas las libs de Compose)
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))

    // Compose core
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Material 3
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")


    // Activity + Compose
    implementation("androidx.activity:activity-compose:1.9.0")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Lottie Compose
    implementation("com.airbnb.android:lottie-compose:6.4.0")

    // AndroidX comunes
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")

    // Tests (opcionales)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

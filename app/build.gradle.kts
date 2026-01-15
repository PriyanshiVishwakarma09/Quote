plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.quote"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quote"
        minSdk = 24
        targetSdk = 35
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
        isCoreLibraryDesugaringEnabled = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(platform(libs.supabase.bom))

    // The actual features you want
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.auth)
    // implementation(libs.supabase.realtime) // Uncomment if you need it later
    // implementation(libs.supabase.storage)  // Uncomment if you need it later

    // --- 3. KTOR (The Engine) ---
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.core)

    // --- 4. SERIALIZATION (JSON Parsing) ---
    implementation(libs.kotlinx.serialization.json)

    // --- 5. DESUGARING (Essential for crashes on older phones) ---
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    implementation("androidx.navigation:navigation-compose:2.8.0")
    // 1. Storage for Settings (Theme, Time)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // 2. Background Tasks (Notifications)
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // 3. Widget Library (Glance)
    implementation("androidx.glance:glance-appwidget:1.1.0")
    implementation("androidx.glance:glance-material3:1.1.0")

    implementation("androidx.compose.material:material-icons-extended:1.6.0")
}
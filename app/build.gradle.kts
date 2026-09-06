plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

val buildNumber: Int = providers.environmentVariable("GITHUB_RUN_NUMBER")
    .map { it.toInt() }
    .orElse(providers.gradleProperty("versionCode").map { it.toInt() })
    .getOrElse(1)

val appVersionName: String = "1.0.$buildNumber"

android {
    namespace = "com.velora.tracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.velora.tracker"
        minSdk = 26
        targetSdk = 34
        versionCode = buildNumber
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    signingConfigs {
        create("release") {
            val rootKeystore = rootProject.file("release.keystore")
            if (rootKeystore.exists()) {
                storeFile = rootKeystore
                storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "veloratracker"
                keyAlias = System.getenv("KEY_ALIAS") ?: "velora"
                keyPassword = System.getenv("KEY_PASSWORD") ?: "veloratracker"
            } else {
                initWith(getByName("debug"))
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose BOM
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.animation)
    debugImplementation(libs.compose.ui.tooling)

    // Activity
    implementation(libs.activity.compose)

    // Core
    implementation(libs.core.ktx)

    // Lifecycle
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)

    // Navigation
    implementation(libs.navigation.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // DataStore
    implementation(libs.datastore.preferences)

    // Coroutines
    implementation(libs.coroutines.android)

    // Unit Testing
    testImplementation("junit:junit:4.13.2")
}

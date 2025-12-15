plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.kotlin.compose.compiler)
    `maven-publish`
//    id("androidx.baselineprofile")
}

android {
    namespace = "com.enmanuelbergling.path_power"
    compileSdk = 36

    defaultConfig {
        minSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

    lint {
        checkReleaseBuilds = false
    }
}

dependencies {

    implementation(libs.androidx.core.core.ktx)

    implementation(platform(libs.androidx.compose.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.ui.graphics)
    implementation(libs.androidx.compose.ui.ui.tooling)
    implementation(libs.androidx.compose.ui.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.constraintlayout.constraintlayout.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.espresso.core)

//    "baselineProfile"(project(":baselineprofile"))

//    implementation("androidx.profileinstaller:profileinstaller:1.3.1")
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = "com.github.enmanuel52"
            artifactId = "path_power"
            version = "0.0.4"

            afterEvaluate {
                from(components["release"])
            }

        }
    }
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
//    id("androidx.baselineprofile")
}

android {
    namespace = "com.enmanuelbergling.path_power"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    //Compose
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

//    "baselineProfile"(project(":baselineprofile"))

//    implementation("androidx.profileinstaller:profileinstaller:1.3.1")
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = "com.github.enmanuel52"
            artifactId = "path_power"
            version = "0.0.5"

            afterEvaluate {
                from(components["release"])
            }

        }
    }
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}

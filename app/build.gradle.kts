plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.dokka") version "1.9.20"
}

android {
    namespace = "com.example.proyect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proyect"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.core:core-ktx:+")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.0-alpha03")
    androidTestImplementation("androidx.test.ext:junit:1.2.0-alpha03")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0-alpha03")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.0-alpha03")
    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:1.9.20")
}
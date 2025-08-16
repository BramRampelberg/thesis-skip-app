import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.application)
    id("skip-build-plugin")
}

skip {
}

android {
    namespace = group as String
    compileSdk = libs.versions.android.sdk.compile.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvm.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvm.get())
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.get().toString()
    }
    packaging {
        jniLibs {
            keepDebugSymbols.add("**/*.so")
            pickFirsts.add("**/*.so")
            // this option will compress JNI .so files
            useLegacyPackaging = true
        }
    }

    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()
        targetSdk = libs.versions.android.sdk.compile.get().toInt()
        // skip.tools.skip-build-plugin will automatically use Skip.env properties for:
        // applicationId = PRODUCT_BUNDLE_IDENTIFIER
        // versionCode = CURRENT_PROJECT_VERSION
        // versionName = MARKETING_VERSION
        addManifestPlaceholders(
            mapOf(
                "baseUrlPlaceholder" to "http://10.0.2.2:5000",
                "auth0Domain" to "rise-gent2.eu.auth0.com",
                "auth0ClientId" to "8vJtbXg2FptHGmKrpFl1tZwhiXOJZ57l",
                "auth0Audience" to "https://api.buut.be",
                "auth0Scheme" to "password"
            )
        )
    }

    buildFeatures {
        buildConfig = true
    }

    lintOptions {
        disable.add("Instantiatable")
        disable.add("MissingPermission")
    }

    // default signing configuration tries to load from keystore.properties
    signingConfigs {
        val keystorePropertiesFile = file("keystore.properties")
        if (keystorePropertiesFile.isFile) {
            create("release") {
                val keystoreProperties = Properties()
                keystoreProperties.load(keystorePropertiesFile.inputStream())
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
                storeFile = file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
            }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false // can be set to true for debugging release build, but needs to be false when uploading to store
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(17)
    androidTarget {
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.composee)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.multiplatformSettings)
            implementation(libs.kotlinx.datetime)
            implementation(libs.room.runtime)
            implementation(libs.composeIcons.featherIcons)
            implementation(libs.androidx.sqlite)
            implementation(libs.ktor.serialization.xml)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.multiplatform.settings.no.arg)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
            implementation(libs.mobileads)
            implementation(libs.androidx.camera.core)
            implementation(libs.androidx.camera.view)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.vision.common)
            implementation(libs.play.services.mlkit.text.recognition.common)
            implementation(libs.play.services.mlkit.text.recognition)
            implementation(libs.androidx.camera.camera2)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.camera.view)
            implementation(libs.androidx.glance.appwidget)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.alextos"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        targetSdk = 36

        applicationId = "com.alextos.converter"
        versionCode = 4
        versionName = "2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }
}

//https://developer.android.com/develop/ui/compose/testing#setup
dependencies {
    androidTestImplementation(libs.androidx.uitest.junit4)
    debugImplementation(libs.androidx.uitest.testManifest)
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.ui.android)
    implementation(libs.accessibility.test.framework)
    with(libs.room.compiler) {
        add("kspAndroid", this)
        add("kspIosX64", this)
        add("kspIosArm64", this)
        add("kspIosSimulatorArm64", this)
    }
}

// --- Ensure KSP waits for Compose Resources codegen ---
listOf("Debug", "Release").forEach { variant ->
    val kspTaskName = "ksp${variant}KotlinAndroid"
    tasks.matching { it.name == kspTaskName }.configureEach {
        dependsOn(
            // общие генераторы (common)
            "generateComposeResClass",
            "generateResourceAccessorsForCommonMain",
            "generateExpectResourceCollectorsForCommonMain",   // <-- добавлено
            // androidMain
            "generateResourceAccessorsForAndroidMain",
            "generateActualResourceCollectorsForAndroidMain",
            // variant-специфичный
            "generateResourceAccessorsForAndroid$variant"
        )
        // (опционально) если хочешь строгое упорядочивание без ввода в up-to-date:
        // mustRunAfter("generateComposeResClass", "generateResourceAccessorsForCommonMain", "generateExpectResourceCollectorsForCommonMain")
    }
}

ksp {
    // генерировать Kotlin-код (помогает уйти от части проблем в XProcessing)
    arg("room.generateKotlin", "true")
    // инкрементальная генерация
    arg("room.incremental", "true")
    // если на KSP2 ловил краш — можно временно отключить:
    // useKsp2 = false
}
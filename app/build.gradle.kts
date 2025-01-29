import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    jacoco
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/config/detekt.yml")
    baseline = file("$projectDir/config/baseline.xml")
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "11" // Utilise la version JVM ciblée par ton projet
}

val exclusions = listOf("null")

android {
    namespace = "com.example.tempomobileapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tempomobileapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
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

    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/DEPENDENCIES"
        }
    }
}

tasks.withType(org.gradle.api.tasks.testing.Test::class.java) {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

// Configure the JaCoCo report task to include Android tests
tasks.register<JacocoReport>("jacocoTestReport") {
    group = "Reporting"
    description = "Generate JaCoCo coverage reports after running tests."
    dependsOn(tasks.named("testDebugUnitTest"), tasks.named("connectedDebugAndroidTest"))

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    sourceDirectories.setFrom(files(
        layout.projectDirectory.dir("src/main/java")
    ))

    classDirectories.setFrom(files(
        fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")),
        fileTree(layout.buildDirectory.dir("intermediates/javac/debug/classes")),
        fileTree(layout.buildDirectory.dir("intermediates/javac/androidTest/debug/classes"))
    ))

    executionData.setFrom(
        fileTree(layout.buildDirectory.dir("outputs/code_coverage/debugAndroidTest/connected")).include("**/*.ec"),
        layout.buildDirectory.file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    )
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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.androidx.tracing.perfetto.handshake)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.compose.ui:ui-text-google-fonts:1.4.3")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    implementation("io.github.sridhar-sp:neumorphic:0.0.6")
    implementation("androidx.compose.ui:ui-graphics:1.3.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.compose.animation:animation:1.5.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.0")
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.robolectric:robolectric:4.10.3")
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.15")
    implementation("com.github.MahboubehSeyedpour:jetpack-loading:1.1.0")
    implementation("com.google.code.gson:gson:2.9.0")
    testImplementation("io.mockk:mockk:1.13.5")
    androidTestImplementation("io.mockk:mockk-android:1.13.5")
    testImplementation("com.linkedin.dexmaker:dexmaker:2.28.1")
    testImplementation("com.linkedin.dexmaker:dexmaker-mockito:2.28.1")

}
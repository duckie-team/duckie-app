/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

import AppVersionNameProvider.App.VersionName
import DependencyHandler.Extensions.androidTestImplementations
import DependencyHandler.Extensions.implementations
import DependencyHandler.Extensions.testImplementations
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.duckie.android.library)
    alias(libs.plugins.duckie.jvm.junit)
    alias(libs.plugins.duckie.android.hilt)
    alias(libs.plugins.duckie.version.name.provider)
    alias(libs.plugins.duckie.android.room)
}

android {
    namespace = "team.duckie.app.android.data"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "APP_VERSION_NAME", "\"$VersionName\"")
        // 정규식 패턴 설정
        val pattern = Regex("\\d+\\.\\d+\\.\\d+")
        // 정규식 패턴에 맞게 검색하여 결과 출력
        val versionNameNumber = pattern.findAll(VersionName).map { it.value }.first()
        buildConfigField("String", "APP_VERSION_NAME_NUMBER", "\"$versionNameNumber\"")
        buildConfigField("String", "REAL_API_URL", "\"${AppVersionNameProvider.App.realUrl}\"")
        buildConfigField("String", "STAGE_API_URL", "\"${AppVersionNameProvider.App.stageUrl}\"")
        manifestPlaceholders["KAKAO_MANIFEST_SCHEME"] =
            gradleLocalProperties(rootDir).getProperty("KAKAO_MANIFEST_SCHEME")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

// TODO(sungbin): ktor 의존성 제거 및 mockk 의존성 추가
// https://github.com/duckie-team/duckie-android/issues/180
dependencies {
    implementations(
        libs.login.kakao,
        libs.kotlin.coroutines,
        libs.kotlin.collections.immutable,
        libs.paging.runtime,
        libs.jackson.databind,
        libs.bundles.fuel,
        libs.bundles.moshi,
        libs.bundles.ktor.client,
        libs.logging.timber,
        projects.domain,
        projects.common.kotlin,
        projects.pluginKtorClient,
        projects.core.datastore,
    )
    testImplementations(
        libs.test.coroutines,
        libs.test.ktor.client,
        libs.test.ktor.server, // for E2E test
        libs.ktor.server.core, // for E2E test
    )
    androidTestImplementations(
        libs.test.androidx.junit,
        libs.test.androidx.runner,
        libs.test.strikt,
    )
    implementation(libs.ktx.lifecycle.viewmodel) { // for androidTest
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel")
    }
}

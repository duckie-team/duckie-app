/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

import DependencyHandler.Extensions.implementations

plugins {
    alias(libs.plugins.duckie.android.library)
    alias(libs.plugins.duckie.android.library.compose)
    alias(libs.plugins.duckie.android.hilt)
}

android {
    namespace = "team.duckie.app.android.feature.search"
}

dependencies {
    implementations(
        platform(libs.firebase.bom),
        projects.di,
        projects.domain,
        projects.navigator,
        projects.common.android,
        projects.common.kotlin,
        projects.common.compose,
        libs.orbit.viewmodel,
        libs.orbit.compose,
        libs.compose.lifecycle.runtime,
        libs.compose.ui.material,
        libs.firebase.crashlytics,
        libs.paging.runtime,
        libs.paging.compose,
        libs.quack.v2.ui,
        libs.kotlin.collections.immutable,
    )
}

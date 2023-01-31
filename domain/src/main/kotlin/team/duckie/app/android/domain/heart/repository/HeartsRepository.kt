/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.domain.heart.repository

import androidx.compose.runtime.Immutable
import team.duckie.app.android.domain.heart.model.HeartBody

@Immutable
interface HeartsRepository {
    suspend fun heart(heartsBody: Int): Int

    suspend fun unHeart(heartsBody: HeartBody): Boolean
}

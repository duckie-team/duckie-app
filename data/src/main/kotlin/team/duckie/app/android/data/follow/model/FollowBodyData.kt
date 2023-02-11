/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.data.follow.model

import com.fasterxml.jackson.annotation.JsonProperty

// TODO(riflockle7): value class 적용 검토하기
internal data class FollowBodyData(
    @field:JsonProperty("followingId")
    val followingId: Int? = null,
)

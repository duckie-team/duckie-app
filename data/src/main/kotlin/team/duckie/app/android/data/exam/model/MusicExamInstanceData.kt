/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.data.exam.model

import com.fasterxml.jackson.annotation.JsonProperty
import team.duckie.app.android.data.user.model.UserResponse

data class MusicExamInstanceData(
    @field:JsonProperty("user")
    val user: UserResponse? = null,
    @field:JsonProperty("takenTime")
    val takenTime: Double? = null,
    @field:JsonProperty("score")
    val score: Double? = null,
    @field:JsonProperty("correctProblemCount")
    val correctProblemCount: Int? = null,
    @field:JsonProperty("status")
    val status: String? = null,
)

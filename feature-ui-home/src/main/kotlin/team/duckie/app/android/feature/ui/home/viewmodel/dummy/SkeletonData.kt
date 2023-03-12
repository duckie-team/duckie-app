/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.feature.ui.home.viewmodel.dummy

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import team.duckie.app.android.domain.exam.model.Exam
import team.duckie.app.android.domain.recommendation.model.ExamType
import team.duckie.app.android.domain.recommendation.model.RecommendationItem
import team.duckie.app.android.domain.tag.model.Tag
import team.duckie.app.android.domain.user.model.DuckPower
import team.duckie.app.android.domain.user.model.User
import team.duckie.app.android.feature.ui.home.viewmodel.state.HomeState
import team.duckie.app.android.shared.ui.compose.DuckTestCoverItem
import team.duckie.app.android.util.kotlin.randomString

internal val skeletonJumbotrons = persistentListOf(
    HomeState.HomeRecommendJumbotron(
        examId = 1,
        coverUrl = "",
        title = randomString(12),
        content = randomString(18),
        buttonContent = randomString(10),
        type = ExamType.Video,
    ),
)

internal val skeletonRecommendationItems = listOf(
    RecommendationItem(
        id = 0,
        title = randomString(20),
        tag = null,
        exams = listOf(
            Exam.empty().copy(
                user = User.empty().copy(nickname = randomString(3)),
                solvedCount = 20,
                description = randomString(6),
            ),
        ),
    ),
)

internal val skeletonExamineeItems = (1..10).map {
    if (it == 1) {
        User.empty().copy(
            id = it,
            profileImageUrl = "",
            nickname = randomString(6),
            duckPower = DuckPower(id = it, tier = "99", Tag(id = it, name = randomString(4))),
            favoriteTags = listOf(Tag(id = it, name = "도로패션"))
        )
    } else {
        User.empty().copy(
            id = it,
            profileImageUrl = "",
            nickname = randomString(3),
            duckPower = DuckPower(id = it, tier = "99", Tag(id = it, name = randomString(4)))
        )
    }
}.toImmutableList()

internal val skeletonRankingItems = (1..10).map {
    DuckTestCoverItem(
        testId = it,
        thumbnailUrl = thumbnailExample,
        nickname = randomString(3),
        title = randomString(6),
        solvedCount = 20
    )
}.toImmutableList()

private const val thumbnailExample = "https://plus.unsplash.com/premium_photo-1669737010860-fc8de46eeb39?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80"

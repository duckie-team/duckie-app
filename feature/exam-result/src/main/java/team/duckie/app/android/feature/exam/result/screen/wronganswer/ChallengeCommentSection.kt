/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.feature.exam.result.screen.wronganswer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import team.duckie.app.android.common.compose.ui.QuackMaxWidthDivider
import team.duckie.app.android.common.compose.ui.Spacer
import team.duckie.app.android.common.compose.ui.quack.QuackProfileImage
import team.duckie.app.android.feature.exam.result.viewmodel.ExamResultState
import team.duckie.quackquack.material.QuackColor
import team.duckie.quackquack.material.QuackTypography
import team.duckie.quackquack.material.icon.QuackIcon
import team.duckie.quackquack.material.icon.quackicon.Outlined
import team.duckie.quackquack.material.icon.quackicon.outlined.ArrowDown
import team.duckie.quackquack.material.quackClickable
import team.duckie.quackquack.ui.QuackIcon
import team.duckie.quackquack.ui.QuackText
import team.duckie.quackquack.ui.sugar.QuackBody2
import team.duckie.quackquack.ui.sugar.QuackHeadLine2

@Composable
internal fun ColumnScope.ChallengeCommentSection(
    profileUrl: String,
    myAnswer: String,
    onHeartComment: (Int) -> Unit,
    commentsTotal: Int,
    comments: ImmutableList<ExamResultState.Success.ChallengeCommentUiModel>,
    showCommentSheet: () -> Unit,
) {
    Spacer(space = 28.dp)
    QuackHeadLine2(text = "답도 없는 오답들")
    Spacer(space = 20.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QuackProfileImage(
            profileUrl = profileUrl,
            size = DpSize(width = 40.dp, height = 40.dp),
        )
        Spacer(space = 8.dp)
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            QuackBody2(text = "나의 답")
            QuackHeadLine2(text = myAnswer)
        }
    }
    Spacer(space = 12.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(QuackColor.Gray4.value)
            .border(
                width = 1.dp,
                color = QuackColor.Gray3.value,
                shape = RoundedCornerShape(8.dp),
            )
            .quackClickable(
                onClick = showCommentSheet,
            )
            .padding(
                horizontal = 12.dp,
                vertical = 16.dp,
            ),
    ) {
        QuackText(
            text = "댓글을 남겨보세요!",
            typography = QuackTypography.Body2.change(
                color = QuackColor.Gray2,
            ),
        )
    }
    Spacer(space = 20.dp)
    QuackMaxWidthDivider()
    Spacer(space = 20.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QuackText(
            text = "전체 댓글 ${commentsTotal}개",
            typography = QuackTypography.Body1.change(
                color = QuackColor.Gray1,
            ),
        )
        QuackIcon(icon = QuackIcon.Outlined.ArrowDown)
    }
    Spacer(space = 8.dp)
    comments.forEach { item ->
        key(item.id) {
            ChallengeComment(
                wrongComment = item,
                onHeartClick = { commentId ->
                    onHeartComment(commentId)
                },
            )
            Spacer(space = 8.dp)
        }
    }
}

@Composable
internal fun ColumnScope.PopularCommentSection(
    myAnswer: String,
    equalAnswerCount: Int,
) {
    Spacer(space = 28.dp)
    QuackHeadLine2(text = "이 문제 인기 오답")
    Spacer(space = 8.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFFEFCF),
                shape = RoundedCornerShape(8.dp),
            )
            .padding(
                vertical = 16.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        QuackHeadLine2(text = myAnswer)
        Spacer(space = 4.dp)
        QuackBody2(text = "이 문제를 푼 유저 중 ${equalAnswerCount}명이 이렇게 답을 적었어요!")
    }
    Spacer(space = 28.dp)
}

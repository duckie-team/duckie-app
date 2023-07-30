/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

@file:OptIn(
    ExperimentalDesignToken::class,
    ExperimentalQuackQuackApi::class,
    ExperimentalDesignToken::class,
)

package team.duckie.app.android.feature.exam.result.screen.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import team.duckie.app.android.common.compose.DuckieFitImage
import team.duckie.app.android.common.compose.ui.QuackMaxWidthDivider
import team.duckie.app.android.common.compose.ui.Spacer
import team.duckie.app.android.common.compose.ui.icon.v2.FilledHeart
import team.duckie.app.android.common.compose.ui.quack.QuackProfileImage
import team.duckie.app.android.feature.exam.result.R
import team.duckie.app.android.feature.exam.result.screen.RANKER_THRESHOLD
import team.duckie.app.android.feature.exam.result.viewmodel.ExamResultState
import team.duckie.quackquack.animation.animateQuackColorAsState
import team.duckie.quackquack.material.QuackColor
import team.duckie.quackquack.material.QuackTypography
import team.duckie.quackquack.material.icon.QuackIcon
import team.duckie.quackquack.material.icon.quackicon.Outlined
import team.duckie.quackquack.material.icon.quackicon.outlined.ArrowDown
import team.duckie.quackquack.material.icon.quackicon.outlined.Heart
import team.duckie.quackquack.material.quackClickable
import team.duckie.quackquack.ui.QuackFilledTextField
import team.duckie.quackquack.ui.QuackIcon
import team.duckie.quackquack.ui.QuackImage
import team.duckie.quackquack.ui.QuackText
import team.duckie.quackquack.ui.QuackTextFieldStyle
import team.duckie.quackquack.ui.optin.ExperimentalDesignToken
import team.duckie.quackquack.ui.span
import team.duckie.quackquack.ui.sugar.QuackBody1
import team.duckie.quackquack.ui.sugar.QuackBody2
import team.duckie.quackquack.ui.sugar.QuackHeadLine1
import team.duckie.quackquack.ui.sugar.QuackHeadLine2
import team.duckie.quackquack.ui.sugar.QuackTitle2
import team.duckie.quackquack.ui.util.ExperimentalQuackQuackApi
import java.util.Locale

@Composable
internal fun QuizResultContent(
    modifier: Modifier = Modifier,
    nickname: String,
    resultImageUrl: String,
    time: Double,
    correctProblemCount: Int,
    mainTag: String,
    message: String,
    ranking: Int,
    // for wrong answer
    profileImg: String,
    myAnswer: String,
    equalAnswerCount: Int,
    wrongComments: kotlinx.collections.immutable.ImmutableList<ExamResultState.Success.WrongAnswerComment>,
    myComment: String,
    myCommentChanged: (String) -> Unit,
    onHeartComment: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
    ) {
        if (myAnswer.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp)
                    .background(
                        color = QuackColor.Gray4.value,
                        shape = RoundedCornerShape(8.dp),
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                QuackText(
                    text = "나의 답",
                    typography = QuackTypography.Body3.change(
                        color = QuackColor.Gray1,
                    ),
                )
                Spacer(space = 2.dp)
                QuackHeadLine2(text = myAnswer)
            }
            Spacer(space = 12.dp)
        }
        DuckieFitImage(
            imageUrl = resultImageUrl,
            horizontalPadding = PaddingValues(horizontal = 0.dp),
        )
        Spacer(space = 16.dp)
        // TODO(limsaehyun): QuackText Quote의 버그가 픽스된 후 아래 코드로 변경해야 함
        // https://duckie-team.slack.com/archives/C054HU0CKMY/p1688278156256779
        // QuackText(text = message, typography = QuackTypography.Quote)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            QuackHeadLine1(text = "\"")
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                QuackHeadLine1(text = message)
            }
            QuackHeadLine1(text = "\"")
        }
        Spacer(space = 24.dp)
        QuackMaxWidthDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(79.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                QuizResultTitleAndDescription(
                    title = stringResource(
                        id = R.string.exam_result_time,
                        String.format(Locale.US, "%.2f", time),
                    ),
                    description = stringResource(id = R.string.exam_result_total_time),
                )
            }
            Box(
                modifier = Modifier
                    .size(1.dp, 18.dp)
                    .background(color = QuackColor.Gray3.value),
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                QuizResultTitleAndDescription(
                    title = stringResource(
                        id = R.string.exam_result_correct_problem_unit,
                        correctProblemCount,
                    ),
                    description = stringResource(id = R.string.exam_result_score),
                )
            }
        }
        QuackMaxWidthDivider()
        Spacer(space = 24.dp)
        QuackHeadLine1(
            modifier = Modifier.span(
                texts = listOf(nickname, mainTag, "${ranking}위"),
                style = SpanStyle(
                    color = QuackColor.DuckieOrange.value,
                    fontWeight = FontWeight.Bold,
                ),
            ),
            text = stringResource(
                id = if (ranking <= RANKER_THRESHOLD) {
                    R.string.exam_result_finish_title_ranker
                } else {
                    R.string.exam_result_finish_title_etc
                },
                nickname,
                mainTag,
                ranking,
            ),
        )
        // 오답 영역
        Spacer(space = 28.dp)
        QuizResultLargeDivider()
        PopularWrongAnswerSection(
            myAnswer = myAnswer,
            equalAnswerCount = equalAnswerCount,
        )
        QuizResultLargeDivider()
        WrongAnswerSection(
            profileUrl = profileImg,
            myAnswer = myAnswer,
            comment = myComment,
            onCommentChange = myCommentChanged,
            wrongAnswerComments = wrongComments,
            onHeartComment = onHeartComment,
        )
    }
}

@Composable
private fun ColumnScope.WrongAnswerSection(
    profileUrl: String,
    myAnswer: String,
    comment: String,
    onCommentChange: (String) -> Unit,
    onHeartComment: (Int) -> Unit,
    wrongAnswerComments: kotlinx.collections.immutable.ImmutableList<ExamResultState.Success.WrongAnswerComment>,
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
    QuackFilledTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        value = comment,
        onValueChange = onCommentChange,
        style = QuackTextFieldStyle.FilledLarge,
        placeholderText = "댓글을 남겨보세요!",
    )
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
            text = "전체 댓글 ${wrongAnswerComments.size}개",
            typography = QuackTypography.Body1.change(
                color = QuackColor.Gray1,
            ),
        )
        QuackIcon(icon = QuackIcon.Outlined.ArrowDown)
    }
    Spacer(space = 8.dp)
    wrongAnswerComments.forEach { item ->
        key(item.id) {
            WrongCommentInternal(
                wrongComment = item,
                onHeartClick = { likeId ->
                    onHeartComment(likeId)
                },
            )
            Spacer(space = 8.dp)
        }
    }
}

@Composable
private fun WrongCommentInternal(
    wrongComment: ExamResultState.Success.WrongAnswerComment,
    onHeartClick: (Int) -> Unit,
) {
    val hearTextColor =
        animateQuackColorAsState(targetValue = if (wrongComment.isLike) QuackColor.Gray1 else QuackColor.Gray2)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Row {
            QuackProfileImage(
                profileUrl = wrongComment.profileImg,
                size = DpSize(width = 40.dp, height = 40.dp),
            )
            Spacer(space = 8.dp)
            Column {
                Row {
                    QuackText(
                        text = "${wrongComment.nickname}님의 답",
                        typography = QuackTypography.Body3.change(
                            color = QuackColor.Gray1,
                        ),
                    )
                    Spacer(space = 4.dp)
                    QuackText(
                        text = wrongComment.createAt.toString(), // TODO(limasaehyun): 시간 계산 로직
                        typography = QuackTypography.Body3.change(
                            color = QuackColor.Gray2,
                        ),
                    )
                }
                Spacer(space = 2.dp)
                QuackTitle2(text = wrongComment.answer)
                Spacer(space = 4.dp)
                QuackBody1(text = wrongComment.comment)
            }
            Spacer(weight = 1f)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                QuackImage(
                    modifier = Modifier
                        .size(DpSize(24.dp, 24.dp))
                        .quackClickable(onClick = {
                            onHeartClick(0)
                        },),
                    src = if (wrongComment.isLike) {
                        QuackIcon.FilledHeart
                    } else {
                        QuackIcon.Outlined.Heart
                    },
                )
                QuackText(
                    text = wrongComment.likeCnt.toString(),
                    typography = QuackTypography.Body3.change(
                        color = hearTextColor.value,
                    ),
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.PopularWrongAnswerSection(
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

@Composable
private fun QuizResultLargeDivider() {
    QuackMaxWidthDivider(
        modifier = Modifier.height(8.dp),
    )
}

@Composable
private fun QuizResultTitleAndDescription(
    title: String,
    description: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuackHeadLine2(text = title)
        Spacer(space = 2.dp)
        QuackText(
            text = description,
            typography = QuackTypography.Body2.change(
                color = QuackColor.Gray1,
            ),
        )
    }
}

@OptIn(ExperimentalDesignToken::class, ExperimentalQuackQuackApi::class)
@Composable
@Preview
fun PreviewTextField() {
    val text = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        QuackFilledTextField(
            value = text.value,
            onValueChange = {
                text.value = it
            },
            style = QuackTextFieldStyle.FilledLarge,
        )
        QuackFilledTextField(
            value = text.value,
            onValueChange = {
                text.value = it
            },
            style = QuackTextFieldStyle.FilledFlat,
        )
//        QuackFilledTextField(
//            value = text.value,
//            onValueChange = {
//                text.value = it
//            },
//            style = QuackTextFieldStyle.Default,
//        )
    }
}

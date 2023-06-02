/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

@file:Suppress("ConstPropertyName", "PrivatePropertyName")

package team.duckie.app.android.feature.onboard.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import team.duckie.app.android.feature.onboard.R
import team.duckie.app.android.feature.onboard.constant.OnboardStep
import team.duckie.app.android.feature.onboard.viewmodel.OnboardViewModel
import team.duckie.app.android.common.compose.activityViewModel
import team.duckie.app.android.common.compose.asLoose
import team.duckie.app.android.common.compose.systemBarPaddings
import team.duckie.app.android.common.kotlin.fastFirstOrNull
import team.duckie.app.android.common.kotlin.npe
import team.duckie.quackquack.ui.color.QuackColor
import team.duckie.quackquack.ui.component.QuackBody3
import team.duckie.quackquack.ui.component.QuackHeadLine2
import team.duckie.quackquack.ui.component.QuackImage
import team.duckie.quackquack.ui.textstyle.QuackTextStyle

@Suppress("UnusedPrivateMember", "unused")
private val currentStep = OnboardStep.Login

private const val LoginScreenWelcomeLayoutId = "LoginScreenWelcome"
private const val LoginScreenLoginAreaLayoutId = "LoginScreenLoginArea"

private val LoginScreenMeasurePolicy = MeasurePolicy { measurables, constraints ->
    val looseConstraints = constraints.asLoose()

    val welcomeMeasurable = measurables.fastFirstOrNull { measurable ->
        measurable.layoutId == LoginScreenWelcomeLayoutId
    } ?: npe()

    val loginPlaceable = measurables.fastFirstOrNull { measurable ->
        measurable.layoutId == LoginScreenLoginAreaLayoutId
    }?.measure(looseConstraints) ?: npe()

    val loginAreaHeight = loginPlaceable.height
    val welcomeHeight = constraints.maxHeight - loginAreaHeight

    val welcomeConstraints = constraints.copy(
        minHeight = welcomeHeight,
        maxHeight = welcomeHeight,
    )
    val welcomePlaceable = welcomeMeasurable.measure(welcomeConstraints)

    layout(
        width = constraints.maxWidth,
        height = constraints.maxHeight,
    ) {
        welcomePlaceable.place(
            x = 0,
            y = 0,
        )
        loginPlaceable.place(
            x = 0,
            y = welcomeHeight,
        )
    }
}

@Composable
internal fun LoginScreen() {
    Layout(
        modifier = Modifier
            .fillMaxSize()
            .padding(systemBarPaddings)
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp,
            ),
        content = {
            LoginScreenWelcome()
            LoginScreenLoginArea()
        },
        measurePolicy = LoginScreenMeasurePolicy,
    )
}

@Composable
private fun LoginScreenWelcome() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .layoutId(LoginScreenWelcomeLayoutId),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        QuackImage(
            src = R.drawable.img_duckie_talk,
            size = DpSize(
                width = 180.dp,
                height = 248.dp,
            ),
        )
        QuackHeadLine2(
            text = stringResource(R.string.kakaologin_welcome_message),
            align = TextAlign.Center,
        )
    }
}

private const val LoginScreenLoginAreaKakaoSymbolLayoutId = "LoginScreenLoginAreaKakaoSymbol"
private const val LoginScreenLoginAreaKakaoLoginLabelLayoutId = "LoginScreenLoginAreaKakaoLoginLabel"

private val LoginScreenLoginAreaMeasurePolicy = MeasurePolicy { measurables, constraints ->
    val extraLooseConstraints = constraints.asLoose(width = true)

    val symbolPlaceable = measurables.fastFirstOrNull { measurable ->
        measurable.layoutId == LoginScreenLoginAreaKakaoSymbolLayoutId
    }?.measure(extraLooseConstraints) ?: npe()

    val labelPlaceable = measurables.fastFirstOrNull { measurable ->
        measurable.layoutId == LoginScreenLoginAreaKakaoLoginLabelLayoutId
    }?.measure(extraLooseConstraints) ?: npe()

    layout(
        width = constraints.maxWidth,
        height = constraints.maxHeight,
    ) {
        symbolPlaceable.place(
            x = 30.dp.roundToPx(),
            y = Alignment.CenterVertically.align(
                size = symbolPlaceable.height,
                space = constraints.maxHeight,
            ),
        )
        labelPlaceable.place(
            x = Alignment.CenterHorizontally.align(
                size = labelPlaceable.width,
                space = constraints.maxWidth,
                layoutDirection = layoutDirection,
            ),
            y = Alignment.CenterVertically.align(
                size = labelPlaceable.height,
                space = constraints.maxHeight,
            ),
        )
    }
}

@Composable
private fun LoginScreenLoginArea(vm: OnboardViewModel = activityViewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.layoutId(LoginScreenLoginAreaLayoutId),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 카카오 로그인 버튼
        // https://developers.kakao.com/docs/latest/ko/kakaologin/design-guide
        Layout(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = Color(
                        ContextCompat.getColor(
                            context,
                            R.color.kakao_login_button_container_background,
                        ),
                    ),
                )
                .clickable {
                    coroutineScope.launch {
                        vm.getKakaoAccessTokenAndJoin()
                    }
                },
            content = {
                Image(
                    modifier = Modifier
                        .layoutId(LoginScreenLoginAreaKakaoSymbolLayoutId)
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_kakao_symbol),
                    colorFilter = ColorFilter.tint(
                        Color(
                            ContextCompat.getColor(
                                context,
                                R.color.kakao_login_button_symbol_tint,
                            ),
                        ),
                    ),
                    contentDescription = null,
                )
                // QuackColor 생성자가 internal 이라 BasicText 사용
                BasicText(
                    modifier = Modifier.layoutId(LoginScreenLoginAreaKakaoLoginLabelLayoutId),
                    text = stringResource(R.string.kakaologin_button_label),
                    style = QuackTextStyle.HeadLine2.asComposeStyle().copy(
                        color = Color(
                            ContextCompat.getColor(
                                context,
                                R.color.kakao_login_button_label,
                            ),
                        ),
                    ),
                )
            },
            measurePolicy = LoginScreenLoginAreaMeasurePolicy,
        )
        QuackBody3(
            text = stringResource(R.string.kakaologin_login_terms),
            color = QuackColor.Gray2,
            align = TextAlign.Center,
        )
    }
}
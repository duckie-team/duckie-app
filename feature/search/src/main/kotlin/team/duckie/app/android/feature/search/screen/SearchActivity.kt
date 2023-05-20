/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.feature.search.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.compose.collectAsState
import team.duckie.app.android.feature.search.R
import team.duckie.app.android.feature.search.constants.SearchResultStep
import team.duckie.app.android.feature.search.constants.SearchStep
import team.duckie.app.android.feature.search.viewmodel.SearchViewModel
import team.duckie.app.android.feature.search.viewmodel.sideeffect.SearchSideEffect
import team.duckie.app.android.navigator.feature.detail.DetailNavigator
import team.duckie.app.android.navigator.feature.profile.ProfileNavigator
import team.duckie.app.android.shared.ui.compose.DuckieCircularProgressIndicator
import team.duckie.app.android.shared.ui.compose.ErrorScreen
import team.duckie.app.android.shared.ui.compose.constant.SharedIcon
import team.duckie.app.android.shared.ui.compose.quack.QuackNoUnderlineTextField
import team.duckie.app.android.common.compose.collectAndHandleState
import team.duckie.app.android.common.android.ui.finishWithAnimation
import team.duckie.app.android.common.android.ui.popStringExtra
import team.duckie.quackquack.ui.animation.QuackAnimatedContent
import team.duckie.quackquack.ui.color.QuackColor
import team.duckie.quackquack.ui.component.QuackImage
import team.duckie.quackquack.ui.icon.QuackIcon
import team.duckie.quackquack.ui.theme.QuackTheme
import team.duckie.quackquack.ui.util.DpSize
import javax.inject.Inject

internal val SearchHorizontalPadding = PaddingValues(horizontal = 16.dp)

@AndroidEntryPoint
class SearchActivity : team.duckie.app.android.common.android.ui.BaseActivity() {

    @Inject
    lateinit var detailNavigator: DetailNavigator

    @Inject
    lateinit var profileNavigator: ProfileNavigator

    private val vm: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.popStringExtra(team.duckie.app.android.common.android.ui.const.Extras.SearchTag)?.let { str ->
            vm.updateSearchKeyword(
                keyword = str,
                debounce = false,
            )
        }

        setContent {
            val state = vm.collectAsState().value

            vm.searchUsers.collectAndHandleState(handleLoadStates = vm::checkError)
            vm.searchExams.collectAndHandleState(handleLoadStates = vm::checkError)

            LaunchedEffect(key1 = vm) {
                vm.container.sideEffectFlow
                    .onEach(::handleSideEffect)
                    .launchIn(this)
            }

            QuackTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        modifier = Modifier.background(QuackColor.White.composeColor),
                    ) {
                        SearchTextFieldTopBar(
                            modifier = Modifier.padding(SearchHorizontalPadding),
                            searchKeyword = state.searchKeyword,
                            onSearchKeywordChanged = { keyword ->
                                vm.updateSearchKeyword(keyword = keyword)
                            },
                            onPrevious = {
                                finishWithAnimation()
                            },
                            clearSearchKeyword = {
                                vm.clearSearchKeyword()
                            },
                        )
                        QuackAnimatedContent(
                            targetState = state.searchStep,
                        ) { step ->
                            when (step) {
                                SearchStep.Search -> SearchScreen(vm = vm)
                                SearchStep.SearchResult -> {
                                    if (state.isSearchProblemError &&
                                        state.tagSelectedTab == SearchResultStep.DuckExam
                                    ) {
                                        ErrorScreen(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .statusBarsPadding(),
                                            false,
                                            onRetryClick = {
                                                vm.fetchSearchExams(state.searchKeyword)
                                            },
                                        )
                                    } else if (state.isSearchUserError &&
                                        state.tagSelectedTab == SearchResultStep.User
                                    ) {
                                        ErrorScreen(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .statusBarsPadding(),
                                            false,
                                            onRetryClick = {
                                                vm.fetchSearchUsers(state.searchKeyword)
                                            },
                                        )
                                    } else {
                                        SearchResultScreen(
                                            navigateDetail = { examId ->
                                                vm.navigateToDetail(examId = examId)
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (state.isSearchLoading) {
                        DuckieCircularProgressIndicator()
                    }
                }
            }
        }
    }

    private fun handleSideEffect(sideEffect: SearchSideEffect) {
        when (sideEffect) {
            is SearchSideEffect.ReportError -> {
                Firebase.crashlytics.recordException(sideEffect.exception)
            }

            is SearchSideEffect.NavigateToDetail -> {
                detailNavigator.navigateFrom(
                    activity = this@SearchActivity,
                    intentBuilder = {
                        putExtra(team.duckie.app.android.common.android.ui.const.Extras.ExamId, sideEffect.examId)
                    },
                )
            }

            is SearchSideEffect.NavigateToUserProfile -> {
                profileNavigator.navigateFrom(
                    activity = this,
                    intentBuilder = {
                        putExtra(team.duckie.app.android.common.android.ui.const.Extras.UserId, sideEffect.userId)
                    },
                )
            }
        }
    }
}

@Composable
private fun SearchTextFieldTopBar(
    modifier: Modifier = Modifier,
    searchKeyword: String,
    onSearchKeywordChanged: (String) -> Unit,
    clearSearchKeyword: () -> Unit,
    onPrevious: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        QuackImage(
            src = QuackIcon.ArrowBack,
            size = DpSize(all = 24.dp),
            onClick = onPrevious,
        )
        Spacer(modifier = Modifier.width(8.dp))
        QuackNoUnderlineTextField(
            text = searchKeyword,
            onTextChanged = { keyword ->
                onSearchKeywordChanged(keyword)
            },
            placeholderText = stringResource(id = R.string.try_search),
            trailingIcon = SharedIcon.ic_textfield_delete_16,
            trailingIconOnClick = clearSearchKeyword,
            trailingEndPadding = 13.dp,
        )
    }
}

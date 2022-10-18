package land.sungbin.androidprojecttemplate.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import land.sungbin.androidprojecttemplate.R
import land.sungbin.androidprojecttemplate.common.component.DuckieFab
import land.sungbin.androidprojecttemplate.common.UiStatus
import land.sungbin.androidprojecttemplate.common.component.DuckieLoadingIndicator
import land.sungbin.androidprojecttemplate.domain.model.Feed
import land.sungbin.androidprojecttemplate.domain.model.constraint.FeedType
import land.sungbin.androidprojecttemplate.home.component.DuckDealHolder
import land.sungbin.androidprojecttemplate.home.component.FeedHeader
import land.sungbin.androidprojecttemplate.home.component.FeedHolder
import land.sungbin.androidprojecttemplate.home.component.HomeDuckDealFeed
import land.sungbin.androidprojecttemplate.home.component.HomeNormalFeed
import land.sungbin.androidprojecttemplate.home.component.dummyTags
import land.sungbin.androidprojecttemplate.home.component.getTradingMethodResourceId
import land.sungbin.androidprojecttemplate.home.component.priceToString
import land.sungbin.androidprojecttemplate.shared.compose.extension.noRippleClickable
import team.duckie.quackquack.ui.component.QuackBody2
import team.duckie.quackquack.ui.component.QuackBottomSheetItem
import team.duckie.quackquack.ui.component.QuackDivider
import team.duckie.quackquack.ui.component.QuackHeadLine2
import team.duckie.quackquack.ui.component.QuackHeadlineBottomSheet
import team.duckie.quackquack.ui.component.QuackImage
import team.duckie.quackquack.ui.component.QuackMenuFabItem
import team.duckie.quackquack.ui.component.QuackModalDrawer
import team.duckie.quackquack.ui.component.QuackRoundImage
import team.duckie.quackquack.ui.component.QuackSimpleBottomSheet
import team.duckie.quackquack.ui.component.QuackTitle1
import team.duckie.quackquack.ui.component.QuackTitle2
import team.duckie.quackquack.ui.component.QuackTopAppBar
import team.duckie.quackquack.ui.component.rememberQuackDrawerState
import team.duckie.quackquack.ui.icon.QuackIcon

@Stable
internal val DuckieLogoSize = DpSize(
    width = 72.dp,
    height = 24.dp,
)

@Stable
internal val homeFabPadding = PaddingValues(
    bottom = 12.dp,
    end = 16.dp
)

internal val homeFabMenuItems = persistentListOf(
    QuackMenuFabItem(
        icon = QuackIcon.Feed,
        text = "피드",
    ),
    QuackMenuFabItem(
        icon = QuackIcon.Buy,
        text = "덕딜",
    )
)


@OptIn(ExperimentalMaterialApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val homeState by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberQuackDrawerState()
    val homeBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val moreBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    var selectedUser by remember {
        mutableStateOf("")
    }
    QuackModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent()
        }
    ) {
        QuackHeadlineBottomSheet(
            bottomSheetState = homeBottomSheetState,
            headline = stringResource(id = R.string.feed_filtering_title),
            items = persistentListOf(
                QuackBottomSheetItem(
                    title = stringResource(id = R.string.feed_filtering_both_feed_duck_deal),
                    isImportant = false,
                ),
                QuackBottomSheetItem(
                    title = stringResource(id = R.string.feed_filtering_feed),
                    isImportant = true,
                ),
                QuackBottomSheetItem(
                    title = stringResource(id = R.string.feed_filtering_duck_deal),
                    isImportant = false,
                )

            ),
            onClick = { quackBottomSheetItem ->

            }
        ) {
            QuackSimpleBottomSheet(
                bottomSheetState = moreBottomSheetState,
                items = persistentListOf(
                    QuackBottomSheetItem(
                        title = stringResource(R.string.follow_other, selectedUser),
                        isImportant = false,
                    ),
                    QuackBottomSheetItem(
                        title = stringResource(R.string.blocking_other_feed, selectedUser),
                        isImportant = false,
                    ),
                    QuackBottomSheetItem(
                        title = stringResource(R.string.report),
                        isImportant = true,
                    )
                ),
                onClick = {

                }
            ) {
                when (homeState.status) {
                    is UiStatus.Success -> {
                        HomeComponent(
                            feeds = homeState.feeds,
                            onClickLeadingIcon = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            },
                            onClickTrailingIcon = {
                                coroutineScope.launch {
                                    homeBottomSheetState.show()
                                }
                            },
                            onClickMoreIcon = {
                                coroutineScope.launch {
                                    moreBottomSheetState.show()
                                }
                            }
                        )
                    }

                    UiStatus.Loading -> {
                        Crossfade(targetState = homeState.status) {
                            DuckieLoadingIndicator()
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun HomeComponent(
    feeds: List<Feed>,
    onClickLeadingIcon: () -> Unit,
    onClickTrailingIcon: () -> Unit,
    onClickMoreIcon: () -> Unit,
) {
    val selectedTags = remember {
        mutableStateOf(
            dummyTags
        )
    }
    val commentCount by remember {
        mutableStateOf(0)
    }
    var isLike by remember {
        mutableStateOf(false)
    }
    var likeCount by remember {
        mutableStateOf(0)
    }
    var fabExpanded by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        QuackTopAppBar(
            leadingIcon = QuackIcon.Profile,
            onClickLeadingIcon = onClickLeadingIcon,
            centerContent = {
                QuackImage(
                    src = R.drawable.top_bar_logo,
                    overrideSize = DuckieLogoSize,
                )
            },
            trailingIcon = QuackIcon.Filter,
            onClickTrailingIcon = onClickTrailingIcon,
        )
        Box(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 24.dp,
                start = 16.dp,
                end = 16.dp,
            )
        ) {
            FeedHeader(
                profile = R.drawable.duckie_profile,
                title = stringResource(id = R.string.duckie_name),
                content = stringResource(id = R.string.duckie_introduce),
                tagItems = selectedTags.value,
                onTagClick = { index: Int ->
                    selectedTags.value = selectedTags.value - selectedTags.value[index]
                }
            )
        }
        LazyColumn(
            modifier = Modifier.weight(
                weight = 1f,
            ),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(
                space = 24.dp,
            )
        ) {
            items(
                items = feeds,
                key = { feed: Feed ->
                    feed.id
                }
            ) { feed: Feed -> // //DuckDeal 이므로 null이 아님을 보장
                when (feed.type) {
                    FeedType.Normal -> {
                        HomeNormalFeed(
                            FeedHolder(
                                profile = feed.writerId,
                                nickname = feed.writerId,
                                time = "3일 전", //Date 로직 작성 필요
                                content = feed.content.text,
                                onMoreClick = onClickMoreIcon,
                                commentCount = { commentCount.toString() },
                                onClickComment = {
                                    //navigate
                                },
                                likeCount = { likeCount.toString() },
                                isLike = { isLike },
                                onClickLike = {
                                    when (isLike) {
                                        true -> likeCount--
                                        false -> likeCount++
                                    }
                                    isLike = !isLike
                                },
                                images = feed.content.images.toPersistentList()
                            ),
                        )
                    }

                    FeedType.DuckDeal -> {
                        HomeDuckDealFeed(
                            FeedHolder(
                                profile = feed.writerId,
                                nickname = feed.writerId,
                                time = "3일 전", //Date 로직 작성 필요
                                content = feed.content.text,
                                onMoreClick = onClickMoreIcon,
                                commentCount = { commentCount.toString() },
                                onClickComment = {
                                    //navigate
                                },
                                likeCount = { likeCount.toString() },
                                isLike = { isLike },
                                onClickLike = {
                                    when (isLike) {
                                        true -> likeCount--
                                        false -> likeCount++
                                    }
                                    isLike = !isLike
                                },
                                images = feed.content.images.toPersistentList()
                            ),
                            DuckDealHolder(
                                tradingMethod = stringResource(
                                    id = getTradingMethodResourceId(
                                        feed.isDirectDealing!!,
                                        feed.parcelable!!
                                    )
                                ),
                                price = feed.price!!.priceToString(),
                                dealState = feed.dealState!!,
                                location = feed.location!!,
                            ),
                        )
                    }
                }
            }
        }
    }
    DuckieFab(
        items = homeFabMenuItems,
        expanded = fabExpanded,
        onFabClick = {
            fabExpanded = !fabExpanded
        },
        onItemClick = { index, item ->

        },
        paddingValues = homeFabPadding,
    )
}

@Composable
fun DrawerContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp,
        )
    ) {
        DrawerHeader()
        QuackDivider()
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(
                16.dp,
            )
        ) {
            QuackTitle2(
                text = stringResource(id = R.string.trading_activity)
            )
            DrawerIconText(
                icon = QuackIcon.Heart,
                text = stringResource(id = R.string.list_of_interests),
                onClick = {

                }
            )
            DrawerIconText(
                icon = QuackIcon.Sell,
                text = stringResource(id = R.string.sales_history),
                onClick = {

                }
            )
            DrawerIconText(
                icon = QuackIcon.Buy,
                text = stringResource(id = R.string.purchase_history),
                onClick = {

                }
            )
        }
        QuackDivider()
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(
                16.dp,
            )
        ) {
            QuackTitle2(
                text = stringResource(id = R.string.my_activity)
            )
            DrawerIconText(
                icon = QuackIcon.Area,
                text = stringResource(id = R.string.setting_up_areas_interest),
                onClick = {

                }
            )
            DrawerIconText(
                icon = QuackIcon.Tag,
                text = stringResource(id = R.string.edit_interest_tags),
                onClick = {

                }
            )
        }
        QuackDivider()
        DrawerIconText(
            modifier = Modifier.padding(
                start = 16.dp,
            ),
            icon = QuackIcon.Setting,
            text = stringResource(id = R.string.app_settings),
            onClick = {

            }
        )
    }
}

@Composable
private fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 20.dp,
                horizontal = 16.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(
            space = 12.dp,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable {

                }
                .padding(
                    vertical = 4.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                )
            ) {
                QuackRoundImage(
                    src = R.drawable.duckie_profile,
                    size = DpSize(
                        width = 48.dp,
                        height = 48.dp,
                    )
                )
                QuackHeadLine2(
                    text = "닉네임"
                )
            }
            QuackImage(
                src = QuackIcon.ArrowRight,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 42.dp,
            )
        ) {
            DrawerNumberText(number = "2.6만", text = stringResource(id = R.string.follower))
            DrawerNumberText(number = "167", text = stringResource(id = R.string.following))
            DrawerNumberText(number = "88", text = stringResource(id = R.string.feed))
        }
    }

}

@Composable
private fun DrawerNumberText(
    number: String,
    text: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            2.dp,
        )
    ) {
        QuackTitle2(
            text = number
        )
        QuackBody2(
            text = text,
        )
    }
}

@NonRestartableComposable
@Composable
private fun DrawerIconText(
    modifier: Modifier = Modifier,
    icon: QuackIcon,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .noRippleClickable(
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
        )
    ) {
        QuackImage(
            src = icon,
        )
        QuackTitle1(
            text = text
        )
    }
}
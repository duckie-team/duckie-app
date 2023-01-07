/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

@file:Suppress(
    "MaxLineLength",
    "MagicNumber"
) // TODO(limsaehyun): 더미데이터를 위해 임시로 구현, 추후에 삭제 필요

package team.duckie.app.android.data.recommendation.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import team.duckie.app.android.data.recommendation.mapper.toDomain
import team.duckie.app.android.data.recommendation.model.RecommendationsResponse
import team.duckie.app.android.domain.recommendation.model.RecommendationFeeds
import kotlin.math.max

private suspend fun dummyReturn(a: Int): RecommendationsResponse {
    delay(1000)
    val recommendations = (a..a + 10).map {
        RecommendationsResponse.Recommendation(
            title = "쿠키좀 구워봤어?\n#웹툰 퀴즈",
            tag = "#웹툰",
            exams = (0..3).map { exam ->
                RecommendationsResponse.Recommendation.Exam(
                    title = "문제$exam",
                    coverImg = "https://user-images.githubusercontent.com/80076029/206901501-8d8a97ea-b7d8-4f18-84e7-ba593b4c824b.png",
                    nickname = "user$it",
                    examineeNumber = it,
                    recommendId = it,
                )
            }
        )
    }
    val jumbotrons = (0..2).map {
        RecommendationsResponse.Jumbotron(
            title = "sad",
            coverUrl = "https://user-images.githubusercontent.com/80076029/206894333-d060111d-e78e-4294-8686-908b2c662f19.png",
            content = "이것은 content 인비다",
            buttonContent = "하고싶음하세요",
        )
    }
    return RecommendationsResponse(
        recommendations = recommendations,
        page = a,
        offset = 1,
        limit = 1,
        jumbotrons = jumbotrons,
    )
}

private const val STARTING_KEY = 1

class RecommendationPagingSource : PagingSource<Int, RecommendationFeeds.Recommendation>() {
    @Suppress("TooGenericExceptionCaught")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecommendationFeeds.Recommendation> {
        return try {
            val nextPageNumber = params.key ?: STARTING_KEY
            val response = dummyReturn(nextPageNumber) // TODO(limsaehyun): Server Request
            val range = nextPageNumber.until(nextPageNumber + params.loadSize)

            LoadResult.Page(
                data = response.recommendations.map { it.toDomain() },
                prevKey = when (nextPageNumber) {
                    STARTING_KEY -> null
                    else -> when (
                        val prevKey =
                            ensureValidKey(key = range.first - params.loadSize)
                    ) {
                        STARTING_KEY -> null
                        else -> prevKey
                    }
                },
                nextKey = range.last + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecommendationFeeds.Recommendation>): Int {
        return ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2)
            .coerceAtLeast(0)
    }

    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}
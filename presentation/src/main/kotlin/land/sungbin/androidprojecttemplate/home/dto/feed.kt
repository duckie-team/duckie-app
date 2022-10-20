package land.sungbin.androidprojecttemplate.home.dto

import land.sungbin.androidprojecttemplate.domain.model.Feed
import land.sungbin.androidprojecttemplate.domain.model.common.Content
import land.sungbin.androidprojecttemplate.domain.model.constraint.DealState
import land.sungbin.androidprojecttemplate.domain.model.constraint.FeedType
import java.text.NumberFormat
import java.util.Locale

sealed class FeedDTO(
    open val writerId: String,
    open val type: FeedType,
    open val content: Content,
    open val createdAt: String,
) {
    data class Normal(
        override val writerId: String,
        override val type: FeedType,
        override val content: Content,
        override val createdAt: String,
    ) : FeedDTO(writerId, type, content, createdAt)

    data class DuckDeal(
        override val writerId: String,
        override val type: FeedType,
        override val content: Content,
        override val createdAt: String,
        val title: String,
        val dealState: DealState,
        val price: Int,
        val location: String,
        val isDirectDealing: Boolean,
        val parcelable: Boolean,
    ) : FeedDTO(writerId, type, content, createdAt)
}

fun Feed.toNormalFeed() = FeedDTO.Normal(
    writerId = writerId,
    type = type,
    content = content,
    createdAt = "",
)

fun Feed.toDuckDealFeed() = FeedDTO.DuckDeal(
    writerId = writerId,
    type = type,
    content = content,
    createdAt = "",
    title = title!!,
    dealState = dealState!!,
    price = price!!,
    location = location!!,
    isDirectDealing = isDirectDealing!!,
    parcelable = parcelable!!,
)

private const val K = 1000
private const val M = 1000 * 1000

internal fun Int.toUnitString(): String =
    when (this) {
        in 0 until K -> {
            toString()
        }

        in K until M -> {
            (this / K).toString() + "k"
        }

        else -> {
            (this / M).toString() + "m"
        }
    }

internal fun Int.priceToString(): String =
    NumberFormat.getInstance(Locale.getDefault()).format(this)
package land.sungbin.androidprojecttemplate.domain.model

import androidx.annotation.Size
import land.sungbin.androidprojecttemplate.domain.model.constraint.DislikeReason
import land.sungbin.androidprojecttemplate.domain.model.constraint.LikeReason
import land.sungbin.androidprojecttemplate.domain.model.constraint.ReasonToken
import land.sungbin.androidprojecttemplate.domain.model.constraint.Review
import land.sungbin.androidprojecttemplate.domain.model.util.FK
import land.sungbin.androidprojecttemplate.domain.model.util.New
import land.sungbin.androidprojecttemplate.domain.model.util.PK
import land.sungbin.androidprojecttemplate.domain.model.util.requireInput
import land.sungbin.androidprojecttemplate.domain.model.util.requireSize

/**
 * 거래 후기 모델
 *
 * @param id 고유 아이디
 * @param buyerId 구매자 [유저 아이디][User.nickname]
 * @param sellerId 판매자 [유저 아이디][User.nickname]
 * @param feedId 해당 거래가 진행된 [덕피드 아이디][Feed.id]
 * @param isDirect 직거래인지 여부
 * @param review 거래에 대한 종합적인 리뷰
 * @param likeReason 좋았던 점 목록
 * @param dislikeReason 아쉬웠던 점 목록
 * @param etc 기타 소견. 기본값은 null 이며, 공백일 수 있습니다.
 */
data class DealReview(
    @PK val id: String,
    @FK val buyerId: String,
    @FK val sellerId: String,
    @FK val feedId: String,
    @New val isDirect: Boolean,
    val review: Review,
    @Size(min = 1) val likeReason: List<LikeReason>,
    @Size(min = 1) val dislikeReason: List<DislikeReason>,
    val etc: String? = null,
) {
    init {
        requireInput(
            field = "id",
            value = id,
        )
        requireInput(
            field = "buyerId",
            value = buyerId,
        )
        requireInput(
            field = "sellerId",
            value = sellerId,
        )
        requireInput(
            field = "feedId",
            value = feedId,
        )
        requireSize(
            min = 1,
            field = "likeReason",
            value = likeReason,
        )
        requireSize(
            min = 1,
            field = "dislikeReason",
            value = dislikeReason,
        )
        if (
            likeReason.any { it.token == ReasonToken.Buyer } &&
            likeReason.any { it.token == ReasonToken.Seller }
        ) {
            throw IllegalArgumentException("Buyer and Seller cannot be selected at the same time.")
        }
        if (
            dislikeReason.any { it.token == ReasonToken.Buyer } &&
            dislikeReason.any { it.token == ReasonToken.Seller }
        ) {
            throw IllegalArgumentException("Buyer and Seller cannot be selected at the same time.")
        }
    }
}


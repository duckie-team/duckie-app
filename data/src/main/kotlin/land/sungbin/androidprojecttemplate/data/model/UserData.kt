package land.sungbin.androidprojecttemplate.data.model

import land.sungbin.androidprojecttemplate.data.model.constraint.BadgesData
import land.sungbin.androidprojecttemplate.data.model.constraint.CategoriesData
import land.sungbin.androidprojecttemplate.data.model.constraint.CollectionsData
import land.sungbin.androidprojecttemplate.data.model.constraint.TagsData
import land.sungbin.androidprojecttemplate.domain.model.constraint.DuckTier

internal data class UserData(
    val nickname: String? = null,
    val accountAvailable: Boolean? = null,
    val profileUrl: String? = null,
    val tier: DuckTier? = null,
    val badges: BadgesData? = null,
    val likeCategories: CategoriesData? = null,
    val interestedTags: TagsData? = null,
    val nonInterestedTags: TagsData? = null,
    val notificationTags: TagsData? = null,
    val tradePreferenceTags: TagsData? = null,
    val collections: CollectionsData? = null,
    val createdAt: String? = null,
    val deletedAt: String? = null,
    val bannedAt: String? = null,
)

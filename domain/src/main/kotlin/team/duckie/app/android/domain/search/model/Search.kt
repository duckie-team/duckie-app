/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.domain.search.model

import androidx.compose.runtime.Immutable
import team.duckie.app.android.domain.exam.model.Exam
import team.duckie.app.android.domain.user.model.User
import team.duckie.app.android.util.kotlin.OutOfDateApi

@Immutable
sealed class Search(val type: String, open val page: Int) {
    companion object {
        const val Exam = "exam"
        const val User = "user"
    }

    @Immutable
    data class ExamSearch @OptIn(OutOfDateApi::class) constructor(
        val exams: List<Exam>,
        override val page: Int
    ) : Search(Exam, page)

    @Immutable
    data class UserSearch(
        val users: List<User>,
        override val page: Int
    ) : Search(User, page)
}
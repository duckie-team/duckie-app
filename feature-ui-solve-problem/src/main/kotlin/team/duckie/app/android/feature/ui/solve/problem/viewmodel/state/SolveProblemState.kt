/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.feature.ui.solve.problem.viewmodel.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import team.duckie.app.android.domain.examInstance.model.ProblemInstance

internal data class SolveProblemState(
    val examId: Int = -1,
    val isProblemsLoading: Boolean = true,
    val currentPageIndex: Int = 0,
    val problems: ImmutableList<ProblemInstance> = persistentListOf(),
    val inputAnswers: ImmutableList<InputAnswer> = persistentListOf(),
) {
    val totalPage: Int
        get() = problems.size
}

internal data class InputAnswer(
    val number: Int = -1,
    val answer: String = "",
)
/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.feature.exam.result.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import team.duckie.app.android.common.android.savedstate.getOrThrow
import team.duckie.app.android.common.android.ui.const.Extras
import team.duckie.app.android.domain.exam.model.ExamInstanceSubmit
import team.duckie.app.android.domain.exam.model.ExamInstanceSubmitBody
import team.duckie.app.android.domain.examInstance.usecase.MakeExamInstanceSubmitUseCase
import team.duckie.app.android.domain.quiz.usecase.GetQuizUseCase
import team.duckie.app.android.domain.quiz.usecase.SubmitQuizUseCase
import javax.inject.Inject

@HiltViewModel
class ExamResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val makeExamInstanceSubmitUseCase: MakeExamInstanceSubmitUseCase,
    private val submitQuizUseCase: SubmitQuizUseCase,
    private val getQuizUseCase: GetQuizUseCase,
) : ViewModel(),
    ContainerHost<ExamResultState, ExamResultSideEffect> {

    override val container: Container<ExamResultState, ExamResultSideEffect> = container(
        ExamResultState.Loading,
    )

    fun initState() {
        val examId = savedStateHandle.getOrThrow<Int>(Extras.ExamId)
        val isQuiz = savedStateHandle.getOrThrow<Boolean>(Extras.IsQuiz)
        if (isQuiz) {
            val updateQuizParam =
                savedStateHandle.getOrThrow<SubmitQuizUseCase.Param>(Extras.UpdateQuizParam)
            updateQuiz(
                examId = examId,
                updateQuizParam = updateQuizParam,
            )
        } else {
            val submitted = savedStateHandle.getOrThrow<Array<String>>(Extras.Submitted)
            getReport(
                examId = examId,
                submitted = ExamInstanceSubmitBody(
                    submitted = submitted.toList().toImmutableList(),
                ),
            )
        }
    }

    private fun getReport(
        examId: Int,
        submitted: ExamInstanceSubmitBody,
    ) = intent {
        reduce {
            ExamResultState.Loading
        }
        makeExamInstanceSubmitUseCase(
            id = examId,
            body = submitted,
        ).onSuccess { submit: ExamInstanceSubmit ->
            reduce {
                ExamResultState.Success(
                    reportUrl = submit.examScoreImageUrl,
                    isQuiz = false,
                )
            }
        }.onFailure {
            it.printStackTrace()
            reduce {
                ExamResultState.Error(exception = it)
            }
            postSideEffect(ExamResultSideEffect.ReportError(it))
        }
    }

    private fun updateQuiz(
        examId: Int,
        updateQuizParam: SubmitQuizUseCase.Param,
    ) = intent {
        reduce {
            ExamResultState.Loading
        }
        submitQuizUseCase(examId, updateQuizParam).onFailure {
            it.printStackTrace()
            reduce {
                ExamResultState.Error(exception = it)
            }
            postSideEffect(ExamResultSideEffect.ReportError(it))
        }
        getQuizUseCase(examId).onSuccess { quizResult ->
            val isPerfectScore = quizResult.wrongProblem == null
            reduce {
                with(quizResult) {
                    ExamResultState.Success(
                        examId = id,
                        reportUrl = if (isPerfectScore) {
                            exam.perfectScoreImageUrl ?: ""
                        } else {
                            wrongProblem?.solution?.solutionImageUrl ?: ""
                        },
                        isQuiz = true,
                        correctProblemCount = correctProblemCount,
                        time = time,
                        mainTag = exam.mainTag?.name ?: "",
                        ranking = ranking ?: 0,
                        wrongAnswerMessage = wrongProblem?.solution?.wrongAnswerMessage ?: "",
                        requirementPlaceholder = exam.requirementPlaceholder ?: "",
                        requirementQuestion = exam.requirementQuestion ?: "",
                        timer = exam.timer ?: 0,
                    )
                }
            }
        }.onFailure {
            it.printStackTrace()
            reduce {
                ExamResultState.Error(exception = it)
            }
            postSideEffect(ExamResultSideEffect.ReportError(it))
        }
    }

    fun clickRetry() = intent {
        val state = state as ExamResultState.Success
        postSideEffect(
            ExamResultSideEffect.NavigateToStartExam(
                examId = state.examId,
                requirementQuestion = state.requirementQuestion,
                requirementPlaceholder = state.requirementPlaceholder,
                timer = state.timer,
            ),
        )
    }

    fun exitExam() = intent {
        postSideEffect(ExamResultSideEffect.FinishExamResult)
    }
}
/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

@file:Suppress("unused")

package team.duckie.app.android.domain.exam.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class Problem(
    val question: Question,
    val answer: Answer,
    val correctAnswer: String,
    val hint: String?,
    val memo: String?,
)

@Immutable
sealed class Question(val type: String, open val text: String) {
    private companion object {
        const val Text = "text"
        const val Image = "image"
        const val Audio = "audio"
        const val Video = "video"
    }

    @Immutable
    data class Text(override val text: String) : Question(type = Text, text = text)

    @Immutable
    data class Image(override val text: String, val imageUrl: String) : Question(
        type = Image,
        text = text,
    )

    @Immutable
    data class Audio(override val text: String, val audioUrl: String) : Question(
        type = Audio,
        text = text,
    )

    @Immutable
    data class Video(override val text: String, val videoUrl: String) : Question(
        type = Video,
        text = text,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Question) return false

        if (type != other.type) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }
}

@Immutable
sealed class Answer(val type: String) {
    private companion object {
        const val ShortAnswer = "shortAnswer"
        const val Choice = "choice"
        const val ImageChoice = "imageChoice"
    }

    @Immutable
    class Short(val answer: String) : Answer(type = ShortAnswer)

    @Immutable
    class Choice(val choices: ImmutableList<ChoiceModel>) : Answer(type = Choice)

    @Immutable
    class ImageChoice(val imageChoice: ImmutableList<ImageChoiceModel>) : Answer(type = ImageChoice)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Answer) return false

        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }
}

@JvmInline
@Immutable
value class ChoiceModel(val text: String)

@Immutable
data class ImageChoiceModel(
    val text: String,
    val imageUrl: String,
)
/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.domain.auth.usecase

import androidx.compose.runtime.Immutable
import javax.inject.Inject
import team.duckie.app.android.domain.auth.repository.AuthRepository

@Immutable
class CheckAccessTokenUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(token: String): Result<Int> { // 유저 아이디 반환
        return runCatching {
            repository.checkAccessToken(token).userId
        }
    }
}

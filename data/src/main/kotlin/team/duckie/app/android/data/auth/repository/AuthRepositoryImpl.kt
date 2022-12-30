/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.data.auth.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import javax.inject.Inject
import team.duckie.app.android.data._exception.util.responseCatching
import team.duckie.app.android.data._util.jsonBody
import team.duckie.app.android.data.auth.mapper.toDomain
import team.duckie.app.android.data.auth.model.AccessTokenCheckResponseData
import team.duckie.app.android.data.auth.model.AuthJoinResponseData
import team.duckie.app.android.domain.auth.model.AccessTokenCheckResponse
import team.duckie.app.android.domain.auth.model.JoinResponse
import team.duckie.app.android.domain.auth.repository.AuthRepository

class AuthRepositoryImpl @Inject constructor(
    private val client: HttpClient,
) : AuthRepository {
    override suspend fun join(kakaoOAuthToken: String): JoinResponse {
        val response = client.post("/auth/kakao") {
            jsonBody {
                "code" withString kakaoOAuthToken
            }
        }
        return responseCatching(
            response = response.body(),
            parse = AuthJoinResponseData::toDomain,
        )
    }

    override suspend fun checkAccessToken(): AccessTokenCheckResponse {
        val response = client.get("/auth/token")
        return responseCatching(
            response = response.body(),
            parse = AccessTokenCheckResponseData::toDomain,
        )
    }
}
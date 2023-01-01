/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.ktor.client.plugin

import io.ktor.client.plugins.api.createClientPlugin

object DuckieAuthorizationHeader {
    internal var accessToken: String? = null

    fun updateAccessToken(token: String) {
        accessToken = token
    }

    fun resetAccessToken() {
        accessToken = null
    }
}

val DuckieAuthorizationHeaderOrNothingPlugin = createClientPlugin(
    name = "DuckieAuthorizationHeaderOrNothing",
) {
    DuckieAuthorizationHeader.accessToken?.let { accessToken ->
        onRequest { request, _ ->
            request.headers.append("X-DUCKIE-AUTHORIZATION", accessToken)
        }
    }
}
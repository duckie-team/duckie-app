/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.data._exception.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import team.duckie.app.android.data._exception.model.ExceptionBody
import team.duckie.app.android.data._exception.model.throwing
import team.duckie.app.android.data._util.jsonMapper
import team.duckie.app.android.util.kotlin.ApiErrorThreshold
import team.duckie.app.android.util.kotlin.toDuckieStatusCode

@Suppress("TooGenericExceptionCaught")
internal suspend inline fun <reified DataModel, DomainModel> responseCatching(
    response: HttpResponse,
    parse: (body: DataModel) -> DomainModel,
): DomainModel {
    if (response.status.value < ApiErrorThreshold) {
        val body: DataModel = response.body()
        return parse(body)
    } else {
        val statusCode = response.status.value
        response.body<ExceptionBody>()
            .copy(statusCode = statusCode.toDuckieStatusCode())
            .throwing()
    }
}

// TODO(riflockle7): statusCode 에 따라 에러 핸들링 또는 데이터 반환하도록 해주어야 함
@Suppress("TooGenericExceptionCaught")
internal inline fun <DomainModel> responseCatching(
    statusCode: Int,
    response: String,
    parse: (body: String) -> DomainModel,
): DomainModel {
    if (statusCode < ApiErrorThreshold) {
        return parse(response)
    } else {
        jsonMapper.readValue(response, ExceptionBody::class.java)
            .copy(statusCode = statusCode.toDuckieStatusCode())
            .throwing()
    }
}

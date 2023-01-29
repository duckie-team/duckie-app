/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package team.duckie.app.android.util.kotlin

/**
 * 덕키 API 에서 발생하는 [Exception] 을 정의합니다.
 *
 * 백엔드의 [에러 처리 규칙](https://www.notion.so/jisungbin/6487a9d604e14375bc02df6fd8397f15)에 따라 [code] 를 필수로 받습니다.
 *
 * @param code 예외 코드
 */
sealed class DuckieException(code: String) : IllegalStateException("DuckieException: $code")

/** 서버 응답 StatusCode */
enum class DuckieStatusCode(val statusCode: Int) {
    Unknown(-1),
    BadRequest(400),
    UnAuthorized(401),
    Forbidden(403),
    NotFound(404),
    InternalServer(500);
}

/** [Int] -> [DuckieStatusCode] */
fun Int.toDuckieStatusCode(): DuckieStatusCode = when (this) {
    400 -> DuckieStatusCode.BadRequest
    401 -> DuckieStatusCode.UnAuthorized
    403 -> DuckieStatusCode.Forbidden
    404 -> DuckieStatusCode.NotFound
    500 -> DuckieStatusCode.InternalServer
    else -> DuckieStatusCode.Unknown
}

/**
 * 덕키 API 의 response 가 정상적으로 처리되지 않았을 때 [DuckieException] 을 나타냅니다.
 *
 * 백엔드의 [에러 처리 규칙](https://www.notion.so/jisungbin/6487a9d604e14375bc02df6fd8397f15) 을 따릅니다.
 *
 * @param serverMessage 예외 메시지. 선택으로 값을 받습니다.
 * @param code 예외 코드. 필수로 값을 받습니다.
 * @param errors 발생한 에러 목록. 선택으로 값을 받습니다
 */
class DuckieResponseException(
    serverMessage: String? = null,
    val statusCode: DuckieStatusCode,
    val code: String,
    val errors: List<String>? = null,
) : DuckieException(code) {
    override val message = "DuckieResponseException: $code".runIf(serverMessage != null) { plus(" - $serverMessage") }
    override fun toString(): String {
        return "DuckieApiException(message=$message, statusCode=$statusCode, code=$code, errors=$errors, throwable=$throwable)"
    }
}

fun duckieResponseException(
    statusCode: DuckieStatusCode,
    serverMessage: String? = null,
    code: String,
    errors: List<String>? = null,
): Nothing {
    throw DuckieResponseException(
        statusCode = statusCode,
        serverMessage = serverMessage,
        code = code,
        errors = errors,
    )
}

/**
 * 덕키 API 의 response 에서 필수 필드가 null 로 나왔을 때 [DuckieException] 을 나타냅니다.
 *
 * @param field 누락된 필수 필드명
 */
class DuckieResponseFieldNPE(field: String) : DuckieException(code = "") {
    override val message = "필수 필드가 response 에 누락되었습니다. 누락된 필드명: $field"
}

fun duckieResponseFieldNpe(field: String): Nothing {
    throw DuckieResponseFieldNPE(field = field)
}

/**
 * 클라이언트 내부 오류를 표현하고 싶을 때 나타냅니다.
 *
 * @param message 예외 메시지. 선택으로 값을 받습니다.
 * @param code 예외 코드. 필수로 값을 받습니다.
 * @param errors 발생한 에러 목록. 선택으로 값을 받습니다
 */
class DuckieClientLogicProblemException(
    message: String? = null,
    val code: String,
    val errors: List<String>? = null,
) : DuckieException(code) {
    val originalMessage = message
    override val message = "DuckieResponseException: $code".runIf(message != null) { plus(" - $message") }
    override fun toString(): String {
        return "DuckieApiException(message=$message, code=$code, errors=$errors)"
    }
}

fun duckieClientLogicProblemException(
    message: String? = null,
    code: String = "client_logic_problem",
    errors: List<String>? = null,
): Nothing {
    throw DuckieClientLogicProblemException(
        message = message,
        code = code,
        errors = errors,
    )
}

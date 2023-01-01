/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

package team.duckie.app.android.data._util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

internal val jsonMapper = ObjectMapper()

/**
 * 모든 필드가 String 일 때만 사용해야 합니다.
 * 그렇지 않으면 역직렬화 에러가 발생합니다.
 */
internal fun String.toStringJsonMap(): Map<String, String?> {
    return jsonMapper.readValue(this, object : TypeReference<Map<String, String?>>() {})
}
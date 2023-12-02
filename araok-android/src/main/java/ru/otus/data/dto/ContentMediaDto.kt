package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.ContentMedia

@JsonClass(generateAdapter = true)
data class ContentMediaDto(
    @Json(name = "contentMediaId") override val contentMediaId: ContentMediaIdDto,
    @Json(name = "media") override val media: ByteArray
): ContentMedia {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContentMediaDto

        if (contentMediaId != other.contentMediaId) return false
        if (!media.contentEquals(other.media)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contentMediaId.hashCode()
        result = 31 * result + media.contentHashCode()
        return result
    }
}

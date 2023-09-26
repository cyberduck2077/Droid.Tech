package ru.data.common.models.network

import ru.data.common.models.local.maps.TypeReason

data class NetworkModelPost(
    val attachments: List<NetworkModelAttachment>,
    val created: Long?,
    val id: Int,
    val is_vote: Boolean?,
   val polling: Any?,
    val published: Int?,
    val text: String?,
    val user: NetworkModelUser,
    val votes_count: Int
) {
    fun getUrlAttachments() = this.attachments.map { it.url }

}

data class NetworkModelPostWithComment(
    val attachments: List<NetworkModelAttachment>,
    val created: Long?,
    val id: Int,
    val is_vote: Boolean?,
    val polling: NetworkModelPolling?,
    val published: Int?,
    val text: String?,
    val user: NetworkModelUser,
    val votes_count: Int,
    val topic: NetworkModelTopic?,
    val comments_count: Int,
    val comment: NetworkModelComment?,
)

data class CreatingPostReport(
    val reason: Int,
    val additional_text: String,
) {
    constructor(
        reason: TypeReason,
        additional_text: String
    ) : this(
        reason = reason.ordinal,
        additional_text = additional_text
    )
}

data class NetworkModelPolling(
    val title: String?,
    val description: String?,
    val started: Long?,
    val ended: Long?,
    val id: Int,
    val created: Long?,
    val image: String?,
    val answers: List<NetworkModelAnswer>,
)

data class NetworkModelAnswer(
    val id: Int,
    val text: String,
    val selected: Boolean?,
    val result_count: Int?,
)

data class NetworkModelComment(
    val text: String,
    val id: Int,
    val parent_id: Int?,
    val created: Long,
    val user: NetworkModelUser,
    val children_count: Int,
)

data class IsVoteBody(
    val is_vote: Boolean
)

data class CreatingComment(
    val text: String,
    val parent_id: Int?,
)

data class NetworkModelTopic(
    val name: String,
    val id: Int,
)

data class UpdatingComment(
    val text: String,
)

data class UpdatingAttachment(
    val name: String?,
    val is_favorite: Boolean?,
    val lat: Double?,
    val lon: Double?,
)

data class NetworkModelAttachment(
    val name: String,
    val is_favorite: Boolean,
    val lat: Int?,
    val lon: Int?,
    val id: Int,
    val extension: String?,
    val url: String,
    val first_frame: String?,
    val duration: String?,
    val created: Long,
    val happened: Int?,
    val address: String?,
    val size: Int,
    val owner: NetworkModelUser?,
    val is_video: Boolean = false,
)

data class CreatingPost(
    val text: String?,
    val published: Int?,
    val attachment_ids: List<Int>?,
    val media_ids: List<Int>?,
    val polling_id: Int?,
    val topic_id: Int?,
)

data class CreatingMediaFromAttachment(
    val album_id: Int?,
)




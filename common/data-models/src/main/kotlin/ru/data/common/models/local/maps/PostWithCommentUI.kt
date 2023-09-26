package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelPostWithComment
import ru.data.common.models.util.millDateDDMMYYYY

data class PostWithCommentUI(
    val attachments: List<AttachmentUI>,
    val created: Long?,
    val createdHuman: String = created?.millDateDDMMYYYY() ?: "",
    val id: Int,
    val isVote: Boolean?,
    val polling: PollingUI?,
    val published: Int?,
    val text: String?,
    val user: UserUI,
    val votesCount: Int,
    val topic: TopicUI?,
    val commentsCount: Int,
    val comment: CommentUI?,
) {

    companion object {
        fun mapFrom(
            post: NetworkModelPostWithComment,
            locationDao: ((Int) -> City?)?,
        ) = PostWithCommentUI(
            attachments = post.attachments.map { attach ->
                AttachmentUI.mapFrom(
                    attach = attach,
                    locationDao = locationDao
                )
            },
            created = post.created?.times(1000),
            id = post.id,
            isVote = post.is_vote,
            polling = post.polling?.let { PollingUI.mapFrom(it) },
            published = post.published,
            text = post.text,
            user = UserUI.mapFrom(
                user = post.user,
                locationDao = locationDao
            ),
            votesCount = post.votes_count,
            topic = post.topic?.let { TopicUI.mapFrom(it) },
            commentsCount = post.comments_count,
            comment = post.comment?.let { CommentUI.mapFore(it, locationDao = locationDao) },
            )
    }

    fun getUrlAttachments() = this.attachments.map { it.url }

    fun getCountCommentText(): String {
        return when (commentsCount) {
            0 -> "комментариев нет"
            1 -> "$commentsCount комментарий"
            in 1..4 -> "$commentsCount комментария"
            else -> "$commentsCount комментариев"
        }
    }
}

package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelPost
import ru.data.common.models.util.millDateDDMMYYYY


class PostUI(
    val attachments: List<AttachmentUI>,
    val created: Long?,
    val createdHuman: String = created?.millDateDDMMYYYY() ?: "",
    val id: Int,
    val isVote: Boolean?,
    val polling: Any?,
    val published: Int?,
    val text: String?,
    val user: UserUI,
    val votesCount: Int
) {
    companion object {
        fun mapFrom(
            post: NetworkModelPost,
            locationDao: ((Int) -> City?)?,
        ) = PostUI(
            attachments = post.attachments.map { attach ->
                AttachmentUI.mapFrom(
                    attach = attach,
                    locationDao = locationDao
                )
            },
            created = post.created?.times(1000),
            id = post.id,
            isVote = post.is_vote,
            polling = post.polling,
            published = post.published,
            text = post.text,
            user = UserUI.mapFrom(
                user = post.user,
                locationDao = locationDao
            ),
            votesCount = post.votes_count

        )
    }
}
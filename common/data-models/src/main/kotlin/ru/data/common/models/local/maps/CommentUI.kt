package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelComment
import ru.data.common.models.util.millDateDDMMYYYY
import ru.data.common.models.util.millDateDDMMYYYYHHMM

data class CommentUI(
    val text: String,
    val id: Int,
    val parentId: Int?,
    val created: Long,
    val createdHuman: String = created.millDateDDMMYYYY(),
    val createdHumanHours: String = created.millDateDDMMYYYYHHMM(),
    val user: UserUI,
    val childrenCount: Int,
) {

    companion object {
        fun mapFore(
            comment: NetworkModelComment,
            locationDao: ((Int) -> City?)?,
        ) = CommentUI(
            text = comment.text,
            id = comment.id,
            parentId = comment.parent_id,
            created = comment.created.times(1000),
            user = UserUI.mapFrom(
                user = comment.user,
                locationDao = locationDao),
            childrenCount = comment.children_count
        )
    }


}
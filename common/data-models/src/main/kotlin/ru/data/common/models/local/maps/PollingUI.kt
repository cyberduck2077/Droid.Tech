package ru.data.common.models.local.maps

import ru.data.common.models.network.NetworkModelPolling
import ru.data.common.models.util.millDateDDMMYYYY

data class PollingUI(
    val title: String?,
    val description: String?,
    val started: Long?,
    val startedHuman: String = started?.millDateDDMMYYYY() ?:"",
    val ended: Long?,
    val endedHuman: String = ended?.millDateDDMMYYYY() ?:"",
    val id: Int,
    val created: Long?,
    val createdHuman: String = created?.millDateDDMMYYYY() ?:"",
    val image: String?,
    val answers: List<AnswerUI>,

) {
    companion object{
        fun mapFrom(polling: NetworkModelPolling)= PollingUI(
            title =polling.title,
            description =polling.description,
            started =polling.started?.times(1000),
            ended =polling.ended?.times(1000),
            id =polling.id,
            created = polling.created?.times(1000),
            image =polling.image,
            answers = polling.answers.map { AnswerUI.mapFrom(it) }
        )
    }
}
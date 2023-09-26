package ru.data.common.models.local.maps

import ru.data.common.models.network.NetworkModelAnswer

data class AnswerUI(
    val id: Int,
    val text: String,
    val selected: Boolean?,
    val resultCount: Int?,
) {
    companion object {
        fun mapFrom(answer: NetworkModelAnswer) = AnswerUI(
            id = answer.id ,
            text = answer.text ,
            selected = answer.selected ,
            resultCount = answer.result_count )


    }

}
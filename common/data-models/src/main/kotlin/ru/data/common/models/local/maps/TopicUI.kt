package ru.data.common.models.local.maps

import ru.data.common.models.network.NetworkModelTopic

data class TopicUI(
    val name: String,
    val id: Int,
) {
    companion object {
        fun mapFrom(topic: NetworkModelTopic) = TopicUI(
            name = topic.name,
            id = topic.id)
    }
}
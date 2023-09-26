package ru.data.common.models.local.maps

import ru.data.common.models.network.NetworkModelInterest

data class InterestUI(
    val name: String,
    val id: Int,
) {
    companion object {
        fun mapFrom(interest: NetworkModelInterest) = InterestUI(name = interest.name, id = interest.id)
    }

}

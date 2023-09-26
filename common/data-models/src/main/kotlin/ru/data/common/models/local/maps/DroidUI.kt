package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelDroid

data class DroidUI(
    val id: Int,
    val name: String?,
    val members: List<DroidMemberUI>,
) {
    companion object {
        fun mapFrom(
            Droid: NetworkModelDroid,
            locationDao:((Int)-> City?)?,
        ) = DroidUI(
            id = Droid.id,
            name = Droid.name,
            members = Droid.members.map { member ->
                DroidMemberUI.mapFrom(
                    member = member,
                    locationDao = locationDao,
                )
            }
        )
    }

    fun getNameDroid(): String {
        this.name?.let { return it }
        val member = this.members.firstOrNull { it.userRole == RoleDroid.SELF }
        return "${member?.firstName ?: ""}#${member?.id ?: ""}"
    }
}
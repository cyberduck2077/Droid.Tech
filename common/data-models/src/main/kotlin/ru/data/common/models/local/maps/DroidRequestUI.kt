package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelDroidRequest


class DroidRequestUI(
    val id: Int,
    val isApproved: Boolean?,
    val user: UserUI?,
){
    companion object{
        fun mapFrom(
            Droid: NetworkModelDroidRequest,
            locationDao: ((Int) -> City?)?) =DroidRequestUI(
            id = Droid.id,
            isApproved = Droid.is_approved,
            user = Droid.user?.let { userIt ->
                UserUI.mapFrom(
                    user = userIt,
                    locationDao = locationDao)
            }
        )
    }
}
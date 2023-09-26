package ru.data.common.domain.use_case

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import ru.data.common.models.local.maps.DroidRequestUI
import ru.data.common.models.logger.LogCustom.logD
import ru.data.common.network.api.ApiRequestsDroid

class UseCaseMembershipDroid(
    private val apiRequestsDroid: ApiRequestsDroid,
    private val daoLocation: UseCaseLocations,
) {


    suspend fun postCollectivesRequests(
        DroidId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: (DroidRequestUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val response = apiRequestsDroid.postCollectivesRequests(DroidId)
        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }


        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            return
        }
        logD("postCollectivesRequests", response)
        response.data?.let { newData ->
            val data = DroidRequestUI.mapFrom(
                Droid = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
            flowSuccess.invoke(data)
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

}
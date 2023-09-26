package ru.data.common.domain.use_case

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import ru.data.common.models.local.maps.DroidRequestUI
import ru.data.common.models.local.maps.RoleDroid
import ru.data.common.models.res.TextApp
import ru.data.common.network.api.ApiRequestsDroid

class UseCaseRequestsDroid(
    private val apiRequestsDroid: ApiRequestsDroid,
    private val daoLocation: UseCaseLocations,
) {

    suspend fun getRequestsDroid(
        DroidId: Int,
        userId: Int? = null,
        page: Int = 1,
        listStatus: (
            hasNext: Boolean?,
            hasPrev: Boolean?,
            codeResponse: Int,
        ) -> Unit
    ): List<DroidRequestUI> {
        val response = apiRequestsDroid.getCollectivesRequests(
            DroidId = DroidId,
            page = page,
            userId = userId,
        )

        listStatus.invoke(
            response.meta?.paginator?.has_next,
            response.meta?.paginator?.has_prev,
            response.getCodeResponse()
        )

        return response.data?.map { resp ->
            DroidRequestUI.mapFrom(
                Droid = resp,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
        } ?: listOf()
    }

    suspend fun putCollectivesRequest(
        requestId: Int,
        DroidRole: RoleDroid?,
        isApproved: Boolean,
        flowStart: () -> Unit = {},
        flowSuccess: (DroidRequestUI?) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val response = apiRequestsDroid.putCollectivesRequestApproved(
            requestId = requestId,
            isApproved = isApproved,
            DroidRole = DroidRole?.ordinal,
        )

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            return
        }

        flowSuccess.invoke(response.data?.let {
            DroidRequestUI.mapFrom(
                Droid = it,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
        })
        flowMessage.invoke(response.getDescriptionRudApi())
        flowStop.invoke()
    }
}
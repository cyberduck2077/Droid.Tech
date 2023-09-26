package ru.data.common.domain.use_case

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.data.common.db.CityDaoRoom
import ru.data.common.models.local.maps.UserUI
import ru.data.common.network.api.ApiUsers

class UseCaseUsers(
    private val apiUser: ApiUsers,
    private val daoLocation: UseCaseLocations,
) {

    suspend fun getUsers(
        page: Int? = null,
        name: String? = null,
        fullName: String? = null,
        gender: Int? = null,
        ageFrom: Long? = null,
        ageTo: Long? = null,
        inCurrentCollectives: Boolean? = null,
        DroidIds: List<Int>? = null,
        locationIds: List<Int>? = null,
        birthLocationIds: List<Int>? = null,
        listStatus: (
            hasNext: Boolean?,
            hasPrev: Boolean?,
            codeResponse: Int,
        ) -> Unit
    ): List<UserUI> {

        val response = apiUser.getUsers(
            name = name,
            page = page,
            fullName = fullName,
            gender = gender,
            ageFrom = ageFrom,
            ageTo = ageTo,
            inCurrentCollectives = inCurrentCollectives,
            DroidIds = DroidIds,
            locationIds = locationIds,
            birthLocationIds = birthLocationIds
        )

        listStatus.invoke(
            response.meta?.paginator?.has_next,
            response.meta?.paginator?.has_prev,
            response.getCodeResponse()
        )

        return response.data?.map { user ->
            UserUI.mapFrom(
                user = user,
                locationDao = {
                  val scope = CoroutineScope(Dispatchers.IO).async {
                      daoLocation.getDdCity(it)
                  }
                   runBlocking { scope.await() }
                })
        } ?: listOf()
    }


    suspend fun postFriendship(
        userId: Int,
        text: String,
        flowStart: () -> Unit = {},
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},

        ) {
        flowStart.invoke()
        val data = apiUser.postFriendship(userId = userId, text = text)

        if (data.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }
        flowMessage.invoke(data.getDescriptionRudApi())
        flowStop.invoke()
    }

    suspend fun getUser(
        userId: Int,
        flowStart: () -> Unit = {},
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
        flowSuccess: (UserUI) -> Unit,
    ) {
        flowStart.invoke()
        val data = apiUser.getUser(userId = userId)

        if (data.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }

        if (!data.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }
        data.data?.let { newData ->
            flowSuccess.invoke(
                UserUI.mapFrom(
                    user = newData,
                    locationDao = { runBlocking { daoLocation.getDdCity(it) } })
            )
        } ?: run {
            flowMessage.invoke(data.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

    suspend fun getUserExistsEmail(
        email: String,
        flowStart: () -> Unit = {},
        flowStop: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ): Boolean {
        flowStart.invoke()
        val data = apiUser.getUserExists(email = email)

        if (!data.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return false
        }

        data.data?.let { newData ->
            flowStop.invoke()
            return newData.exists
        } ?: run {
            flowMessage.invoke(data.getDescriptionRudApi())
        }
        flowStop.invoke()
        return false
    }

}
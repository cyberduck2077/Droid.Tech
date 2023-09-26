package ru.data.common.domain.use_case

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import ru.data.common.ds.FileStoreApp
import ru.data.common.models.local.maps.DroidMemberUICreating
import ru.data.common.models.local.maps.DroidUI
import ru.data.common.models.logger.LogCustom.logE
import ru.data.common.models.network.CreatingDroidMember
import ru.data.common.models.res.TextApp
import ru.data.common.network.api.ApiDroid
import ru.data.common.network.api.ApiDroidMembers

class UseCaseDroid(
    private val apiDroid: ApiDroid,
    private val apiDroidMembers: ApiDroidMembers,
    private val dataStore: FileStoreApp,
    private val daoLocation: UseCaseLocations,
) {

    fun getUser() = dataStore.UserData().get()

    suspend fun getMyDroid(
        flowStart: () -> Unit = {},
        flowSuccess: (List<DroidUI>) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiDroid.getCollectives()

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getMyDroid", response)
            return
        }

        response.data?.let { newData ->
            val data = newData.map { mapping ->
                DroidUI.mapFrom(
                    Droid = mapping,
                    locationDao = { runBlocking { daoLocation.getDdCity(it) } }
                )
            }
            flowSuccess.invoke(data)
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getMyDroid", response)
        }
        flowStop.invoke()
    }


    suspend fun getDroidId(
        id: Int,
        flowStart: () -> Unit = {},
        flowSuccess: (DroidUI) -> Unit,
        flowStop: () -> Unit = {},
        flowError: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiDroid.getDroidId(id)

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getDroidId", response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(DroidUI.mapFrom(
                Droid = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            ))
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getDroidId", response)
        }
        flowStop.invoke()
    }

    suspend fun postNewDroidAndListMember(
        listMembers: List<DroidMemberUICreating>,
        flowStart: () -> Unit = {},
        flowSuccess: (DroidUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val newDroid = apiDroid.postCreateDroid(null)

        if (newDroid.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(newDroid.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }

        if (!newDroid.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(newDroid.getDescriptionRudApi())
            logE("postNewDroidAndListMember", newDroid)
            return
        }
        val idDroid = newDroid.data?.id ?: run {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textSomethingWentWrong)
            return
        }

        listMembers.forEach { DroidMember ->
            val response = apiDroidMembers.postCreateDroidMember(
                DroidId = idDroid,
                DroidMember = CreatingDroidMember.mapFrom(DroidMember)
            )

            if (!response.status.isSuccess()) {
                flowMessage.invoke(response.getDescriptionRudApi())
                logE("postNewDroidAndListMember", response)
                flowStop.invoke()
                return
            }
        }

        val Droid = apiDroid.getDroidId(DroidId = idDroid)
        if (!Droid.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(Droid.getDescriptionRudApi())
            logE("postNewDroidAndListMember", Droid)
            return
        }

        Droid.data?.let { newData ->
            flowSuccess.invoke(DroidUI.mapFrom(
                Droid = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            ))
        } ?: run {
            flowMessage.invoke(Droid.getDescriptionRudApi())
            logE("postNewDroidAndListMember", Droid)
        }
        flowStop.invoke()
    }


    suspend fun updateDroidAndListMember(
        idDroid: Int,
        listMembers: List<DroidMemberUICreating>,
        flowStart: () -> Unit = {},
        flowSuccess: (DroidUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        listMembers.forEach { DroidMember ->
            if (DroidMember.id == 0) {
                val responseNewMember = apiDroidMembers.postCreateDroidMember(
                    DroidId = idDroid,
                    DroidMember = CreatingDroidMember.mapFrom(DroidMember)
                )

                if (!responseNewMember.status.isSuccess()) {
                    flowMessage.invoke(responseNewMember.getDescriptionRudApi())
                    logE("updateDroidAndListMember", responseNewMember)
                    return
                }
            }

            if (DroidMember.id > 0) {
                val responseUpdateMember = apiDroidMembers.putUpdateDroidMember(
                    memberId = DroidMember.id,
                    DroidMember = CreatingDroidMember.mapFrom(DroidMember)
                )
                if (!responseUpdateMember.status.isSuccess()) {
                    flowMessage.invoke(responseUpdateMember.getDescriptionRudApi())
                    logE("updateDroidAndListMember", responseUpdateMember)
                    return
                }
            }
        }

        val Droid = apiDroid.getDroidId(DroidId = idDroid)

        if (Droid.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(Droid.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }
        if (!Droid.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(Droid.getDescriptionRudApi())
            logE("updateDroidAndListMember", Droid)
            return
        }

        Droid.data?.let { newData ->
            flowSuccess.invoke(DroidUI.mapFrom(
                Droid = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            ))
        } ?: run {
            flowMessage.invoke(Droid.getDescriptionRudApi())
            logE("updateDroidAndListMember", Droid)
        }
        flowStop.invoke()
    }

    suspend fun deleteDroidFromId(
        idDroid: Int,
        flowStart: () -> Unit = {},
        flowSuccess: () -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val response = apiDroid.deleteDroidFromId(DroidId = idDroid)

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
        flowMessage.invoke(response.getDescriptionRudApi())
        flowSuccess.invoke()
        flowStop.invoke()
    }
}
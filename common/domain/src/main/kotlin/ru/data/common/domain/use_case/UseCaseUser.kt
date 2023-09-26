package ru.data.common.domain.use_case

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import ru.data.common.db.CityDaoRoom
import ru.data.common.ds.FileStoreApp
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.local.maps.UserUIUpdating
import ru.data.common.models.network.UpdatingUser
import ru.data.common.network.api.ApiDroid
import ru.data.common.network.api.ApiUser
import java.io.File

class UseCaseUser(
    private val apiUser: ApiUser,
    private val apiDroid: ApiDroid,
    private val dataStore: FileStoreApp,
    private val daoLocation: UseCaseLocations,
) {

    fun getUserLocalData() = dataStore.UserData().get()
    fun getChooseDroidId() = dataStore.Droid().getId()
    fun setChooseDroidId(id: Int) = dataStore.Droid().update { id }
    fun setTokenUser(token: String) = dataStore.TokenData().putServer(token)

    suspend fun getMe(
        flowStart: () -> Unit = {},
        flowSuccess: (UserUI) -> Unit,
        flowStop: () -> Unit = {},
        flowError: () -> Unit,
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiUser.getMe()

        if (data.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }

        if (!data.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            flowError.invoke()
            return
        }
        data.data?.let { newData ->
            val userUi = UserUI.mapFrom(
                user = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } },
            )
            updateLocalUserData(userUi)
            flowSuccess.invoke(userUi)
        } ?: run {
            flowMessage.invoke(data.getDescriptionRudApi())
        }
        flowStop.invoke()
    }


    suspend fun putPassword(
        password: String,
        flowStart: () -> Unit = {},
        flowSuccess: (UserUI) -> Unit,
        flowStop: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val response = apiUser.putPassword(password = password)
        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            return
        }
        response.data?.let { newData ->
            val userUi = UserUI.mapFrom(
                user = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } },
            )
            updateLocalUserData(userUi)
            flowSuccess.invoke(userUi)
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

    suspend fun postMyAvatar(
        uri: String,
        compressedFile: (String) -> File,
        flowStart: () -> Unit = {},
        flowSuccess: (UserUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val compressedImageFile = compressedFile.invoke(uri)
        val data = apiUser.postMyAvatar(file = compressedImageFile)

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
            val userUi = UserUI.mapFrom(
                user = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } },
            )
            updateLocalUserData(userUi)
            flowSuccess.invoke(userUi)
        } ?: run {
            flowMessage.invoke(data.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

    suspend fun putMe(
        user: UserUIUpdating,
        flowStart: () -> Unit = {},
        flowSuccess: (UserUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val response = apiUser.putMe(user = UpdatingUser.mapFrom(user))
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
        response.data?.let { newData ->
            val userUi = UserUI.mapFrom(
                user = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } },
            )
            updateLocalUserData(userUi)
            flowSuccess.invoke(userUi)
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

    suspend fun postMyTel(
        tel: String,
        code: String,
        flowStart: () -> Unit = {},
        flowSuccess: () -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiUser.postMyTel(
            tel = tel,
            code = code,
        )

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
        flowSuccess.invoke()
        flowStop.invoke()
    }


    private suspend fun updateLocalUserData(
        newData: UserUI
    ) {
        dataStore.UserData().update { newData }
        if (dataStore.Droid().getId() == null) {
            apiDroid.getCollectives().data?.let { Droid ->
                dataStore.Droid().update {
                    Droid.firstOrNull()?.id
                }
            }
        }
    }
}
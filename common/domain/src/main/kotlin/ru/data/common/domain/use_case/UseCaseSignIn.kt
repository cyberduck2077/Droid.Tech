package ru.data.common.domain.use_case

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import ru.data.common.ds.FileStoreApp
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.network.VerificationCode
import ru.data.common.network.api.ApiDroid
import ru.data.common.network.api.ApiSignIn

class UseCaseSignIn(
    private val apiSignIn: ApiSignIn,
    private val apiDroid: ApiDroid,
    private val dataStore: FileStoreApp,
    private val daoLocation: UseCaseLocations,
) {

    fun getUserLocalData() = dataStore.UserData().get()

    suspend fun postAuthorization(
        email: String,
        password: String,
        flowStart: () -> Unit = {},
        flowSuccess: (UserUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiSignIn.postSignIn(email = email, password = password)

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
            dataStore.TokenData().putServer(newData.token)
            val userUi = UserUI.mapFrom(
                user = newData.user,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
            updateLocalUserData(userUi)
            flowSuccess.invoke(userUi)
        } ?: run {
            flowMessage.invoke(data.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

    suspend fun postVerificationTel(
        tel: String,
        flowStart: () -> Unit = {},
        flowSuccess: (String) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiSignIn.postVerificationTel(tel = tel)

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
            flowSuccess.invoke(newData.code)
        } ?: run {
            flowMessage.invoke(data.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

    suspend fun postReg(
        email: String,
        password: String,
        code: String,
        flowStart: () -> Unit = {},
        flowSuccess: (UserUI) -> Unit,
        flowStop: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiSignIn.postRegApi(email = email, password = password, code = code)

        if (data.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }

        if (!data.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }
        data.data?.let { newData ->
            dataStore.TokenData().putServer(newData.token)
            val userUi = UserUI.mapFrom(
                user = newData.user,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
            updateLocalUserData(userUi)
            flowSuccess.invoke(userUi)
        } ?: run {
            flowMessage.invoke(data.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

    suspend fun postEmail(
        email: String,
        flowStart: () -> Unit = {},
        flowSuccess: (VerificationCode) -> Unit,
        flowStop: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiSignIn.postEmailApi(email = email)

        if (data.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }

        if (!data.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }
        data.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowMessage.invoke(data.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

    suspend fun postRegApi(
        email: String,
        code: String,
        flowStart: () -> Unit = {},
        flowSuccess: (String) -> Unit,
        flowStop: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val response = apiSignIn.postSignInOtp(
            email = email,
            code = code
        )

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            return
        }
        response.data?.let { newData ->
            flowSuccess.invoke(newData.token)
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
        }
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
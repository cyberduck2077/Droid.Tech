package ru.data.common.ds

import androidx.datastore.core.DataStoreFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import ru.data.common.ds.di.ProvideFile
import ru.data.common.models.local.LocalFileValue
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.network.NetworkModelUser

class FileStoreApp(private val provideFile: ProvideFile) {

    private val dataStore = DataStoreFactory.create(
        produceFile = { provideFile.getFileMain() },
        serializer = LocalFileSerializer(provideFile.getFileCrypto())
    )

    inner class TokenData {
        fun putServer(token: String) = updateLocalData { it.copy(token = token) }
        fun getServer() = getLocalData().token

        fun putFirebase(token: String) = updateLocalData { it.copy(tokenFirebase = token) }
        fun getFirebase() = getLocalData().tokenFirebase
    }

    inner class Droid {
        fun delete() = updateLocalData { it.copy(chooserDroidId = null) }
        fun getId(): Int? = getLocalData().chooserDroidId
        fun update(onUpdate: (Int?) -> Int?) {
            updateLocalData { localData ->
                localData.copy(
                    chooserDroidId = onUpdate.invoke(localData.chooserDroidId)
                )
            }
        }
    }

    inner class UserData {
        fun delete() = updateLocalData { it.copy(user = UserUI()) }
        fun get(): UserUI = getLocalData().user ?: UserUI()
        fun update(onUpdate: (UserUI) -> UserUI) {
            updateLocalData { localData ->
                val user = onUpdate.invoke(localData.user ?: UserUI())
                localData.copy(user = user)
            }
        }
    }

    fun getLocalData(): LocalFileValue = runBlocking {
        dataStore.data.first()
    }

    fun updateLocalData(
        onUpdate: (LocalFileValue) -> LocalFileValue
    ) = runBlocking {
        dataStore.updateData { onUpdate.invoke(it) }
    }

    fun deleteAppSettings() {
        updateLocalData { LocalFileValue() }
    }
}


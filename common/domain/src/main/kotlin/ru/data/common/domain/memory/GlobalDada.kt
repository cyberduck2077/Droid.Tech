@file:OptIn(kotlinx.coroutines.DelicateCoroutinesApi::class)

package ru.data.common.domain.memory

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.data.common.models.local.DataForPostingMedia
import ru.data.common.models.local.DataSingleLive
import ru.data.common.models.local.EventProject
import ru.data.common.models.local.screens.ScreensHome

var GlobalDada = SingleLiveEvent<DataSingleLive>()

fun gDSetScreenMain(screen: ScreensHome) {
    funInScope {
        GlobalDada.value = GlobalDada.value?.copy(screen = screen)
            ?: DataSingleLive().copy(screen = screen)
    }
}

fun gDMessage(text: String?) {
    funInScope {
        GlobalDada.value =
            GlobalDada.value?.copy(messageSnack = EventProject(text))
                ?: DataSingleLive().copy(messageSnack = EventProject(text))
    }
}

fun gDSetListImage(listImageForUpload: List<DataForPostingMedia>) {
    funInScope {
        GlobalDada.value = GlobalDada.value?.copy(listImageForUpload = listImageForUpload)
            ?: DataSingleLive().copy(listImageForUpload = listImageForUpload)
    }
}

fun gDSetLoader(isLoad: Boolean, isInfinity: Boolean = false) {
    funInScope {
        GlobalDada.value = GlobalDada.value?.copy(
            isLoad = isLoad,
            isInfinity = isInfinity
        )
            ?: DataSingleLive().copy(
                isLoad = isLoad,
                isInfinity = isInfinity
            )
    }
}

fun gDLoaderStart() {
    funInScope {
        GlobalDada.value = GlobalDada.value?.copy(isLoad = true)
            ?: DataSingleLive().copy(isLoad = true)
    }
}

fun gDLoaderStop() {
    funInScope {
        GlobalDada.value = GlobalDada.value?.copy(isLoad = false)
            ?: DataSingleLive().copy(isLoad = false)
    }
}

private fun funInScope(unit: () -> Unit) {
    GlobalScope.launch launchMain@ {
        withContext(Dispatchers.Main) {
            unit.invoke()
            this@launchMain.cancel()
        }
    }
}
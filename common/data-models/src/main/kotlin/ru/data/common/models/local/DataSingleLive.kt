package ru.data.common.models.local

import ru.data.common.models.local.screens.ScreensHome

data class DataSingleLive(
    val isLoad: Boolean = false,
    val isInfinity: Boolean = false,
    val messageSnack: EventProject<String?> = EventProject(null),
    val screen: ScreensHome = ScreensHome.RIBBON_SCREEN,
    val listImageForUpload: List<DataForPostingMedia> = listOf()
)




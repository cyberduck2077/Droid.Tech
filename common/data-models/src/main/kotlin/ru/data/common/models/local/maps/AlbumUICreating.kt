package ru.data.common.models.local.maps

import ru.data.common.models.util.millDateDDMMYYYY

data class AlbumUICreating(
    val name: String?,
    val description: String?,
    val location: String?,
    val customDate: Long?,
    val customDateHuman: String = customDate?.millDateDDMMYYYY() ?:"",
    val isPrivate: Boolean = false,
) {
}
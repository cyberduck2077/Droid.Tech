package ru.data.common.models.local.maps

import ru.data.common.models.res.TextApp
import ru.data.common.models.util.millDateDDMMYYYY

data class DroidMemberUICreating(
    val id: Int = 0,
    val firstName: String? = null,
    val lastName: String? = null,
    val patronymic: String? = null,
    val birthdate: Long? = null,
    val birthdateHuman: String = birthdate?.millDateDDMMYYYY() ?:"" ,
    val maidenName: String? = null,
    val gender: Gender? = null,
    val role: RoleDroid? = null,
) {

    companion object {
        fun mapFrom(members: List<DroidMemberUI>) = members.map {
                DroidMemberUICreating(
                    id = it.id,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    patronymic = it.patronymic,
                    birthdate = it.birthdate,
                    maidenName = it.maidenName,
                    gender = it.gender,
                    role = it.userRole,
                )
            }
    }

    fun getFullName(): String {
        var fullName = ""
        val addStr: (String) -> Unit = {
            fullName += if (fullName.isNotEmpty()) " $it" else it
        }
        this.firstName?.let(addStr)
        this.lastName?.let(addStr)
        this.patronymic?.let(addStr)
        this.maidenName?.let(addStr)
        return fullName
    }


    fun isFullForeSend() =
        !firstName.isNullOrEmpty()
                && !lastName.isNullOrEmpty()
                && birthdate != null
                && gender != null
                && role != null


    fun getEnterDataYourSatellite() =
        when (this.role) {
            RoleDroid.SPOUSE  -> this.gender?.getGenderTextShort()
            RoleDroid.CHILD  -> TextApp.textChildren
            RoleDroid.SELF   -> TextApp.formatSomethingYou(gender?.getGenderTextShort())
            RoleDroid.PARENT -> TextApp.textGrandParent
            RoleDroid.SIBLING -> TextApp.textBrotherSister
            else -> ""
        }


}
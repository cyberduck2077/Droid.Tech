package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelDroidMember
import ru.data.common.models.util.millDateDDMMYYYY

class DroidMemberUI(
    val id: Int,
    val firstName: String?,
    val lastName: String?,
    val patronymic: String?,
    val birthdate: Long?,
    val birthdateHuman: String = birthdate?.millDateDDMMYYYY() ?: "",
    val maidenName: String?,
    val gender: Gender?,
    val user: UserUI?,
    val ownerRole: RoleDroid?,
    val userRole: RoleDroid?,
) {
    companion object {
        fun mapFrom(
            member: NetworkModelDroidMember,
            locationDao:((Int)-> City?)?,
        ) = DroidMemberUI(
            id = member.id,
            firstName = member.user?.first_name,
            lastName = member.user?.last_name,
            patronymic = member.user?.patronymic,
            birthdate = member.user?.birthdate?.times(1000),
            maidenName = member.user?.maiden_name,
            gender = member.user?.gender?.let { Gender.getEnum(it) },
            user = member.user?.let {
                UserUI.mapFrom(
                    user = it,
                    locationDao = locationDao,
                )
            },
            ownerRole = member.owner_role?.let { RoleDroid.getEnum(it) },
            userRole = member.user_role?.let { RoleDroid.getEnum(it) }

        )
    }



    fun mapToDroidMemberUICreating() = DroidMemberUICreating(
        id = this.id ,
        firstName = this.firstName ,
        lastName = this.lastName ,
        patronymic = this.patronymic ,
        birthdate = this.birthdate ,
        maidenName = this.maidenName ,
        gender = this.gender ,
        role = this.userRole

    )


    fun getNameAndLastName(): String {
        var fullName = ""
        val addStr: (String) -> Unit = {
            fullName += if (fullName.isNotEmpty()) " $it" else it
        }
        this.firstName?.let(addStr)
        this.lastName?.let(addStr)
        return fullName
    }

}

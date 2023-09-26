package ru.data.common.models.network

import ru.data.common.models.local.maps.DroidMemberUICreating

data class NetworkModelDroidRequest(
    val id: Int,
    val is_approved: Boolean?,
    val Droid_role: Int?,
    val user: NetworkModelUser?,
)

data class UpdatingDroidRequest(
    val is_approved: Boolean,
    val Droid_role: Int?,
)

data class BodyAny(
    val any: Any? = null
)

data class UpdatingDroid(
    val name: String?
)

data class NetworkModelDroid(
    val id: Int,
    val name: String?,
    val members: List<NetworkModelDroidMember>,
)

data class NetworkModelDroidMember(
    val id: Int,
    val user: NetworkModelUser?,
    val owner_role: Int?,
    val user_role: Int?,
)

data class CreatingDroidMember(
    val id: Int = 0,
    val first_name: String? = null,
    val last_name: String? = null,
    val patronymic: String? = null,
    val birthdate: Long? = null,
    val maiden_name: String? = null,
    val gender: Int? = null,
    val role: Int? = null,
) {
    companion object {
        fun mapFrom(member: DroidMemberUICreating) = CreatingDroidMember(
            id = member.id,
            first_name = member.firstName,
            last_name = member.lastName,
            patronymic = member.patronymic,
            birthdate = member.birthdate?.div(1000),
            maiden_name = member.maidenName,
            gender = member.gender?.ordinal,
            role = member.role?.ordinal
        )
    }
}

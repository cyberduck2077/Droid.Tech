package ru.data.common.models.network

import kotlinx.serialization.Serializable
import ru.data.common.models.local.maps.UserUIUpdating

@Serializable
data class NetworkModelUser(
    val friendship_status: Int? = null,
    val friendship_description: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val patronymic: String? = null,
    val maiden_name: String? = null,
    val gender: Int? = null,
    val tg: String? = null,
    val status: String? = null,
    val birthdate: Long? = null,
    val description: String? = null,
    val work: String? = null,
    val favorite_music: String? = null,
    val favorite_movies: String? = null,
    val favorite_books: String? = null,
    val favorite_games: String? = null,
    val interests: List<String> = listOf(),
    val bio: String? = null,
    val id: Int = 0,
    val email: String? = null,
    val registration_age: Long? = null,
    val registration_date: Long? = null,
    val tel: String? = null,
    val is_active: Boolean? = null,
    val is_superuser: Boolean? = null,
    val last_visit: Long? = null,
    val location_id: Int? = null,
    val birth_location_id: Int? = null,
    val avatar: String? = null,
    val is_hidden: Boolean? = null,
)

data class UpdatingUser(
    val email: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val patronymic: String? = null,
    val maiden_name: String? = null,
    val gender: Int? = null,
    val tg: String? = null,
    val tel: String? = null,
    val status: String? = null,
    val birthdate: Long? = null,
    val description: String? = null,
    val work: String? = null,
    val favorite_music: String? = null,
    val favorite_movies: String? = null,
    val favorite_books: String? = null,
    val favorite_games: String? = null,
    val interests: List<String>? = null,
    val location_id: Int? = null,
    val birth_location_id: Int? = null,
) {
    companion object {
        fun mapFrom(user: UserUIUpdating) = UpdatingUser(
            email = user.email,
            first_name = user.firstName,
            last_name = user.lastName,
            patronymic = user.patronymic,
            maiden_name = user.maidenName,
            gender = user.gender?.ordinal,
            tg = user.tg,
            tel = user.tel,
            status = user.status,
            birthdate = user.birthdate?.div(1000),
            description = user.description,
            work = user.work,
            favorite_music = user.favoriteMusic,
            favorite_movies = user.favoriteMovies,
            favorite_books = user.favoriteBooks,
            favorite_games = user.favoriteGames,
            interests = user.interests,
            location_id = user.location?.id,
            birth_location_id = user.birthLocation?.id,
        )
    }
}

data class NetworkModelLocation(
    val name: String,
    val id: Int,
)

data class LoginData(
    val email: String,
    val password: String,
)

data class CreatingEmailVerificationCode(
    val email: String,
)

data class VerifyingTelCode(
    val tel: String,
    val code: String,
)

data class CreatingTelVerificationCode(
    val tel: String,
)

data class NetworkModelTelVerificationCode(
    val code: String,
)

data class VerifyingEmailCode(
    val email: String,
    val code: String,
)

data class SignUpEmail(
    val email: String,
    val password: String,
    val code: String,
)

data class VerificationCode(
    val code: String?,
)

data class NetworkModelInterest(
    val name: String,
    val id: Int,
)

data class NetworkModelTokenWithUser(
    val user: NetworkModelUser,
    val token: String,
)

data class NetworkModelNotification(
    val id: Int,
    val icon: String?,
    val title: String?,
    val body: String?,
    val created: Long?,
    val order_id: Int?,
    val offer_id: Int?,
    val is_read: Boolean?,
)

data class ExistsResponse(
    val exists: Boolean,
)

data class DescriptionBody(
    val description: String,
)

data class IsAcceptedBody(
    val is_accepted: Boolean,
)

data class PasswordBody(
    val password: String,
)




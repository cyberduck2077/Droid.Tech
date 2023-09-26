package ru.data.common.models.local.maps

import kotlinx.serialization.Serializable
import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelUser
import ru.data.common.models.network.UpdatingUser
import ru.data.common.models.util.FieldValidators
import ru.data.common.models.util.millDateDDMMYYYY

@Serializable
data class UserUI(
    val id: Int = 0,
    val friendshipStatus: FriendshipStatuses = FriendshipStatuses.getStatus(),
    val friendshipDescription: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val patronymic: String? = null,
    val maidenName: String? = null,
    val gender: Gender? = null,
    val tg: String? = null,
    val status: String? = null,
    val birthdate: Long? = null,
    val birthdateHuman: String = birthdate?.millDateDDMMYYYY() ?: "",
    val description: String? = null,
    val work: String? = null,
    val favoriteMusic: String? = null,
    val favoriteMovies: String? = null,
    val favoriteBooks: String? = null,
    val favoriteGames: String? = null,
    val interests: List<String> = listOf(),
    val bio: String? = null,
    val email: String? = null,
    val registrationAge: Long? = null,
    val registrationAgeHuman: String? = registrationAge?.millDateDDMMYYYY() ?: "",
    val registrationDate: Long? = null,
    val registrationDateHuman: String? = registrationDate?.millDateDDMMYYYY() ?: "",
    val lastVisit: Long? = null,
    val lastVisitHuman: String? = lastVisit?.millDateDDMMYYYY() ?: "",
    val tel: String? = null,
    val isActive: Boolean = false,
    val isSuperuser: Boolean = false,
    val location: City? = null,
    val birthLocation: City? = null,
    val avatar: String? = null,
    val isHidden: Boolean = false,
) {

    companion object {
        fun mapFrom(
            user: NetworkModelUser,
            locationDao:((Int)-> City?)?,
        ) = UserUI(
            friendshipStatus = FriendshipStatuses.getStatus(user.friendship_status),
            friendshipDescription = user.friendship_description,
            firstName = user.first_name,
            lastName = user.last_name,
            patronymic = user.patronymic,
            maidenName = user.maiden_name,
            gender = user.gender?.let { Gender.getEnum(it) },
            tg = user.tg,
            status = user.status,
            birthdate = user.birthdate?.times(1000),
            description = user.description,
            work = user.work,
            favoriteMusic = user.favorite_music,
            favoriteMovies = user.favorite_movies,
            favoriteBooks = user.favorite_books,
            favoriteGames = user.favorite_games,
            interests = user.interests,
            bio = user.bio,
            id = user.id,
            email = user.email,
            registrationAge = user.registration_age?.times(1000),
            registrationDate = user.registration_date?.times(1000),
            tel = user.tel,
            isActive = user.is_active ?: false,
            isSuperuser = user.is_superuser ?: false,
            lastVisit = user.last_visit?.times(1000),
            location = user.location_id?.let {  locationDao?.invoke(it)} ,
            birthLocation = user.birth_location_id?.let {  locationDao?.invoke(it)} ,
            avatar = user.avatar,
            isHidden = user.is_hidden ?: false
        )
    }

    fun mapInUpdatingUserUI() = UserUIUpdating(
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        patronymic = this.patronymic,
        maidenName = this.maidenName,
        gender = this.gender,
        tg = this.tg,
        tel = this.tel,
        status = this.status,
        birthdate = this.birthdate,
        description = this.description,
        work = this.work,
        favoriteMusic = this.favoriteMusic,
        favoriteMovies = this.favoriteMovies,
        favoriteBooks = this.favoriteBooks,
        favoriteGames = this.favoriteGames,
        interests = this.interests,
        location = this.location,
        birthLocation = this.birthLocation,
    )


    fun getUpdatingUser() = UpdatingUser(
        email = this.email,
        first_name = this.firstName,
        last_name = this.lastName,
        patronymic = this.patronymic,
        maiden_name = this.maidenName,
        gender = this.gender?.ordinal,
        tg = this.tg,
        tel = this.tel,
        status = this.status,
        birthdate = this.birthdate,
        description = this.description,
        work = this.work,
        favorite_music = this.favoriteMusic,
        favorite_movies = this.favoriteMovies,
        favorite_books = this.favoriteBooks,
        favorite_games = this.favoriteGames,
        interests = this.interests,
        location_id = this.location?.id,
        birth_location_id = this.birthLocation?.id,
    )

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

    fun getNameAndLastName(): String {
        var fullName = ""
        val addStr: (String) -> Unit = {
            fullName += if (fullName.isNotEmpty()) " $it" else it
        }
        this.firstName?.let(addStr)
        this.lastName?.let(addStr)
        return fullName
    }

    fun stepRegStatus() = when {
        !isStepOneSuccess() -> StepNotEnoughReg.STEP_ONE_NOT_ENOUGH
        !isStepTwoSuccess() -> StepNotEnoughReg.STEP_TWO_NOT_ENOUGH
//        !isStepThreeSuccess() -> StepNotEnoughReg.STEP_THIRD_NOT_ENOUGH
//        !isStepForthSuccess() -> StepNotEnoughReg.STEP_FORTH_NOT_ENOUGH
        else                -> StepNotEnoughReg.STEP_SUCCESS
    }

    fun isStepOneSuccess(
    ) = this.email != null && FieldValidators.isValidEmail(this.email)

    fun isStepForthSuccess(
    ) = this.interests.isNotEmpty() && !description.isNullOrEmpty()

    fun isStepTwoSuccess(
    ) = this.firstName != null && this.lastName != null && this.gender != null

    fun isStepThreeSuccess() = this.birthdate != null

}
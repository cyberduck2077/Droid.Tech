package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.util.millDateDDMMYYYY

data class UserUIUpdating(
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val patronymic: String? = null,
    val maidenName: String? = null,
    val gender: Gender? = null,
    val tg: String? = null,
    val tel: String? = null,
    val status: String? = null,
    val birthdate: Long? = null,
    val description: String? = null,
    val work: String? = null,
    val favoriteMusic: String? = null,
    val favoriteMovies: String? = null,
    val favoriteBooks: String? = null,
    val favoriteGames: String? = null,
    val interests: List<String>? = null,
    val location: City? = null,
    val birthLocation: City? = null,
) {
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

    fun birthdateHuman() = this.birthdate?.millDateDDMMYYYY() ?: ""

    fun getUpdatingUser(user: UserUI) = UserUIUpdating(
        email = if (user.email != this.email) this.email else null,
        firstName = if (user.firstName != this.firstName) this.firstName else null,
        lastName = if (user.lastName != this.lastName) this.lastName else null,
        patronymic = if (user.patronymic != this.patronymic) this.patronymic else null,
        maidenName = if (user.maidenName != this.maidenName) this.maidenName else null,
        gender = if (user.gender != this.gender) this.gender else null,
        tg = if (user.tg != this.tg) this.tg else null,
        tel = if (user.tel != this.tel) this.tel else null,
        status = if (user.status != this.status) this.status else null,
        birthdate = if (user.birthdate != this.birthdate) this.birthdate else null,
        description = if (user.description != this.description) this.description else null,
        work = if (user.work != this.work) this.work else null,
        favoriteMusic = if (user.favoriteMusic != this.favoriteMusic) this.favoriteMusic else null,
        favoriteMovies = if (user.favoriteMovies != this.favoriteMovies) this.favoriteMovies else null,
        favoriteBooks = if (user.favoriteBooks != this.favoriteBooks) this.favoriteBooks else null,
        favoriteGames = if (user.favoriteGames != this.favoriteGames) this.favoriteGames else null,
        interests = if (user.interests != this.interests) this.interests else null,
        location = if (user.location?.id != this.location?.id) this.location else null,
        birthLocation = if (user.birthLocation?.id != this.birthLocation?.id) this.birthLocation else null,
    )
}
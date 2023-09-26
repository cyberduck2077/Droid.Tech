package ru.droid.tech.screens.module_main.profile_redaction

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import cafe.adriel.voyager.core.model.coroutineScope
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDSetLoader
import ru.data.common.domain.use_case.UseCaseDroid
import ru.data.common.domain.use_case.UseCaseInterests
import ru.data.common.domain.use_case.UseCaseLocations
import ru.data.common.domain.use_case.UseCaseSignIn
import ru.data.common.domain.use_case.UseCaseUser
import ru.data.common.models.data_base.City
import ru.data.common.models.local.maps.DroidMemberUICreating
import ru.data.common.models.local.maps.Gender
import ru.data.common.models.local.maps.InterestUI
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.local.maps.UserUIUpdating
import ru.data.common.models.res.TextApp

class ProfileRedactionModel(
    private val apiUser: UseCaseUser,
    private val apiSignIn: UseCaseSignIn,
    private val apiLocation: UseCaseLocations,
    private val apiInterests: UseCaseInterests,
    private val apiDroid: UseCaseDroid,
    private val context: Context
) : BaseModel() {

    private var jobSearch: Job = Job()

    private val _userData = MutableStateFlow(apiUser.getUserLocalData())
    val userData = _userData.asStateFlow()

    private val _interests = MutableStateFlow(listOf<InterestUI>())
    val interests = _interests.asStateFlow()

    private val _locationBirth = MutableStateFlow<City?>(null)
    val locationBirth = _locationBirth.asStateFlow()

    private val _locationResidence = MutableStateFlow<City?>(null)
    val locationResidence = _locationResidence.asStateFlow()

    private val _location = MutableStateFlow(listOf<City>())
    val location = _location.asStateFlow()

    private val _DroidError = MutableStateFlow(false)
    val DroidError = _DroidError.asStateFlow()

    private val _DroidMembers = MutableStateFlow(listOf<DroidMemberUICreating>())
    val DroidMembers = _DroidMembers.asStateFlow()

    private val _newPhone = MutableStateFlow<String?>(null)
    val newPhone = _newPhone.asStateFlow()

    private val _codeVerificationPhone = MutableStateFlow<String?>(null)
    val codeVerificationPhone = _codeVerificationPhone.asStateFlow()

    init {

        getInterests()
        getLocation()
    }

    fun getMe() = coroutineScope.launch(Dispatchers.IO) {
        apiUser.getMe(
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                userUpdateAndGetLocations(it)
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message,
            flowError = { }

        )
    }

    fun getInterests() = coroutineScope.launch(Dispatchers.IO) {
        _interests.value = apiInterests.getInterests(page = null, name = null)
    }

    fun getLocation() = coroutineScope.launch(Dispatchers.IO) {
        _location.value = apiLocation.getDdCities()
    }

    fun getDroid() = coroutineScope.launch(Dispatchers.IO) {
        apiUser.getChooseDroidId()?.let {
            apiDroid.getDroidId(
                id = it,
                flowStart = {
                    _DroidError.value = false
                    gDLoaderStart()
                },
                flowSuccess = { Droid ->
                    gDSetLoader(false)
                    _DroidMembers.value = Droid.members.map { member ->
                        member.mapToDroidMemberUICreating()
                    }
                },
                flowError = {
                    _DroidError.value = true
                },
                flowUnauthorized = {
                    getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
                },
                flowStop = ::gDLoaderStop,
                flowMessage = ::message,
            )
        } ?: run {
            _DroidError.value = true
        }
    }

    fun uploadPhoto(image: Uri) = coroutineScope.launch(Dispatchers.IO) {
        apiUser.postMyAvatar(
            uri = image.toString(),
            flowStart = ::gDLoaderStart,
            compressedFile = {
                runBlocking {
                    Compressor.compress(context, it.toUri().toFile()) {
                        default(width = 1024)
                    }
                }
            },
            flowSuccess = {
                gDSetLoader(false)
                userUpdateAndGetLocations(it)
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun onSearchLocation(searchCity: String?) {
        jobSearch.cancel()
        jobSearch = coroutineScope.launch(Dispatchers.IO) {
            _location.value = apiLocation.getDdCities(searchCity)
            delay(500)
        }
    }

    fun saveDroid(
        list: List<DroidMemberUICreating>
    ) = coroutineScope.launch(Dispatchers.IO) {
        apiUser.getChooseDroidId()?.let { DroidId ->
            apiDroid.updateDroidAndListMember(
                idDroid = DroidId,
                listMembers = list,
                flowStart = ::gDLoaderStart,
                flowSuccess = { Droid ->
                    gDSetLoader(false)
                    _DroidMembers.value = Droid.members.map { member ->
                        member.mapToDroidMemberUICreating()
                    }
                    getMe()
                },
                flowStop = ::gDLoaderStop,
                flowUnauthorized = {
                    getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
                },
                flowMessage = ::message,
            )
        }
    }

    fun deleteMember(member: DroidMemberUICreating) = coroutineScope.launch(Dispatchers.IO) {
        apiDroid.deleteDroidFromId(
            idDroid = member.id,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                _DroidMembers.value = listOf()
                getDroid()
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message,
        )
    }

    fun dismissDialogEnterCode() {
        _newPhone.value = null
        _codeVerificationPhone.value = null
    }

    fun userUpdateAndGetLocations(user: UserUI) = coroutineScope.launch(Dispatchers.IO) {
        _userData.value = user
    }

    fun saveContacts(
        locationBirth: City?,
        locationResidence: City?,
        tg: String?,
        tel: String?,
    ) = coroutineScope.launch(Dispatchers.IO) {

        val user = UserUIUpdating().copy(
            tg = tg,
            birthLocation = locationResidence,
            location = locationBirth
        ).getUpdatingUser(userData.value)

        apiUser.putMe(
            user = user,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                message(TextApp.textDataUpdated)
                gDSetLoader(false)
                userUpdateAndGetLocations(it)
                if (!tel.isNullOrEmpty() && userData.value.tel != tel) {
                    _newPhone.value = tel
                    getVerificationCode(tel)
                }
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun getVerificationCode(tel: String) = coroutineScope.launch(Dispatchers.IO) {
        apiSignIn.postVerificationTel(
            tel = tel,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                gDSetLoader(false)
                _codeVerificationPhone.value = it
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun sendNewPhone(
        tel: String,
        code: String
    ) = coroutineScope.launch(Dispatchers.IO) {
        if (code.isEmpty() || code != codeVerificationPhone.value) {
            message(TextApp.textIncorrectCode)
            return@launch
        }

        if (tel.isEmpty()) {
            message(TextApp.textSomethingWentWrong)
            return@launch
        }

        apiUser.postMyTel(
            tel = tel,
            code = code,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                gDSetLoader(false)
                dismissDialogEnterCode()
                message(TextApp.textPhoneUpdate)
                getMe()
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun updateMyPersonData(
        firstName: String?,
        lastName: String?,
        patronymic: String?,
        maidenName: String?,
        gender: Gender?,
        birthdateInMillis: Long?,
    ) = coroutineScope.launch(Dispatchers.IO) {

        val user = UserUIUpdating().copy(
            firstName = firstName,
            lastName = lastName,
            patronymic = patronymic,
            maidenName = maidenName,
            gender = gender,
            birthdate = birthdateInMillis
        ).getUpdatingUser(userData.value)

        apiUser.putMe(
            user = user,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                message(TextApp.textDataUpdated)
                gDSetLoader(false)
                userUpdateAndGetLocations(it)
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun updateInterests(
        description: String?,
        interests: List<String>?
    ) = coroutineScope.launch(Dispatchers.IO) {
        val user = UserUIUpdating().copy(
            description = description,
            interests = interests?.ifEmpty { null },
        ).getUpdatingUser(userData.value)

        apiUser.putMe(
            user = user,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                message(TextApp.textDataUpdated)
                gDSetLoader(false)
                userUpdateAndGetLocations(it)
            },
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowStop = ::gDLoaderStop,
            flowMessage = ::message
        )
    }

    fun goToPersonalData() {
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileRedactionPersonalDataScreen())
    }

    fun goToInterestsData() {
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileRedactionInterestsScreen())
    }

    fun goToDroidData() {
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileRedactionDroidScreen())
    }

    fun goToContactsData() {
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileRedactionContactsScreen())
    }

}
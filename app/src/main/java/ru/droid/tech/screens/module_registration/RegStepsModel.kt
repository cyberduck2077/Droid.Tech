package ru.droid.tech.screens.module_registration

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import cafe.adriel.voyager.core.model.coroutineScope
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.droid.tech.base.BaseModel
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDSetLoader
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_registration.step_3.StepThirdScreen
import ru.droid.tech.screens.module_registration.step_5.StepFifthScreen
import ru.droid.tech.screens.module_registration.step_5_join_cell_1.StepFifthJoinInCellFirst
import ru.droid.tech.screens.module_registration.step_5_join_cell_2.StepFifthJoinInCellSecond
import ru.droid.tech.screens.module_registration.step_5_new_cell_1.StepFifthCreateNewCellFirst
import ru.droid.tech.screens.module_registration.step_5_new_cell_2.StepFifthCreateNewCellSecond
import ru.droid.tech.screens.module_registration.step_5_new_cell_3.StepFifthCreateNewCellThird
import ru.droid.tech.screens.splash.SplashScreen
import ru.data.common.domain.use_case.UseCaseDroid
import ru.data.common.domain.use_case.UseCaseInterests
import ru.data.common.domain.use_case.UseCaseLocations
import ru.data.common.domain.use_case.UseCaseMembershipDroid
import ru.data.common.domain.use_case.UseCaseUser
import ru.data.common.models.data_base.City
import ru.data.common.models.local.maps.DroidMemberUICreating
import ru.data.common.models.local.maps.DroidRequestUI
import ru.data.common.models.local.maps.Gender
import ru.data.common.models.local.maps.InterestUI
import ru.data.common.models.local.maps.RoleDroid
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.local.maps.UserUIUpdating
import ru.data.common.models.res.TextApp
import ru.data.common.models.util.onlyDigit

class RegStepsModel(
    private val apiUser: UseCaseUser,
    private val apiLocation: UseCaseLocations,
    private val apiInterests: UseCaseInterests,
    private val apiMembershipDroid: UseCaseMembershipDroid,
    private val apiDroid: UseCaseDroid,
    private val context: Context
) : BaseModel() {

    private var jobSearch: Job = Job()
    private var jobSearchInterests: Job = Job()

    private val _updatingUser = MutableStateFlow(UserUIUpdating())
    val updatingUser = _updatingUser.asStateFlow()

    private val _image = MutableStateFlow<Uri?>(null)
    val image = _image.asStateFlow()

    private val _location = MutableStateFlow(listOf<City>())
    val location = _location.asStateFlow()

    private val _interests = MutableStateFlow(listOf<InterestUI>())
    val interests = _interests.asStateFlow()

    private val _DroidRequest = MutableStateFlow<DroidRequestUI?>(null)
    val DroidRequest = _DroidRequest.asStateFlow()

    private val _firstDroidMember = MutableStateFlow(
        DroidMemberUICreating(
            role = RoleDroid.SPOUSE,
            gender = updatingUser.value.gender?.getGenderYourSatellite()
        )
    )

    val firstDroidMember = _firstDroidMember.asStateFlow()

    private val _DroidMembers = MutableStateFlow(listOf<DroidMemberUICreating>())
    val DroidMembers = _DroidMembers.asStateFlow()

    init {
        coroutineScope.launch {
            _location.value = apiLocation.getDdCities()
        }
    }

    fun onSearchInterests(search: String? = null) {
        jobSearchInterests.cancel()
        jobSearchInterests = coroutineScope.launch {
            delay(500)
            _interests.value = apiInterests.getInterests(
                page = 1,
                name = search?.ifEmpty { null }
            )
        }
    }

    fun updateMyProfileStepThird(
        birthdate: Long,
        residenceLocation: City?,
        birthLocation: City?
    ) {
        _updatingUser.update {
            it.copy(
                birthdate = birthdate,
                birthLocation = birthLocation,
                location = residenceLocation,
            )
        }
        updateAllDadaMyProfile()
    }


    fun onSearchLocation(searchCity: String?) {
        jobSearch.cancel()
        jobSearch = coroutineScope.launch {
            delay(500)
            _location.update { apiLocation.getDdCities(searchCity) }
        }
    }

    fun getUser(
    ) = coroutineScope.launch {
        apiUser.getMe(
            flowStart = {},
            flowSuccess = {
                userUpdateAndGetLocations(it)
            },
            flowStop = {},
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = {},
            flowError = {

            }
        )
    }

    private fun userUpdateAndGetLocations(user: UserUI) = coroutineScope.launch {
        _updatingUser.update { user.mapInUpdatingUserUI() }
        _firstDroidMember.update {
            DroidMemberUICreating(
                role = RoleDroid.SPOUSE,
                gender = _updatingUser.value.gender?.getGenderYourSatellite()
            )
        }
    }

    fun updateFirstDroidMember(person: DroidMemberUICreating) {
        _firstDroidMember.update { person }
    }

    fun updateListDroidMembers(persons: List<DroidMemberUICreating>) {
        _DroidMembers.update { persons }
    }

    fun addDroidMember() {
        _DroidMembers.update { it + DroidMemberUICreating(role = RoleDroid.CHILD) }

    }

    fun updateMyProfileStepTwo(
        firstName: String,
        lastName: String,
        patronymic: String?,
        maidenName: String?,
        gender: Gender,
    ) {
        _updatingUser.update {
            it.copy(
                firstName = firstName,
                lastName = lastName,
                patronymic = patronymic,
                maidenName = maidenName,
                gender = gender
            )
        }
        navigator.push(StepThirdScreen())
    }

    fun uploadPhoto(image: Uri?) {
        _image.update { image }
    }


    fun finishAddNewDroidCell() = coroutineScope.launch {
        val listMembers = DroidMembers.value + firstDroidMember.value
        apiDroid.postNewDroidAndListMember(
            listMembers = listMembers,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                gDSetLoader(false)
                goBackToSplashScreen()
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {},
            flowMessage = ::message
        )
    }

    fun sendIdCell(
        id: String,
    ) = coroutineScope.launch {
        val idNumb = id.onlyDigit().toIntOrNull() ?: return@launch
        apiMembershipDroid.postCollectivesRequests(
            DroidId = idNumb,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                gDSetLoader(false)
                _DroidRequest.value = it
                navigator.push(StepFifthJoinInCellSecond())
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {},
            flowMessage = ::message
        )
    }

    fun goToFifthStep(
        description: String?,
        interests: List<String>?
    ) {
        _updatingUser.update {
            it.copy(
                description = description,
                interests = interests?.ifEmpty { null },
            )
        }
        goToFifthStep()
    }


    private fun updateAllDadaMyProfile() = coroutineScope.launch {
        gDLoaderStart()
        var onNextScr = false
        apiUser.putMe(
            user = updatingUser.value,
            flowSuccess = {
                message(TextApp.textDataUpdated)
                userUpdateAndGetLocations(it)
                onNextScr = true
            },
            flowMessage = ::message,
            flowUnauthorized = {}
        )

        image.value?.let { imageNotNull ->
            apiUser.postMyAvatar(
                uri = imageNotNull.toString(),
                compressedFile = {
                    runBlocking {
                        Compressor.compress(context, it.toUri().toFile()) {
                            default(width = 1024)
                        }
                    }
                },
                flowSuccess = {},
                flowMessage = ::message,
                flowUnauthorized = {}
            )
        }

        gDLoaderStop()
        if (onNextScr) {
            goToFifthStep()
        }
    }

    fun goToAuth() {
        getNavigationLevel(NavLevel.MAIN)?.replaceAll(AuthScreen())
    }

    fun goToJoinInCellFirst() {
        navigator.push(StepFifthJoinInCellFirst())
    }

    fun goToNewCellThird() {
        navigator.push(StepFifthCreateNewCellThird())
    }

    fun goToCreateNewCellFirst() {
        navigator.push(StepFifthCreateNewCellFirst())
    }

    fun goToFifthStep() {
        navigator.push(StepFifthScreen())
    }

    fun addSatelliteInCell() {
        navigator.push(StepFifthCreateNewCellSecond())
    }

    fun goBackToSplashScreen() {
        getNavigationLevel(NavLevel.MAIN)?.replaceAll(SplashScreen())
    }


}
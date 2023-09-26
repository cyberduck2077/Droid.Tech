package ru.droid.tech.screens.module_main.show_Droid

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.droid.tech.base.BasePagination
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.chat_user.ChatUser
import ru.droid.tech.screens.module_main.search_users.SearchUsers
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDMessage
import ru.data.common.domain.use_case.UseCaseDroid
import ru.data.common.domain.use_case.UseCaseLocations
import ru.data.common.domain.use_case.UseCaseRequestsDroid
import ru.data.common.domain.use_case.UseCaseUser
import ru.data.common.domain.use_case.UseCaseUsers
import ru.data.common.models.data_base.City
import ru.data.common.models.local.maps.DroidMemberUI
import ru.data.common.models.local.maps.Gender
import ru.data.common.models.local.maps.RoleDroid
import ru.data.common.models.local.screens.MyDroidStatusScr

class ShowDroidModel(
    private val apiDroid: UseCaseDroid,
    private val apiUser: UseCaseUser,
    private val apiLocation: UseCaseLocations,
    private val apiUsers: UseCaseUsers,
    private val apiRequestsDroid: UseCaseRequestsDroid,
) : BaseModel() {

    private var jobSearch: Job = Job()

    private val _listDroid = MutableStateFlow<List<DroidMemberUI>>(listOf())
    private val _listMyDroid = MutableStateFlow<List<DroidMemberUI>>(listOf())
    val listMyDroid = _listMyDroid.asStateFlow()

    private val _ageFrom = MutableStateFlow<Int?>(null)
    val ageFrom = _ageFrom.asStateFlow()

    private val _statusScr = MutableStateFlow(MyDroidStatusScr.Droid)
    val statusScr = _statusScr.asStateFlow()

    private val _chooseRoleDroid = MutableStateFlow<RoleDroid?>(null)
    val chooseRoleDroid = _chooseRoleDroid.asStateFlow()

    private val _ageTo = MutableStateFlow<Int?>(null)
    val ageTo = _ageTo.asStateFlow()

    private val _gender = MutableStateFlow<Gender?>(null)
    val gender = _gender.asStateFlow()

    private val _locationChoose = MutableStateFlow<City?>(null)
    val locationChoose = _locationChoose.asStateFlow()

    private val _chooseIsIncoming = MutableStateFlow(true)
    val chooseIsIncoming = _chooseIsIncoming.asStateFlow()

    private val _isMyMainRole = MutableStateFlow(true)
    val isMyMainRole = _isMyMainRole.asStateFlow()

    private val _chooseIsIncomingApplication = MutableStateFlow(true)
    val chooseIsIncomingApplication = _chooseIsIncomingApplication.asStateFlow()

    private val _location = MutableStateFlow(listOf<City>())
    val location = _location.asStateFlow()

    init {
        coroutineScope.launch(Dispatchers.IO) {
            _location.value = apiLocation.getDdCities()
        }
        getMyDroid()
    }

    val pagingInvitationsIn = BasePagination(
        scope = coroutineScope,
        unit = { pageCurrent, statusPage ->
            apiRequestsDroid.getRequestsDroid(
                DroidId = apiUser.getChooseDroidId() ?: 0,
                page = pageCurrent,
                listStatus = statusPage,
            )
        }
    )

    val pagingSearchUsersInMainScr = BasePagination(
        scope = coroutineScope,
        unit = { pageCurrent, statusPage ->
            apiUsers.getUsers(
                page = pageCurrent,
                name = null,
                fullName = null,
                gender = gender.value?.ordinal,
                ageFrom = ageFrom.value?.toLong(),
                ageTo = ageTo.value?.toLong(),
                inCurrentCollectives = null,
                DroidIds = null,
                locationIds = locationChoose.value?.let { listOf(it.id) },
                birthLocationIds = null,
                listStatus = statusPage,
            )
        }
    )

    fun setIsIncoming(bool: Boolean) {
        _chooseIsIncoming.update { bool }
        gDMessage("STUB")
    }

    fun setIsIncomingApplications(bool: Boolean) {
        _chooseIsIncomingApplication.update { bool }
        gDMessage("STUB")
    }

    private fun getMyDroid() = coroutineScope.launch(Dispatchers.IO) {
        apiUser.getChooseDroidId()?.let { id ->
            apiDroid.getDroidId(
                id = id,
                flowStart = {},
                flowSuccess = { Droid ->
                    _listDroid.update { Droid.members }
                    _listMyDroid.update { Droid.members }
                    _isMyMainRole.update {
                        Droid.members.firstOrNull { it.id == apiUser.getUserLocalData().id }?.ownerRole == RoleDroid.SELF
                    }
                },
                flowUnauthorized = {
                    getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
                },
                flowStop = {},
                flowMessage = ::message
            )
        }
    }

    private fun updateLists() {
        when (statusScr.value) {
            MyDroidStatusScr.Droid       -> getMyDroid()
            MyDroidStatusScr.APPLICATIONS -> {
                //TODO()
                gDMessage("Stub")
            }
            MyDroidStatusScr.SEARCH       -> pagingSearchUsersInMainScr.refreshFlow()
            MyDroidStatusScr.INVITATIONS  -> pagingInvitationsIn.refreshFlow()
        }
    }

    fun onSearchLocation(searchCity: String?) {
        jobSearch.cancel()
        jobSearch = coroutineScope.launch(Dispatchers.IO) {
            delay(500)
            _location.update { apiLocation.getDdCities(searchCity) }
        }
    }

    fun setFilterInUserSearch(
        genderNew: Gender?,
        ageFromNew: Int?,
        ageToNew: Int?,
        locationChooseNew: City?,
    ) {
        _gender.value = genderNew
        _ageFrom.value = ageFromNew
        _ageTo.value = ageToNew
        _locationChoose.value = locationChooseNew
        pagingSearchUsersInMainScr.refreshFlow()
    }

    fun filterDroid(roleDroid: RoleDroid?) {
        if (roleDroid == null) {
            _listMyDroid.update { _listDroid.value }
            return
        }
        _chooseRoleDroid.update { roleDroid }
        _listMyDroid.update { _listDroid.value.filter { it.userRole == roleDroid } }
    }

    suspend fun getLocation(idLocation: Int): String {
        return apiLocation.getDdCity(idLocation)?.name ?: "-"
    }

    fun userInvite(
        userId: Int,
        text: String,
    ) = coroutineScope.launch(Dispatchers.IO) {
        apiUsers.postFriendship(
            userId = userId,
            text = text,
            flowStart = ::gDLoaderStart,
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun sendMessage(
        userId: Int,
        text: String,
    ) {
        getNavigationLevel(NavLevel.MAIN)?.push(ChatUser(userId))
    }

    fun findDuplicate(nums: IntArray): Int {
        nums.sorted().indexOfFirst { index -> nums.getOrNull(index + 1) == nums.getOrNull(index) }
        return 0
    }

    fun editDroidMembership(
        requestId: Int,
        isApproved: Boolean,
        roleDroid: RoleDroid?,
    ) = coroutineScope.launch(Dispatchers.IO) {
        apiRequestsDroid.putCollectivesRequest(
            requestId = requestId,
            isApproved = isApproved,
            DroidRole = roleDroid,
            flowStart = ::gDLoaderStart,
            flowStop = ::gDLoaderStop,
            flowSuccess = {
                pagingInvitationsIn.refreshFlow()
            },
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun setMyDroidStatusScr(scr: MyDroidStatusScr) {
        _statusScr.update { scr }
        updateLists()
    }

    fun goToSearchUsers() {
        getNavigationLevel(NavLevel.MAIN)?.push(SearchUsers())
    }
}


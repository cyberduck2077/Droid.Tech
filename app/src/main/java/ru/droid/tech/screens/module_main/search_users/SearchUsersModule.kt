package ru.droid.tech.screens.module_main.search_users

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.droid.tech.base.BaseModel
import ru.droid.tech.base.BasePagination
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.use_case.UseCaseLocations
import ru.data.common.domain.use_case.UseCaseUsers
import ru.data.common.models.local.maps.UserUI

class SearchUsersModule(
    private val apiUsers: UseCaseUsers,
    private val apiLocation: UseCaseLocations,
) : BaseModel() {

    private var jobSearch: Job = Job()

    private val _fullName = MutableStateFlow("")
    val fullName = _fullName.asStateFlow()

    private val scopeForePagination = CoroutineScope(coroutineScope.coroutineContext + Dispatchers.IO)


    val pagingSearchUsersInMainScr =  BasePagination(
        scope = scopeForePagination,
        unit = { pageCurrent, statusPage ->
            apiUsers.getUsers(
                page = pageCurrent,
                fullName = fullName.value.ifEmpty { null },
                listStatus = statusPage,
                )
        }
    )

    fun userSearch(
        text: String,
    ) {
        _fullName.value = text
        jobSearch.cancel()
        jobSearch = coroutineScope.launch {
            delay(500)
            pagingSearchUsersInMainScr.refreshFlow()
        }
    }

    fun userInvite(
        userId: Int,
        text: String,
    ) = coroutineScope.launch {
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

    override fun onDispose() {
        scopeForePagination.cancel()
        super.onDispose()
    }
}
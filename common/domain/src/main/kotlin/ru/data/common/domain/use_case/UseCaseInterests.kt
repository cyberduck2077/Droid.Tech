package ru.data.common.domain.use_case

import ru.data.common.models.local.maps.InterestUI
import ru.data.common.network.api.ApiInterests

class UseCaseInterests(
    private val apiInterests: ApiInterests,
) {

    suspend fun getInterests(
        page: Int? = 1,
        name: String? = null,
    ): List<InterestUI> = apiInterests.getInterests(
        name = name,
        page = page
    ).data?.map { InterestUI.mapFrom(it) } ?: listOf()

}
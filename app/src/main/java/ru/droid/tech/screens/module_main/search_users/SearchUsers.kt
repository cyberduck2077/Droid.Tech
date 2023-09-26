package ru.droid.tech.screens.module_main.search_users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.ContentErrorLoad
import ru.droid.tech.base.common_composable.DialogInvite
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextFieldOutlinesAppStr
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.UserUI

class SearchUsers : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<SearchUsersModule>()
        val fullName by model.fullName.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)

        SearchUsersScr(
            onClickBack = model::goBackStack,
            onSearchText = fullName,
            onSearch = model::userSearch,
            pagingUsers = model.pagingSearchUsersInMainScr.getFlow(),
            onClickInviteFriendship = model::userInvite,
        )
    }


    @Composable
    private fun SearchUsersScr(
        onSearchText: String,
        onSearch: (String) -> Unit,
        onClickBack: () -> Unit,
        pagingUsers: LazyPagingItems<UserUI>,
        onClickInviteFriendship: (
            userId: Int,
            text: String,
        ) -> Unit,
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeApp.colors.background)
                .systemBarsPadding()
        ) {

            PanelNavBackTop(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = DimApp.shadowElevation)
                    .background(ThemeApp.colors.backgroundVariant),
                onClickBack = onClickBack,
                container = ThemeApp.colors.backgroundVariant,
                content = {
                    TextFieldOutlinesAppStr(
                        modifier = Modifier.fillMaxWidth(),
                        value = onSearchText,
                        onValueChange = {
                            onSearch.invoke(it)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                        ),
                        placeholder = {
                            Text(text = TextApp.textEnterARequest)
                        },
                        trailingIcon = {
                            if (onSearchText.isNotEmpty()) {
                                IconButton(onClick = {
                                    onSearch.invoke("")
                                }) {
                                    IconApp(painter = rememberImageRaw(id = R.raw.ic_cancel))
                                }

                            } else {
                                IconApp(painter = rememberImageRaw(id = R.raw.ic_search))
                            }

                        },
                    )
                }
            )

            ListSearch(
                paging = pagingUsers,
                onClickInviteFriendship = onClickInviteFriendship,
            )
        }
    }

    @Composable
    private fun ListSearch(
        paging: LazyPagingItems<UserUI>,
        onClickInviteFriendship: (
            userId: Int,
            text: String,
        ) -> Unit,
    ) {

        var dialogInviteFriendship by rememberState<Int?> { null }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = DimApp.screenPadding),
            verticalArrangement = Arrangement.spacedBy(DimApp.screenPadding),
        ) {

            item {
                ContentErrorLoad(visible = paging.loadState.refresh is LoadState.Error)
            }
            items(
                count = paging.itemCount,
                key = paging.itemKey { item ->
                    return@itemKey item.id
                },
            ) { index ->
                val item = paging[index] ?: return@items
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BoxImageLoad(
                        modifier = Modifier
                            .padding(end = DimApp.screenPadding)
                            .size(DimApp.iconSizeOrder)
                            .clip(CircleShape),
                        drawableError = R.drawable.stab_avatar,
                        drawablePlaceholder = R.drawable.stab_avatar,
                        image = item.avatar
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        TextTitleSmall(
                            text = item.getNameAndLastName(),
                            maxLines = 1
                        )
                        TextBodyMedium(
                            text = item.location?.name ?:""
                        )
                    }
                    IconButtonApp(onClick = { dialogInviteFriendship = item.id }) {
                        IconApp(painter = rememberImageRaw(R.raw.ic_person_add))
                    }
                }
            }

            if (paging.loadState.refresh is LoadState.NotLoading
                && paging.itemCount == 0) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextTitleSmall(text = TextApp.textItEmpty)
                    }

                }
            }

            if (paging.loadState.refresh is LoadState.Loading
                || paging.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(DimApp.screenPadding),
                            color = ThemeApp.colors.primary
                        )
                    }
                }
            }
        }

        dialogInviteFriendship?.let { userId ->
            DialogInvite(
                onDismiss = { dialogInviteFriendship = null },
                onClick = { s ->
                    onClickInviteFriendship.invoke(userId, s)
                })
        }
    }
}
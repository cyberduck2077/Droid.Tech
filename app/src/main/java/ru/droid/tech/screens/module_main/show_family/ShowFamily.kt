package ru.droid.tech.screens.module_main.show_Droid

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ContentErrorLoad
import ru.droid.tech.base.common_composable.DialogBottomSheet
import ru.droid.tech.base.common_composable.DialogInvite
import ru.droid.tech.base.common_composable.FillLineHorizontal
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.TextButtonStyle
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.common_composable.colorsButtonAccentTextApp
import ru.droid.tech.base.common_composable.colorsIconButtonApp
import ru.droid.tech.base.common_composable.handleStateEmpty
import ru.droid.tech.base.common_composable.handleStateError
import ru.droid.tech.base.common_composable.handleStateProgress
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.data_base.City
import ru.data.common.models.local.maps.DroidMemberUI
import ru.data.common.models.local.maps.DroidRequestUI
import ru.data.common.models.local.maps.Gender
import ru.data.common.models.local.maps.RoleDroid
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.local.screens.MyDroidStatusScr
import ru.data.common.models.res.TextApp
import kotlin.time.Duration.Companion.seconds

class ShowDroid() : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ShowDroidModel>()
        BackPressHandler(onBackPressed = model::goBackStack)

        val listMyDroid by model.listMyDroid.collectAsState()
        val statusScr by model.statusScr.collectAsState()
        val ageFrom by model.ageFrom.collectAsState()
        val ageTo by model.ageTo.collectAsState()
        val gender by model.gender.collectAsState()
        val locationChoose by model.locationChoose.collectAsState()
        val chooseIsIncoming by model.chooseIsIncoming.collectAsState()
        val chooseRoleDroid by model.chooseRoleDroid.collectAsState()
        val location by model.location.collectAsState()
        val chooseIsIncomingApplication by model.chooseIsIncomingApplication.collectAsState()
        val isMyMainRole by model.isMyMainRole.collectAsState()

        MyDroidScr(
            onClickBack = model::goBackStack,
            onClickSearch = model::goToSearchUsers,
            DroidStatusScr = statusScr,
            onClickDroidStatusScr = model::setMyDroidStatusScr,
            pagingInvitationsIn = model.pagingInvitationsIn.getFlow(),
            listMyDroid = listMyDroid,
            pagingUsers = model.pagingSearchUsersInMainScr.getFlow(),
            ageFrom = ageFrom,
            ageTo = ageTo,
            isMyMainRole = isMyMainRole,
            chooseRoleDroid = chooseRoleDroid,
            chooseIsIncoming = chooseIsIncoming,
            chooseIsIncomingApplication = chooseIsIncomingApplication,
            gender = gender,
            locationChoose = locationChoose,
            location = location,
            onClickMessage = model::sendMessage,
            onClickIsIncoming = model::setIsIncoming,
            onClickIsIncomingApplications = model::setIsIncomingApplications,
            onClickMembership = model::editDroidMembership,
            onClickInviteFriendship = model::userInvite,
            onSearchLocation = model::onSearchLocation,
            onFilterUserSearch = model::setFilterInUserSearch,
            onFilterDroid = model::filterDroid,
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun MyDroidScr(
        DroidStatusScr: MyDroidStatusScr,
        listMyDroid: List<DroidMemberUI>,
        pagingInvitationsIn: LazyPagingItems<DroidRequestUI>,
        pagingUsers: LazyPagingItems<UserUI>,
        ageFrom: Int?,
        ageTo: Int?,
        gender: Gender?,
        chooseRoleDroid: RoleDroid?,
        chooseIsIncoming: Boolean,
        isMyMainRole: Boolean,
        chooseIsIncomingApplication: Boolean,
        locationChoose: City?,
        location: List<City>,
        onClickBack: () -> Unit,
        onClickIsIncoming: (Boolean) -> Unit,
        onClickIsIncomingApplications: (Boolean) -> Unit,
        onClickSearch: () -> Unit,
        onClickDroidStatusScr: (MyDroidStatusScr) -> Unit,
        onClickMessage: (
            userId: Int,
            text: String,
        ) -> Unit,
        onClickMembership: (
            requestId: Int,
            isApproved: Boolean,
            roleDroid: RoleDroid?,
        ) -> Unit,
        onClickInviteFriendship: (
            userId: Int,
            text: String,
        ) -> Unit,
        onSearchLocation: (
            text: String?,
        ) -> Unit,
        onFilterUserSearch: (
            genderNew: Gender?,
            ageFromNew: Int?,
            ageToNew: Int?,
            locationChooseNew: City?,
        ) -> Unit,
        onFilterDroid: (roleDroid: RoleDroid?) -> Unit,
    ) {

        val menuList by rememberState { MyDroidStatusScr.values() }
        val pagerState: PagerState = rememberPagerState() { menuList.size }
        val scope = rememberCoroutineScope()
        var dialogFilterDroid by rememberState { false }
        var dialogSearchUsers by rememberState { false }
        var dialogFilterInvitations by rememberState { false }
        var dialogFilterApplications by rememberState { false }
        var dialogMembership: Pair<UserUI, Boolean>? by rememberState { null }

        val onFilter = remember(DroidStatusScr) {
            {
                when (DroidStatusScr) {
                    MyDroidStatusScr.Droid       -> dialogFilterDroid = true
                    MyDroidStatusScr.INVITATIONS  -> dialogFilterInvitations = true
                    MyDroidStatusScr.APPLICATIONS -> dialogFilterApplications = true
                    MyDroidStatusScr.SEARCH       -> dialogSearchUsers = true
                }
            }
        }

        LaunchedEffect(
            key1 = pagerState.currentPage,
            block = {
                val item = menuList.getOrNull(pagerState.currentPage) ?: return@LaunchedEffect
                onClickDroidStatusScr.invoke(item)
            })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeApp.colors.background)
                .systemBarsPadding()
        ) {
            PanelTopRibbon(
                onClickDroidStatusScr = {
                    onClickDroidStatusScr.invoke(it)
                    scope.launch {
                        pagerState.scrollToPage(it.ordinal)
                    }
                },
                isMyMainRole = isMyMainRole,
                DroidStatusScr = DroidStatusScr,
                onClickBack = onClickBack,
                onClickFilter = onFilter,
                onClickSearch = onClickSearch,
            )

            HorizontalPager(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(0.dp),
                verticalAlignment = Alignment.Top,
                state = pagerState
            ) { page ->
                val menuItem = menuList.getOrNull(page) ?: return@HorizontalPager
                when (menuItem) {
                    MyDroidStatusScr.Droid       -> ListDroid(
                        listMyDroid = listMyDroid,
                        onClickMessage = onClickMessage,
                    )

                    MyDroidStatusScr.APPLICATIONS -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            TextBodyMedium(
                                modifier = Modifier.align(Alignment.Center),
                                text = "Stub")
                        }
                    }

                    MyDroidStatusScr.INVITATIONS  -> ListInvitations(
                        paging = pagingInvitationsIn,
                        onClickMembership = { requestUser: UserUI, isApproved: Boolean ->
                            if (isApproved) {
                                dialogMembership = Pair(requestUser, isApproved)
                            } else {
                                onClickMembership.invoke(requestUser.id, isApproved, null)
                            }
                        },
                    )

                    MyDroidStatusScr.SEARCH       -> ListSearch(
                        paging = pagingUsers,
                        onClickInviteFriendship = onClickInviteFriendship
                    )
                }
            }
        }

        if (dialogFilterDroid) {
            Dialog(onDismissRequest = { dialogFilterDroid = false }) {
                ContentDialogFilterDroid(
                    roleDroid = chooseRoleDroid,
                    onFilterDroid = onFilterDroid,
                    onDismiss = {
                        dialogFilterDroid = false
                    },
                )
            }
        }

        if (dialogFilterInvitations) {
            Dialog(onDismissRequest = { dialogFilterInvitations = false }) {
                ContentDialogFilterAppeals(
                    chooseIsIncoming = chooseIsIncoming,
                    onClickIsIncoming = onClickIsIncoming,
                    onDismiss = {
                        dialogFilterInvitations = false
                    },
                )
            }
        }

        if (dialogFilterApplications) {
            Dialog(onDismissRequest = { dialogFilterApplications = false }) {
                ContentDialogFilterInvitations(
                    chooseIsIncoming = chooseIsIncomingApplication,
                    onClickIsIncoming = onClickIsIncomingApplications,
                    onDismiss = {
                        dialogFilterApplications = false
                    },
                )
            }
        }

        if (dialogSearchUsers) {
            DialogBottomSheet(onDismiss = { dialogSearchUsers = false }) { dismiss ->
                ContentDialogSearchUsers(
                    ageFrom = ageFrom,
                    ageTo = ageTo,
                    gender = gender,
                    onDismiss = dismiss,
                    locationChoose = locationChoose,
                    locations = location,
                    onSearchLocation = onSearchLocation,
                    onFilterUserSearch = onFilterUserSearch,
                )
            }
        }

        dialogMembership?.let { item ->
            val requestUser = item.first
            val isApproved = item.second
            Dialog(onDismissRequest = { dialogMembership = null }) {
                ContentDialogMembership(
                    name = requestUser.getNameAndLastName(),
                    onClickSend = { onClickMembership.invoke(requestUser.id, isApproved, it) },
                    onDismiss = {
                        dialogMembership = null
                    }
                )
            }
        }
    }

    @Composable
    private fun ListDroid(
        listMyDroid: List<DroidMemberUI>,
        onClickMessage: (
            userId: Int,
            text: String,
        ) -> Unit,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = DimApp.screenPadding),
            verticalArrangement = Arrangement.spacedBy(DimApp.screenPadding),
        ) {

            if (listMyDroid.isEmpty()) {
                item {
                    var isLoad by remember {
                        mutableStateOf(true)
                    }
                    LaunchedEffect(key1 = Unit, block = {
                        delay(5.seconds)
                        isLoad = false
                    })
                    when (isLoad) {
                        true  -> Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(DimApp.screenPadding),
                                color = ThemeApp.colors.primary
                            )
                        }

                        false -> Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            TextTitleSmall(text = TextApp.textItEmpty)
                        }
                    }
                }
            }

            item {
                BoxSpacer(.5f)
            }

            items(
                items = listMyDroid,
                key = { it.id }) { item ->
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
                        image = item.user?.avatar
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        TextTitleSmall(
                            text = item.getNameAndLastName(),
                            maxLines = 1
                        )
                        TextBodyMedium(
                            text = item.user?.location?.name ?: ""
                        )
                    }
                    item.user?.let { user ->
                        IconButtonApp(
                            onClick = {
                                onClickMessage.invoke(user.id, "TODO(\"STUB\")") // TODO("STUB")
                            },
                            colors = colorsIconButtonApp().copy(contentColor = ThemeApp.colors.primary),
                        ) {
                            IconApp(painter = rememberImageRaw(R.raw.ic_sms))
                        }
                    }

                }
            }
        }
    }

    @Composable
    private fun ListInvitations(
        paging: LazyPagingItems<DroidRequestUI>,
        onClickMembership: (
            request: UserUI,
            isApproved: Boolean,
        ) -> Unit,
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = DimApp.screenPadding),
            verticalArrangement = Arrangement.spacedBy(DimApp.screenPadding),
        ) {
            handleStateError(paging.loadState.refresh)
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
                        image = item.user?.avatar
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        TextTitleSmall(
                            text = item.user?.getNameAndLastName() ?: "",
                            maxLines = 1
                        )
                        TextBodyMedium(
                            text = item.user?.location?.name ?: ""
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(modifier = Modifier
                                .clip(ThemeApp.shape.smallAll)
                                .clickableRipple {
                                    item.user?.let { onClickMembership.invoke(it, true) }
                                }) {
                                TextButtonStyle(
                                    modifier = Modifier.padding(DimApp.textPaddingMin),
                                    color = ThemeApp.colors.primary,
                                    text = TextApp.textAcceptApplication
                                )
                            }
                            BoxSpacer()
                            Box(modifier = Modifier
                                .clip(ThemeApp.shape.smallAll)
                                .clickableRipple {
                                    item.user?.let { onClickMembership.invoke(it, false) }
                                }) {
                                TextButtonStyle(
                                    modifier = Modifier.padding(DimApp.textPaddingMin),
                                    text = TextApp.textReject
                                )
                            }

                        }
                    }
                }
            }
            handleStateEmpty(paging)
            handleStateProgress(paging)
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
                            text = item.location?.name ?: ""
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

    @Composable
    private fun PanelTopRibbon(
        onClickDroidStatusScr: (MyDroidStatusScr) -> Unit,
        DroidStatusScr: MyDroidStatusScr,
        isMyMainRole: Boolean,
        onClickBack: () -> Unit,
        onClickFilter: () -> Unit,
        onClickSearch: () -> Unit,
        styleMenuText: TextStyle = ThemeApp.typography.button
    ) {
        val menuList by rememberState {
            MyDroidStatusScr.values().filter { filter ->
                if (isMyMainRole) {
                    true
                } else {
                    filter != MyDroidStatusScr.APPLICATIONS
                        && filter != MyDroidStatusScr.INVITATIONS
                }
            }
        }
        var offsetTargetDot by rememberState { 0.dp }
        val offsetDot by animateDpAsState(targetValue = offsetTargetDot, label = "")
        val des = LocalDensity.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = DimApp.shadowElevation)
                .background(ThemeApp.colors.backgroundVariant),
        ) {
            PanelNavBackTop(
                onClickBack = onClickBack,
                container = ThemeApp.colors.backgroundVariant,
                text = TextApp.titleDroidRibbon,
                content = {
                    IconButtonApp(
                        modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                        onClick = onClickFilter
                    ) {
                        IconApp(painter = rememberImageRaw(R.raw.ic_setting_post))
                    }
                    IconButtonApp(
                        modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                        onClick = onClickSearch
                    ) {
                        IconApp(painter = rememberImageRaw(R.raw.ic_search))
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DimApp.screenPadding)
                    .padding(end = DimApp.screenPadding / 2),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                menuList.forEach { item ->
                    var fontSize by rememberState { 1f }
                    val isChoose by rememberState(DroidStatusScr) {
                        val choose = DroidStatusScr == item
                        fontSize = if (choose) 1.3f else 1f
                        choose
                    }
                    val animFontSize = animateFloatAsState(targetValue = fontSize, label = "")

                    TextButtonApp(
                        modifier = Modifier.onGloballyPositioned {
                            if (isChoose) {
                                offsetTargetDot = with(des) {
                                    it.positionInWindow().x.toDp() + (it.size.width * .5f).toDp()
                                }
                            }
                        },
                        ifCorrectTextSize = false,
                        styleText = styleMenuText.copy(fontSize = styleMenuText.fontSize * animFontSize.value),
                        contentPadding = PaddingValues(DimApp.screenPadding * .5f),
                        colors = colorsButtonAccentTextApp().copy(
                            contentColor = if (DroidStatusScr == item) {
                                ThemeApp.colors.primary
                            } else {
                                ThemeApp.colors.textDark
                            }
                        ),
                        onClick = {
                            onClickDroidStatusScr.invoke(item)
                        },
                        text = item.getTextStage()
                    )
                    BoxSpacer()
                }
            }
            Box(
                modifier = Modifier
                    .offset(x = offsetDot - (DimApp.menuItemsWidth * .5f))
                    .width(DimApp.menuItemsWidth)
                    .height(DimApp.menuItemsHeight)
                    .clip(ThemeApp.shape.smallTop)
                    .background(ThemeApp.colors.primary)
            )
            FillLineHorizontal(modifier = Modifier.fillMaxWidth())
        }
    }

}
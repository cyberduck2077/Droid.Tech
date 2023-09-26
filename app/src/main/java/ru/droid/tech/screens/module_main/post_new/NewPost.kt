package ru.droid.tech.screens.module_main.post_new

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxImageRowRes
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.DialogGetImageList
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.TextButtonStyle
import ru.droid.tech.base.common_composable.TextFieldApp
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.domain.memory.gDMessage
import ru.data.common.models.local.maps.AttachmentUI
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.local.maps.TopicUI

class NewPost(private val postIdRote: Int? = null) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<NewPostModel>()
        val gettingPost by model.gettingPost.collectAsState()
        val gettingTopics by model.gettingTopics.collectAsState()
        val isUpdatePost by model.isUpdatePost.collectAsState()
        LifeScreen(onCreate = {
            model.setNetworkModelPost(postIdRote)
            model.getTopics()
        })

        ContentPostScr(
            onClickBack = model::goBackStack,
            gettingPost = gettingPost,
            isUpdatePost = isUpdatePost,
            onClickUpdatePost = model::updatePost,
            onClickSendNewPost = model::sendNewPost,
            listTopic = gettingTopics,
        )
    }
}

@Composable
private fun ContentPostScr(
    gettingPost: PostWithCommentUI?,
    onClickBack: () -> Unit,
    listTopic: List<TopicUI>,
    onClickUpdatePost: (
        text: String, attachmentNew: List<String>, attachment: List<AttachmentUI>, topic: TopicUI?, isPrivate: Boolean
    ) -> Unit,
    onClickSendNewPost: (
        text: String, attachmentNew: List<String>, topic: TopicUI?, isPrivate: Boolean
    ) -> Unit,
    isUpdatePost: Boolean,
) {

    val focusHelper = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var text by rememberState(gettingPost) { TextFieldValue(text = gettingPost?.text ?: "") }
    var listImageInNetworkModelPost by rememberState(gettingPost) {
        gettingPost?.attachments?.filter { !it.isVideo } ?: listOf()
    }
    var listImageNew by rememberState<List<String>> { listOf() }
    var dialogGetImage by rememberState { false }

    var chooseTopic by rememberState(gettingPost) { gettingPost?.topic }
    var isPrivatePost by rememberState { false }

    val enableSend = remember(text.text, listImageNew, listImageInNetworkModelPost) {
        text.text.isNotEmpty()
    }
    val sendPost = remember(enableSend, text.text, listImageNew, listImageInNetworkModelPost) {
        {
            if (isUpdatePost) {
                onClickUpdatePost.invoke(text.text,
                    listImageNew,
                    listImageInNetworkModelPost,
                    chooseTopic,
                    isPrivatePost)
            } else {
                onClickSendNewPost.invoke(text.text, listImageNew, chooseTopic, isPrivatePost)
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(ThemeApp.colors.background)
        .systemBarsPadding()) {
        PanelTopContentPost(
            onClickBack = onClickBack,
            enableSend = enableSend,
            onClickSendPost = sendPost,
            title = if (isUpdatePost) TextApp.textPostEditing else TextApp.textNewPost,
            textButton = if (isUpdatePost) TextApp.textUpdatePost else TextApp.textPostPost,
        )

        ContentEnter(
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
                .weight(1f),
            listImageInNetworkModelPost = listImageInNetworkModelPost,
            listImageNew = listImageNew,
            text = text,
            onTextChange = { text = it },
            focusHelper = focusHelper,
            onDeleteImageApi = {
                val listImageMutable = listImageInNetworkModelPost.toMutableList()
                listImageMutable.removeIf { itRemove -> itRemove.id == it.id }
                listImageInNetworkModelPost = listImageMutable

            },
            onDeleteImageLocal = {
                val listImageMutable = listImageNew.toMutableList()
                listImageMutable.removeIf { itRemove -> itRemove == it }
                listImageNew = listImageMutable
            },
        )

        PanelBottomContentPost(
            onClickAddPhoto = {
                dialogGetImage = true
            },
            onClickAddVideo = {
                gDMessage("Stub")
            },
            onClickAddFile = {
                gDMessage("Stub")
            },
            onClickAddQuiz = {
                gDMessage("Stub")
            },
            onClickSettings = {
                gDMessage("Stub")
            },
            onClickAddSmile = {
                focusManager.clearFocus()
                focusHelper.requestFocus()
            },
            listTopic = listTopic,
            chooseTopic = chooseTopic,
            onClickTopic = {
                chooseTopic = it
            },
            onClickPrivate = {
                isPrivatePost = it
            },
            isPrivate = isPrivatePost,
        )

    }

    if (dialogGetImage) {
        DialogGetImageList(
            onDismiss = { dialogGetImage = false },
            getPhoto = { images ->
            listImageNew = listImageNew + images.map { it.toString() }
        })
    }
}

@Composable
private fun ContentEnter(
    modifier: Modifier = Modifier,
    listImageInNetworkModelPost: List<AttachmentUI>,
    listImageNew: List<String>,
    focusHelper: FocusRequester,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onDeleteImageApi: (AttachmentUI) -> Unit,
    onDeleteImageLocal: (String) -> Unit,
) {
    val brush = Brush.radialGradient(colorStops = arrayOf(Pair(0.5F, ThemeApp.colors.onBackground),
        Pair(0.6F, ThemeApp.colors.background.copy(0.0F))))
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(DimApp.screenPadding),
    ) {

        item {
            TextFieldApp(modifier = Modifier
                .focusRequester(focusHelper)
                .fillMaxWidth()
                .widthIn(min = DimApp.widthTextViewSize),
                value = text,
                isIndicatorOn = false,
                onValueChange = onTextChange,
                placeholder = { Text(text = TextApp.textAnythingNew) })
        }

        items(listImageInNetworkModelPost, key = { it.id }) { item ->
            BoxImageLoad(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding)
                .clipToBounds(),
                contentScale = ContentScale.FillWidth,
                image = item.url) {
                Box(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(DimApp.iconSizeTouchStandard)
                    .background(brush = brush)
                    .clip(CircleShape)
                    .clickableRipple { onDeleteImageApi.invoke(item) },
                    contentAlignment = Alignment.Center) {
                    BoxImageRowRes(modifier = Modifier.size(DimApp.iconSizeStandard),
                        colorFilter = ColorFilter.tint(ThemeApp.colors.background),
                        image = R.raw.ic_close)
                }
            }
        }

        items(listImageNew, key = { it }) { item ->
            BoxImageLoad(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding)
                .clipToBounds(),
                contentScale = ContentScale.FillWidth,
                image = item) {
                Box(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(DimApp.iconSizeTouchStandard)
                    .background(brush = brush)
                    .clip(CircleShape)
                    .clickableRipple { onDeleteImageLocal.invoke(item) },
                    contentAlignment = Alignment.Center) {
                    BoxImageRowRes(modifier = Modifier.size(DimApp.iconSizeStandard),
                        colorFilter = ColorFilter.tint(ThemeApp.colors.background),
                        image = R.raw.ic_close)
                }
            }
        }
    }
}


@Composable
private fun PanelTopContentPost(
    onClickBack: () -> Unit,
    onClickSendPost: () -> Unit,
    enableSend: Boolean,
    title: String,
    textButton: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimApp.heightTopNavigationPanel)
            .shadow(elevation = DimApp.shadowElevation)
            .background(ThemeApp.colors.backgroundVariant),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        IconButtonApp(modifier = Modifier.padding(horizontal = DimApp.screenPadding * .5f),
            onClick = onClickBack) {
            IconApp(painter = rememberImageRaw(R.raw.ic_arrow_back))
        }

        TextBodyLarge(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            textAlign = TextAlign.Start,
            text = title)

        TextButtonApp(enabled = enableSend, onClick = onClickSendPost, text = textButton)
    }
}

@Composable
private fun PanelBottomContentPost(
    onClickAddPhoto: () -> Unit,
    onClickAddVideo: () -> Unit,
    onClickAddFile: () -> Unit,
    onClickAddQuiz: () -> Unit,
    onClickAddSmile: () -> Unit,
    onClickSettings: () -> Unit,
    listTopic: List<TopicUI>,
    chooseTopic: TopicUI?,
    onClickTopic: (TopicUI?) -> Unit,
    onClickPrivate: (Boolean) -> Unit,
    isPrivate: Boolean,
) {

    Column {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(DimApp.heightTopNavigationPanel),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            BoxSpacer()
            DropMenuTopicsPost(
                listTopic = listTopic,
                chooseTopic = chooseTopic,
                onClickTopic = onClickTopic)
            BoxSpacer()
            DropMenuPrivatePost(
                isPrivate = isPrivate,
                onClick = onClickPrivate)
            BoxSpacer()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(DimApp.heightTopNavigationPanel)
                .shadow(elevation = DimApp.shadowElevation)
                .background(ThemeApp.colors.backgroundVariant),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BoxSpacer(.5f)
            IconButtonApp(onClick = onClickAddPhoto) {
                IconApp(painter = rememberImageRaw(R.raw.ic_photo))
            }
            IconButtonApp(onClick = onClickAddVideo) {
                IconApp(painter = rememberImageRaw(R.raw.ic_video))
            }
            /**
             * TODO(" Закомиченны методы
             *     onClickAddFile: () -> Unit,
             *     onClickAddQuiz: () -> Unit,
             *     onClickAddSmile: () -> Unit,
             *     onClickSettings: () -> Unit,
             * ")
             * */
//        IconButtonApp(onClick = onClickAddFile) {
//            IconApp(painter = rememberImageRaw(R.raw.ic_file))
//        }
//        IconButtonApp(onClick = onClickAddQuiz) {
//            IconApp(painter = rememberImageRaw(R.raw.ic_quiz))
//        }

            BoxFillWeight()
//        IconButtonApp(onClick = onClickAddSmile) {
//            IconApp(painter = rememberImageRaw(R.raw.ic_smile))
//        }
//
//        IconButtonApp(onClick = onClickSettings) {
//            IconApp(painter = rememberImageRaw(R.raw.ic_settings))
//        }
            BoxSpacer(.5f)
        }
    }
}


@Composable
private fun DropMenuTopicsPost(
    listTopic: List<TopicUI>,
    chooseTopic: TopicUI?,
    onClickTopic: (TopicUI?) -> Unit,
) {
    var expanded by rememberState { false }
    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {

        ButtonForeDropMenuPost(
            text = chooseTopic?.name ?: TextApp.textSubject,

            onClick = { expanded = !expanded })
        DropdownMenu(
            modifier = Modifier.background(color = ThemeApp.colors.backgroundVariant),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(text = {
                TextButtonStyle(text = TextApp.textNoSubject)
            }, onClick = {
                onClickTopic.invoke(null)
                expanded = false
            })

            listTopic.forEach { topic ->
                DropdownMenuItem(text = {
                    TextButtonStyle(text = topic.name)
                }, onClick = {
                    onClickTopic.invoke(topic)
                    expanded = false
                })
            }
        }
    }
}

@Composable
private fun DropMenuPrivatePost(
    isPrivate: Boolean,
    onClick: (Boolean) -> Unit,
) {
    var expanded by rememberState { false }
    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {

        ButtonForeDropMenuPost(
            text = if (isPrivate) TextApp.textDroidOnly else TextApp.textVisibleToAll,
            onClick = { expanded = !expanded })

        DropdownMenu(
            modifier = Modifier.background(color = ThemeApp.colors.backgroundVariant),
            expanded = expanded,
            onDismissRequest = { expanded = false },

            ) {
            DropdownMenuItem(text = {
                TextButtonStyle(text = TextApp.textDroidOnly)
            }, onClick = {
                onClick.invoke(true)
                expanded = false
            })
            DropdownMenuItem(text = {
                TextButtonStyle(text = TextApp.textVisibleToAll)
            }, onClick = {
                onClick.invoke(false)
                expanded = false
            })
        }
    }
}

@Composable
private fun ButtonForeDropMenuPost(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Row(modifier = modifier
        .clip(ThemeApp.shape.smallAll)
        .clickableRipple { onClick.invoke() }
        .padding(start = DimApp.screenPadding * .5f)) {
        TextTitleSmall(text = text, maxLines= 1)
        IconApp(painter = rememberImageRaw(id = R.raw.ic_arrow_drop))
    }
}




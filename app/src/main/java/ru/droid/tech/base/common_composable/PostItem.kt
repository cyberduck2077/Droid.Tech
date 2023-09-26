package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import ru.droid.tech.R
import ru.droid.tech.base.extension.clickableNoRipple
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.AttachmentUI
import ru.data.common.models.local.maps.CommentUI
import ru.data.common.models.res.TextApp

@Composable
fun ItemsPost(
    modifier: Modifier = Modifier,
    avatar: String?,
    name: String,
    lastVisited: String,
    countLikes: Int,
    countComments: Int,
    imageList: List<AttachmentUI>,
    description: String,
    isLike: Boolean,
    isYourPost: Boolean = true,
    onClickLike: (Boolean) -> Unit,
    onClickLink: (String) -> Unit,
    onClickUser: () -> Unit,
    onRemake: () -> Unit = {},
    onDelete: () -> Unit = {},
    onToComplain: () -> Unit = {},
    onSaveToYourFiles: () -> Unit = {},
    onClickComment: () -> Unit,
    onClickShare: () -> Unit,
    onClickImage: (List<AttachmentUI>, Int) -> Unit
) {
    var expandedMero by rememberState { false }
    var like by rememberState(isLike) { isLike }
    var inCountLikes by rememberState(countLikes) { countLikes }

    var isOverflowHeight by remember { mutableStateOf(false) }
    var isOverStart by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = isOverflowHeight, block = {
        if (isOverflowHeight) isOverStart = true
    })
    var isClickFull by remember { mutableStateOf(false) }

    val painterLike = when (like) {
        true  -> rememberImageRaw(R.raw.ic_heart_solid)
        false -> rememberImageRaw(R.raw.ic_heart_outline)
    }

    Column(
        modifier = modifier
            .padding(vertical = DimApp.shadowElevation)
            .fillMaxWidth()
            .shadow(
                elevation = DimApp.shadowElevation,
                shape = ThemeApp.shape.mediumAll
            )
            .background(color = ThemeApp.colors.backgroundVariant)

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickableNoRipple { onClickUser.invoke() }
                .padding(horizontal = DimApp.screenPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            BoxImageLoad(
                modifier = Modifier
                    .padding(end = DimApp.screenPadding)
                    .size(DimApp.iconSizeBig)
                    .clip(CircleShape),
                drawableError = R.drawable.stab_avatar,
                drawablePlaceholder = R.drawable.stab_avatar,
                image = avatar
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                TextTitleSmall(text = name, maxLines = 1)
                TextBodyMedium(text = lastVisited)
            }
            IconButtonApp(
                modifier = Modifier,
                onClick = { expandedMero = !expandedMero }
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_mero_vert))
            }
            if (isYourPost)
                DropMenuInYourPost(
                    expanded = expandedMero,
                    onDismiss = { expandedMero = !expandedMero },
                    onRemake = onRemake,
                    onDelete = onDelete
                )
            if (!isYourPost)
                DropMenuInNotYourPost(
                    expanded = expandedMero,
                    onDismiss = { expandedMero = !expandedMero },
                    onToComplain = onToComplain,
                    onSaveToYourFiles = onSaveToYourFiles
                )

        }
        if (imageList.isNotEmpty()) {
            BoxImageItemRibbon(
                imageList = imageList,
                onClickImage = { attach ->
                    onClickImage.invoke(imageList, imageList.indexOfFirst { it.id == attach.id })
                }
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = DimApp.screenPadding)
        ) {
            if (description.isNotEmpty()) {
                TextLinksWeb(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(DimApp.starsPadding),
                    text = description,
                    style = ThemeApp.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (!isClickFull) 5 else Int.MAX_VALUE,
                    onClick = onClickLink,
                    onTextLayout = { result ->
                        isOverflowHeight = result.didOverflowHeight
                    },
                )
            }

            if (isOverStart) {
                Text(modifier = Modifier
                    .clickableRipple { isClickFull = !isClickFull }
                    .padding(start = DimApp.textPaddingMin),
                    text = if (isClickFull) TextApp.buttonGoneText else TextApp.buttonViewNextText,
                    color = ThemeApp.colors.primary,
                    style = ThemeApp.typography.button)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = DimApp.screenPadding, start = DimApp.screenPadding / 2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButtonApp(
                modifier = Modifier,
                onClick = {
                    onClickLike(!isLike)
                    like = !isLike
                    if (like) inCountLikes += 1 else inCountLikes -= 1
                }
            ) {
                IconApp(painter = painterLike)
            }
            if (inCountLikes > 0) TextBodyMedium(text = inCountLikes.toString())

            IconButtonApp(
                modifier = Modifier,
                onClick = onClickComment
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_comment))
            }
            if (countComments > 0) TextBodyMedium(text = countComments.toString())

            IconButtonApp(
                modifier = Modifier,
                onClick = onClickShare
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_share))
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
        }
    }
}


@Composable
fun BoxImageItemRibbon(
    imageList: List<AttachmentUI>,
    onClickImage: (AttachmentUI) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(DimApp.screenPadding)
            .aspectRatio(1f)
            .fillMaxWidth()
            .clip(ThemeApp.shape.smallAll),
        contentAlignment = Alignment.Center
    ) {

        when (imageList.size) {
            1    -> BoxImageLoad(
                modifier = Modifier
                    .fillMaxWidth()
                    .clipToBounds()
                    .clickableRipple {
                        imageList
                            .firstOrNull()
                            ?.let(onClickImage)
                    },
                sizeToIntrinsics = false,

                image = imageList.firstOrNull()?.url
            )

            2    -> Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DimApp.lineWidthBorderProfile)
            ) {
                imageList.forEach {
                    BoxImageLoad(
                        image = it.url,
                        modifier = Modifier
                            .clipToBounds()
                            .weight(1f)
                            .clickableRipple {
                                it.let(onClickImage)
                            },
                        sizeToIntrinsics = false
                    )
                }
            }

            3    -> Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(DimApp.lineWidthBorderProfile)
                ) {
                    imageList.take(2).forEach {
                        BoxImageLoad(
                            image = it.url,
                            modifier = Modifier
                                .clipToBounds()
                                .weight(1f)
                                .clickableRipple {
                                    it.let(onClickImage)
                                },
                            sizeToIntrinsics = false
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    BoxImageLoad(
                        image = imageList.getOrNull(2)?.url,
                        modifier = Modifier
                            .clipToBounds()
                            .weight(1f)
                            .clickableRipple {
                                imageList
                                    .getOrNull(2)
                                    ?.let(onClickImage)
                            },
                        sizeToIntrinsics = false
                    )
                }
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(
                        DimApp.lineWidthBorderProfile
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(DimApp.lineWidthBorderProfile)
                    ) {
                        imageList.take(2).forEach {
                            BoxImageLoad(
                                image = it.url,
                                modifier = Modifier
                                    .clipToBounds()
                                    .weight(1f)
                                    .clickableRipple {
                                        it.let(onClickImage)
                                    },
                                sizeToIntrinsics = false
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(DimApp.lineWidthBorderProfile)
                    ) {

                        BoxImageLoad(
                            image = imageList.getOrNull(2)?.url,
                            modifier = Modifier
                                .clipToBounds()
                                .weight(1f)
                                .clickableRipple {
                                    imageList
                                        .getOrNull(2)
                                        ?.let(onClickImage)
                                },
                            sizeToIntrinsics = false
                        )

                        BoxImageLoad(
                            image = imageList.getOrNull(3)?.url,
                            modifier = Modifier
                                .clipToBounds()
                                .weight(1f),
                            modifierOnImage = Modifier
                                .background(ThemeApp.colors.background.copy(.5f))
                                .clickableRipple {
                                    imageList
                                        .getOrNull(3)
                                        ?.let(onClickImage)
                                },
                            sizeToIntrinsics = false
                        ) {
                            TextButtonStyle(
                                modifier = Modifier.align(Alignment.Center),
                                text = TextApp.holderShowAllS
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemsPostWithComments(
    modifier: Modifier = Modifier,
    avatar: String?,
    name: String,
    lastVisited: String,
    countLikes: Int,
    countComments: String,
    countCommentsText: String,
    imageList: List<AttachmentUI>,
    description: String,
    isLike: Boolean,
    onClickLike: (Boolean) -> Unit,
    onClickLink: (String) -> Unit,
    onClickAnswer: (Int) -> Unit,
    onRemake: () -> Unit,
    onDelete: () -> Unit,
    onClickShare: () -> Unit,
    comments: List<CommentUI>,
    onClickImage: (List<AttachmentUI>, Int) -> Unit
) {
    var expandedMero by rememberState { false }
    var like by rememberState(isLike) { isLike }
    var inCountLikes by rememberState(countLikes) { countLikes }
    var isOverflowHeight by remember { mutableStateOf(false) }
    var isOverStart by remember { mutableStateOf(false) }
    var state = rememberLazyListState()
    LaunchedEffect(key1 = isOverflowHeight, block = {
        if (isOverflowHeight) isOverStart = true
    })
    var isClickFull by remember { mutableStateOf(false) }

    val painterLike = when (like) {
        true  -> rememberImageRaw(R.raw.ic_heart_solid)
        false -> rememberImageRaw(R.raw.ic_heart_outline)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(color = ThemeApp.colors.backgroundVariant),
        state = state,
    ) {

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                BoxImageLoad(
                    modifier = Modifier
                        .padding(top = DimApp.screenPadding)
                        .padding(horizontal = DimApp.screenPadding)
                        .size(DimApp.iconSizeBig)
                        .clip(CircleShape),
                    drawableError = R.drawable.stab_avatar,
                    drawablePlaceholder = R.drawable.stab_avatar,
                    image = avatar
                )

                Column(
                    modifier = Modifier
                        .padding(end = DimApp.screenPadding)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    TextTitleSmall(
                        text = name, maxLines = 1,
                    )
                    TextBodyMedium(text = lastVisited)
                }
                IconButtonApp(
                    modifier = Modifier,
                    onClick = { expandedMero = !expandedMero }
                ) {
                    IconApp(painter = rememberImageRaw(R.raw.ic_mero_vert))
                }
                DropMenuInYourPost(
                    expanded = expandedMero,
                    onDismiss = { expandedMero = !expandedMero },
                    onRemake = onRemake,
                    onDelete = onDelete
                )
            }

            if (imageList.isNotEmpty()) {
                BoxImageItemRibbon(
                    imageList = imageList,
                    onClickImage = { attach ->
                        onClickImage.invoke(
                            imageList,
                            imageList.indexOfFirst { it.id == attach.id })
                    }
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = DimApp.screenPadding)
            ) {
                TextLinksWeb(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(DimApp.starsPadding),
                    text = description,
                    style = ThemeApp.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (!isClickFull) 5 else Int.MAX_VALUE,
                    onClick = onClickLink,
                    onTextLayout = { result ->
                        isOverflowHeight = result.didOverflowHeight
                    },
                )

                if (isOverStart) {
                    Text(modifier = Modifier
                        .clickableRipple { isClickFull = !isClickFull }
                        .padding(start = DimApp.textPaddingMin),
                        text = if (isClickFull) TextApp.buttonGoneText else TextApp.buttonViewNextText,
                        color = ThemeApp.colors.primary,
                        style = ThemeApp.typography.button)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = DimApp.screenPadding, start = DimApp.screenPadding / 2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButtonApp(
                    modifier = Modifier,
                    onClick = {
                        onClickLike(!isLike)
                        like = !isLike
                        if (like) inCountLikes += 1 else inCountLikes -= 1
                    }
                ) {
                    IconApp(painter = painterLike)
                }
                TextBodyMedium(text = inCountLikes.toString())
                IconButtonApp(
                    enabled = false,
                    onClick = {}
                ) {
                    IconApp(painter = rememberImageRaw(R.raw.ic_comment))
                }

                TextBodyMedium(text = countComments)
                IconButtonApp(
                    modifier = Modifier,
                    onClick = onClickShare
                ) {
                    IconApp(painter = rememberImageRaw(R.raw.ic_share))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }

        if (comments.isNotEmpty()) {
            item {
                FillLineHorizontal()
                TextBodyMedium(
                    text = countCommentsText,
                    color = ThemeApp.colors.textLight,
                    modifier = Modifier.padding(DimApp.screenPadding)
                )
            }
        }

        items(items = comments, key = { it.id }) { mComment ->
            Comment(
                name = mComment.user.getNameAndLastName(),
                text = mComment.text,
                date = mComment.createdHuman,
                avatar = mComment.user.avatar,
                modifier = Modifier.fillMaxWidth(),
                onClickAnswer = { onClickAnswer.invoke(mComment.id) },
                isParentExist = mComment.parentId != null
            )
        }
    }
}
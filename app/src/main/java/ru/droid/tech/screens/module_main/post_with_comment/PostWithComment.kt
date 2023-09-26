package ru.droid.tech.screens.module_main.post_with_comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlinx.coroutines.launch
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.DialogYesOrNo
import ru.droid.tech.base.common_composable.DialogZoomImagePager
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.ItemsPostWithComments
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextFieldApp
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.domain.memory.gDMessage
import ru.data.common.models.local.maps.AttachmentUI
import ru.data.common.models.local.maps.CommentUI
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.util.formatTimeElapsed

class PostWithComment(private val postId:Int) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<PostWithCommentModel>()
        val postStart by rememberState { postId }
        val post by model.post.collectAsState()
        val comments by model.comments.collectAsState()

        BackPressHandler(onBackPressed = model::goBackStack)
        LifecycleEffect(onStarted = {
            model.getPost(postId = postStart)
        })
        PostScr(
            postWithComment = post,
            comments = comments,
            onClickBack = model::goBackStack,
            onClickLike = model::likePost,
            onClickLink = {
                /**TODO()*/
                gDMessage("Stub")
            },
            onClickShare = {
                /**TODO()*/
                gDMessage("Stub")
            },
            onClickRemake = model::goToRemakeMyPost,
            onClickDelete =  model::deleteMyPost,
            onClickSendMessage = model::sendMessage
        )
    }
}

@Composable
fun PostScr(
    postWithComment: PostWithCommentUI?,
    comments: List<CommentUI>,
    onClickBack: () -> Unit,
    onClickLike: () -> Unit,
    onClickLink: () -> Unit,
    onClickShare: () -> Unit,
    onClickRemake: () -> Unit,
    onClickDelete: () -> Unit,
    onClickSendMessage: (String, Int?) -> Unit,
) {
    var textCommentValue by rememberState { TextFieldValue() }
    var textCommentParentId by rememberState<Int?> { null }

    var dialogPagerImage by rememberState<Pair<List<AttachmentUI>, Int>?> { null }
    val focus = LocalFocusManager.current
    val registerFocus = remember { FocusRequester() }
    var isFocused by rememberState { false }
    var dialogDeleteMyPost by rememberState { false }
    val scope = rememberCoroutineScope()

    val clearComments = remember() {
        {
            scope.launch {
                textCommentValue = TextFieldValue()
                textCommentParentId = null
            }
        }
    }

    val sendMessage = remember() {
        {
            val text = textCommentValue.text
            if (text.isNotEmpty()) {
                onClickSendMessage.invoke(text, textCommentParentId)
                clearComments.invoke()
            }
            focus.clearFocus()
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        registerFocus.requestFocus()
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
    ) {

        PanelNavBackTop(
            modifier = Modifier,
            onClickBack = onClickBack,
            text = TextApp.textPost,
        )

        ItemsPostWithComments(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            avatar = postWithComment?.user?.avatar,
            name = postWithComment?.user?.getNameAndLastName() ?: "",
            lastVisited = postWithComment?.user?.lastVisit?.formatTimeElapsed() ?: "",
            countLikes = postWithComment?.votesCount ?: 0,
            countComments = postWithComment?.commentsCount.toString(),
            countCommentsText = postWithComment?.getCountCommentText() ?: "",
            imageList = postWithComment?.attachments ?: listOf(),
            description = postWithComment?.text ?: "",
            isLike = postWithComment?.isVote ?: false,
            onClickLike = { onClickLike.invoke() },
            onClickLink = { onClickLink.invoke() },
            onRemake = { onClickRemake.invoke() },
            onDelete = {
                dialogDeleteMyPost = true
                       },
            onClickShare = { onClickShare.invoke() },
            comments = comments,
            onClickImage = { list, index ->
                dialogPagerImage = Pair(list, index)
            },
            onClickAnswer = {
                focus.clearFocus()
                registerFocus.requestFocus()
                textCommentParentId = it
            },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = DimApp.shadowElevation,
                )
                .background(ThemeApp.colors.backgroundVariant)
        ) {
            TextFieldApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(registerFocus)
                    .onFocusChanged { focus ->
                        isFocused = focus.isFocused
                    },
                value = textCommentValue,
                onValueChange = {
                    textCommentValue = it
                },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButtonApp(
                            onClick = sendMessage
                        ) {
                            IconApp(painter = rememberImageRaw(R.raw.ic_send))
                        }
                    }
                },
                placeholder = {
                    Text(text = textCommentParentId?.let {
                        TextApp.textAnswerComment } ?: TextApp.textComment)
                },
                keyboardActions = KeyboardActions(onDone = {
                    focus.clearFocus()
                    clearComments.invoke()
                }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    if (isFocused) {
                        IconButtonApp(
                            onClick = {
                                focus.clearFocus()
                                clearComments.invoke()
                            }
                        ) {
                            IconApp(painter = rememberImageRaw(R.raw.ic_close))
                        }
                    }
                }
            )
        }
    }

    if (dialogDeleteMyPost){
        DialogYesOrNo(
            onDismiss = { dialogDeleteMyPost = false },
            onClick = { onClickDelete.invoke() },
            title = TextApp.textDeletingPost,
            body = TextApp.formatReallyWantDeletingPost(postWithComment?.text?.take(20))
        )
    }

    dialogPagerImage?.let { attach ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            DialogZoomImagePager(
                images = attach.first.map { it.url },
                dismiss = { dialogPagerImage = null },
                initPageNumb = attach.second,
            )
        }
    }
}
package ru.droid.tech.base.common_composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp

fun LazyListScope.handleStateError(state: LoadState) {
    item {
        ContentErrorLoad(visible = state is LoadState.Error)
    }
}

fun <LIST: Any>LazyListScope.handleStateEmpty(list: LazyPagingItems<LIST> ) {
    if (list.loadState.refresh is LoadState.NotLoading
        && list.itemCount == 0) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextTitleSmall(text = TextApp.textItEmpty)
            }

        }
    }
}

fun <LIST: Any>LazyListScope.handleStateProgress(list: LazyPagingItems<LIST> ) {
    if (list.loadState.refresh is LoadState.Loading
        || list.loadState.append is LoadState.Loading) {
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

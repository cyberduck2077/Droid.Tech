package ru.droid.tech.base.util.picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.droid.tech.base.util.rememberDerivedState
import ru.droid.tech.base.util.rememberState
import kotlin.math.abs
import kotlin.math.ceil


@Composable
fun <ItemT> InfiniteItemsPicker(
    modifier: Modifier,
    items: List<ItemT>,
    indexInit: Int,
    onItemSelected: (ItemT?) -> Unit = {},
    heightLazyColumn: Dp = 230.dp,
    heightItem: Dp = heightLazyColumn * 0.15f,
    countItem: Int = Int.MAX_VALUE,
    itemPicker: @Composable BoxScope.(ItemT, VisiblyItem) -> Unit,
) {

    val chooseIndex: Int by rememberState {
        val half = countItem / 2
        val countViewItems =  ceil(heightLazyColumn / heightItem).toInt()
        val itemsIndexInHalf = half % items.size
        val firstElementInMax = half - itemsIndexInHalf
        val chooseIndexInit = indexInit + firstElementInMax  - (countViewItems /2 ) - 1
        chooseIndexInit
    }
    val listState = rememberLazyListState(chooseIndex)
    var currentValue by rememberState(items) { items.getOrNull(chooseIndex % items.size) }
    val firstVisibleItemIndex by rememberDerivedState { listState.firstVisibleItemIndex }
    val visibleItems by rememberDerivedState { listState.layoutInfo.visibleItemsInfo.size }
    val isScrollInProgress by rememberDerivedState { listState.isScrollInProgress }

    LaunchedEffect(!isScrollInProgress) {
        onItemSelected(currentValue)
        listState.animateScrollToItem(index = firstVisibleItemIndex)
    }

    Box(
        modifier = modifier.height(heightLazyColumn),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            content = {
                items(count = countItem, itemContent = { indexValue ->
                    val item by rememberState(indexValue, items) {
                        val index = indexValue % items.size
                        items.getOrNull(index)
                    }

                    val visiblyItem by rememberState(
                        firstVisibleItemIndex,
                        visibleItems,
                        indexValue
                    ) {
                       val position = roundVisibleItemIndex(firstVisibleItemIndex, visibleItems, indexValue)
                        val visibly = VisiblyItem.getVisibly(position)
                        if (visibly == VisiblyItem.FIRST) {
                            currentValue = item
                        }
                        visibly
                    }

                    Box(
                        modifier = Modifier
                            .height(heightItem)
                            .offset(y = -heightItem * 1.5f)

                    ) {
                        item?.let { itItem -> itemPicker.invoke(this, itItem, visiblyItem) }
                    }
                })
            }
        )
    }
}

private fun roundVisibleItemIndex(
    firstVisibleItemIndex: Int,
    visibleItems: Int,
    indexValue: Int,
): Int {
    val numbVisibly = firstVisibleItemIndex + (visibleItems * 0.5) - indexValue
    return ceil(numbVisibly).toInt()
}

enum class VisiblyItem {
    FIRST,
    SECOND,
    THIRD,
    FORTH,
    OTHER;

    companion object {
        fun getVisibly(numb: Int) = when (abs(numb)) {
            0 -> FIRST
            1 -> SECOND
            2 -> THIRD
            3 -> FORTH
            else -> OTHER
        }
    }
}
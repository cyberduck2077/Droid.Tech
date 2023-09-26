package ru.droid.tech.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class BasePagination<DATA_API : Any>(
    private val scope: CoroutineScope? = null,
    private val pageSize : Int = 30,
    private val unit: suspend (
        page: Int,
        listStatus:  (
            hasNext: Boolean?,
            hasPrev: Boolean?,
            codeResponse: Int,
        ) -> Unit
    ) -> List<DATA_API>,
) {
    private var refreshPager: () -> Unit = {}
    private val listPager
        get() = Pager(
            config = PagingConfig(pageSize = pageSize),
            initialKey = 1,
            pagingSourceFactory = {
                object : PagingSource<Int, DATA_API>() {
                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DATA_API> {
                        val pageNotNull = params.key ?: 1
                        val pageCurrent = if (pageNotNull < 1) 1 else pageNotNull
                        var hasNext = false
                        var hasPrev = false
                        var codeResponse = 200
                        val response = unit(pageCurrent) { hasNextUnit: Boolean?, hasPrevUnit: Boolean?, codeResponseUnit: Int ->
                            hasNext = hasNextUnit ?: false
                            hasPrev = hasPrevUnit ?: false
                            codeResponse = codeResponseUnit
                        }

                        if (codeResponse > 300) return LoadResult.Error(Throwable(""))

                        val prevKey = if (hasPrev && pageCurrent > 1) pageCurrent - 1 else null
                        val nextKey = if (hasNext) pageCurrent + 1 else null
                        return LoadResult.Page(
                            data = response ,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    }

                    override val keyReuseSupported: Boolean = true

                    override fun getRefreshKey(state: PagingState<Int, DATA_API>): Int {
                        return state.anchorPosition?.let { anchorPosition ->
                            state.closestPageToPosition(anchorPosition)?.prevKey ?: 1
                        } ?: 1
                    }
                }
            }
        ).flow.let { flow -> scope?.let { flow.cachedIn(scope) } }
    private var childFlow: Flow<PagingData<DATA_API>>? = null
        get() {
            if (field == null) field = listPager
            return field
        }

    fun refreshFlow() {
        refreshPager.invoke()
    }

    @Composable
    fun getFlow(): LazyPagingItems<DATA_API> {
        val flow = remember(childFlow) { childFlow!! }
        return flow.collectAsLazyPagingItems().also { paging ->
            refreshPager = { paging.refresh() }
        }
    }

    fun clearFlow() {
        childFlow = null
    }
}
package org.mp.doctorsearchapp.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener


/**
 * Pagination class to add more items to the list when reach the last item.
 */
abstract class PaginationScrollListener(var layoutManager: LinearLayoutManager) : OnScrollListener() {

    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
    abstract fun getTotalPageCount(): Int
    protected abstract fun loadMoreItems()
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        var visibleItemCount : Int = layoutManager.childCount
        var totalItemCount : Int = layoutManager.itemCount
        var firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage())
        {
            if ((visibleItemCount+firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >=0)
            {
                loadMoreItems()
            }
        }
    }

}
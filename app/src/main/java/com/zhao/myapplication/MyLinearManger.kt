package com.zhao.myapplication

import androidx.recyclerview.widget.RecyclerView

/**
 *创建时间： 2022/7/27
 *编   写：  zjf
 *页面功能:
 */
class MyLinearManger : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
    }

}
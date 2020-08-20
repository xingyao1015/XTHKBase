package com.xthk.base.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("bind_recyclerView_adapter")
fun bindRecyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
    adapter?.apply {
        recyclerView.adapter = this
    }
}

@BindingAdapter("bind_recyclerView_item_decoration")
fun bindRecyclerViewAdapter(
    recyclerView: RecyclerView,
    itemDecoration: RecyclerView.ItemDecoration?
) {
    itemDecoration?.apply {
        recyclerView.addItemDecoration(itemDecoration)
    }
}

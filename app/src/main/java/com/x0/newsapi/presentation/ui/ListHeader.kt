package com.x0.newsapi.presentation.ui

import android.content.Context
import android.view.View
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractHeaderItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_header.listHeader

class ListHeader(private val headerStringId: Int, private val headerLayoutId: Int) :
    AbstractHeaderItem<ListHeader.ViewHolder>() {

    override fun equals(other: Any?): Boolean {
        if (other is ListHeader) return this.headerStringId == other.headerStringId
        return false
    }

    override fun getLayoutRes(): Int = headerLayoutId

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): ViewHolder =
        ViewHolder(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: ViewHolder,
        position: Int,
        payloads: List<Any>
    ) = holder.bind(headerStringId)

    override fun hashCode(): Int = headerStringId.hashCode()

    class ViewHolder(override val containerView: View?, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(containerView, adapter, true), LayoutContainer {

        private val context: Context = itemView.context

        fun bind(headerStringId: Int) {
            listHeader.text = context.getString(headerStringId)
        }
    }
}

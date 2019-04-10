package com.x0.newsapi.presentation.ui

import android.view.View
import com.x0.newsapi.R
import com.x0.newsapi.common.clickWithDebounce
import com.x0.newsapi.data.model.sources.Source
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import io.reactivex.subjects.Subject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_source.descriptionText
import kotlinx.android.synthetic.main.item_source.sourceTitleText
import kotlinx.android.synthetic.main.item_source.urlText

class SourcesListItem(
    listHeader: ListHeader,
    private val source: Source,
    private val openSourceObserver: Subject<Source>
) : AbstractSectionableItem<SourcesListItem.ViewHolder, ListHeader>(listHeader) {

    override fun getLayoutRes(): Int = R.layout.item_source

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
    ) = holder.bind(
        source,
        openSourceObserver
    )

    class ViewHolder(override var containerView: View?, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(containerView, adapter), LayoutContainer {

        fun bind(
            source: Source,
            openSourceObserver: Subject<Source>
        ) {
            setSourceTitleText(source)
            setDescriptionText(source)
            setUrlText(source)

            setOpenArticleDetails(source, openSourceObserver)
        }

        private fun setSourceTitleText(source: Source) = with(sourceTitleText) {
            text = source.name
        }

        private fun setDescriptionText(source: Source) = with(descriptionText) {
            text = source.description
        }

        private fun setUrlText(source: Source) = with(urlText) {
            text = source.url
        }

        private fun setOpenArticleDetails(
            source: Source,
            openSourceObserver: Subject<Source>
        ) = containerView?.clickWithDebounce {
            openSourceObserver.onNext(source)
        }
    }

    override fun equals(other: Any?): Boolean =
        if (other is SourcesListItem) source == other.source
        else false

    override fun hashCode(): Int {
        return source.hashCode()
    }
}
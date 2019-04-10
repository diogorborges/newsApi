package com.x0.newsapi.presentation.ui

import android.view.View
import com.x0.newsapi.common.formattedDate
import com.x0.newsapi.common.gone
import com.x0.newsapi.common.setThumbnailImage
import com.x0.newsapi.data.model.news.Article
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import io.reactivex.subjects.Subject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_generic.authorText
import kotlinx.android.synthetic.main.item_generic.descriptionText
import kotlinx.android.synthetic.main.item_generic.publishedDayText
import kotlinx.android.synthetic.main.item_generic.thumbnailImage
import kotlinx.android.synthetic.main.item_generic.titleText


class GenericListItem(
    listHeader: ListHeader,
    private val article: Article,
    private val loadMoreObserver: Subject<Article>,
    private val listSize: Int
) : AbstractSectionableItem<GenericListItem.ViewHolder, ListHeader>(listHeader) {

    override fun getLayoutRes(): Int = com.x0.newsapi.R.layout.item_generic

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
        article,
        loadMoreObserver,
        position,
        listSize
    )

    class ViewHolder(override var containerView: View?, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(containerView, adapter), LayoutContainer {

        fun bind(
            article: Article,
            loadMoreNewsObserver: Subject<Article>,
            position: Int,
            listSize: Int
        ) {
            setImageView(article)
            setTitleText(article)
            setDescriptionText(article)
            setAuthorText(article)
            setPublishedDayText(article)

            setShouldLoadMoreNews(position, listSize, loadMoreNewsObserver, article)
        }

        private fun setImageView(article: Article) = with(thumbnailImage) {
            article.urlToImage?.let {
                setThumbnailImage(this, it)
            }
        }

        private fun setShouldLoadMoreNews(
            position: Int,
            listSize: Int,
            loadMoreNewsObserver: Subject<Article>,
            article: Article
        ) {
            if (position == listSize)
                loadMoreNewsObserver.onNext(article)
        }

        private fun setTitleText(article: Article) = with(titleText) {
            text = article.source.name
        }

        private fun setDescriptionText(article: Article) = with(descriptionText) {
            article.description?.let {
                text = article.description
            } ?: gone()
        }

        private fun setAuthorText(article: Article) = with(authorText) {
            article.author?.let {
                text = it
            }
        }

        private fun setPublishedDayText(article: Article) = with(publishedDayText) {
            try {
                text = formattedDate(article.publishedAt)
            } catch (e: Exception) {
                gone()
            }
        }
    }

    override fun equals(other: Any?): Boolean =
        if (other is GenericListItem) listSize == other.listSize
        else false

    override fun hashCode(): Int = listSize
}
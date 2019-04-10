package com.x0.newsapi.presentation.ui

import android.view.View
import com.x0.newsapi.R
import com.x0.newsapi.common.clickWithDebounce
import com.x0.newsapi.common.gone
import com.x0.newsapi.data.model.news.Article
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import io.reactivex.subjects.Subject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_news.descriptionText
import kotlinx.android.synthetic.main.item_news.sourceTitleText
import kotlinx.android.synthetic.main.item_news.urlText

class NewsListItem(
    listHeader: ListHeader,
    private val article: Article,
    private val openDetailsObserver: Subject<Article>,
    private val loadMoreObserver: Subject<Article>,
    private val listSize: Int
) : AbstractSectionableItem<NewsListItem.ViewHolder, ListHeader>(listHeader) {

    override fun getLayoutRes(): Int = R.layout.item_news

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
        openDetailsObserver,
        loadMoreObserver,
        position,
        listSize
    )

    class ViewHolder(override var containerView: View?, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(containerView, adapter), LayoutContainer {

        fun bind(
            article: Article,
            openNewsDetailsObserver: Subject<Article>,
            loadMoreNewsObserver: Subject<Article>,
            position: Int,
            listSize: Int
        ) {
            setSourceTitleText(article)
            setDescriptionText(article)
            setUrlText(article)

            setOpenArticleDetails(article, openNewsDetailsObserver)

            setShouldLoadMoreNews(position, listSize, loadMoreNewsObserver, article)
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

        private fun setSourceTitleText(article: Article) = with(sourceTitleText) {
            text = article.source.name
        }

        private fun setDescriptionText(article: Article) = with(descriptionText) {
            article.description?.let {
                text = article.description
            } ?: gone()
        }

        private fun setUrlText(article: Article) = with(urlText) {
            text = article.url
        }

        private fun setOpenArticleDetails(
            article: Article,
            openNewsDetailsObserver: Subject<Article>
        ) = containerView?.clickWithDebounce {
            openNewsDetailsObserver.onNext(article)
        }
    }

    override fun equals(other: Any?): Boolean =
        if (other is NewsListItem) listSize == other.listSize
        else false

    override fun hashCode(): Int = listSize

}
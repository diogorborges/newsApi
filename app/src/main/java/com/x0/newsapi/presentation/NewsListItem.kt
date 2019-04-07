package com.x0.newsapi.presentation

import android.view.View
import com.x0.newsapi.R
import com.x0.newsapi.data.model.news.Article
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import io.reactivex.subjects.Subject

class NewsListItem(
    listHeader: ListHeader,
    private val article: Article,
    private val openNewsDetailsObserver: Subject<Article>
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
        openNewsDetailsObserver
    )

    class ViewHolder(override var containerView: View?, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(containerView, adapter), LayoutContainer {

        fun bind(
            article: Article,
            openNewsDetailsObserver: Subject<Article>
        ) {
            setSourceTitleText(article)
            setDescriptionText(article)
            setUrlText(article)

            setOpenArticleDetails(article, openNewsDetailsObserver)
        }


        private fun setSourceTitleText(article: Article) = with(sourceTitleText) {
            text = article.source.name
        }

        private fun setDescriptionText(article: Article) = with(descriptionText) {
            article.description?.let {
                text = article.url
            } ?: ""
        }

        private fun setUrlText(article: Article) = with(urlText) {
            text = article.url
        }

        private fun setOpenArticleDetails(
            article: Article,
            openNewsDetailsObserver: Subject<Article>
        ) = clickWithDebounce {
            openNewsDetailsObserver.onNext(article)
        }
    }

    override fun equals(other: Any?): Boolean =
        if (other is NewsListItem) article == other.article
        else false

    override fun hashCode(): Int {
        var result = article.hashCode()
        result = 31 * result + openNewsDetailsObserver.hashCode()
        return result
    }
}
package workshop.akbolatss.tagsnews.screen.news

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.dao.RssFeedItem
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * MVP Presenter for #NewsFragment
 * @see NewsFragment
 */
class NewsPresenter @Inject
constructor() : BasePresenter<NewsView>() {

    internal lateinit var url: String

    @Inject
    lateinit var mApiService: NewsApiService

    /**
     * Show loading state and fetch RSS feed from #url
     */
    fun onLoadNews() {
        view.onShowLoading()

        mApiService.getRss(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rssFeed ->
                    view.onLoadNews(rssFeed)
                    view.onHideLoading()
                }, { e ->
                    if (e is SocketTimeoutException) {
                        view.onTimeout()
                    } else if (e is IOException) {
                        view.onNetworkError()
                    } else {
                        view.onUnknownError(e.message!!)
                    }
                    view.onHideLoading()
                })
    }

    /**
     * Mapping RssItem to RssFeedItem
     */
    fun mapRssFeed(rssItem: RssItem, mRssSourceId: Long?): RssFeedItem {
        val rssFeedItem = RssFeedItem()
        rssFeedItem.title = rssItem.title
        rssFeedItem.link = rssItem.link
        rssFeedItem.pubDate = rssItem.publishDate
        rssFeedItem.image = rssItem.image
        rssFeedItem.description = rssItem.description
        rssFeedItem.rssSourceId = mRssSourceId
        return rssFeedItem
    }
}

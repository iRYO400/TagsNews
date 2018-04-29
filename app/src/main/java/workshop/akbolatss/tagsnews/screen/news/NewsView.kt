package workshop.akbolatss.tagsnews.screen.news

import me.toptas.rssconverter.RssFeed
import workshop.akbolatss.tagsnews.base.BaseView
import workshop.akbolatss.tagsnews.base.ErrorView
import workshop.akbolatss.tagsnews.base.LoadingView

interface NewsView : BaseView, LoadingView, ErrorView {

    fun onLoadNews(rssFeed: RssFeed)
}

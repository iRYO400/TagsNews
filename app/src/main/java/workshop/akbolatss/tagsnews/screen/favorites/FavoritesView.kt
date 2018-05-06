package workshop.akbolatss.tagsnews.screen.favorites

import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.base.BaseView
import workshop.akbolatss.tagsnews.base.ErrorView
import workshop.akbolatss.tagsnews.base.LoadingView

interface FavoritesView : BaseView, LoadingView {

    fun onLoadFavorites(rssItems: List<RssItem>)

}

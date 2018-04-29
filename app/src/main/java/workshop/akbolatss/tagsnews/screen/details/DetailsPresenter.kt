package workshop.akbolatss.tagsnews.screen.details

import javax.inject.Inject

import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.DBRssItemRepository

class DetailsPresenter @Inject
constructor() : BasePresenter<DetailsView>() {

    @Inject
    lateinit var mDbRssItemRepository: DBRssItemRepository

    var mRssItem: RssItem? = null

    var isFavorite = false

    fun onAddToFavorites() {
        if (!isFavorite) {
            mDbRssItemRepository.addRssItem(mRssItem)
            view.onRefreshToolbar(true)
        } else {
            mDbRssItemRepository.removeRssItem(mRssItem?.publishDate)
            view.onRefreshToolbar(false)
        }
    }

    fun onCheckFavorites(pubDate: String): Boolean {
        isFavorite = mDbRssItemRepository.checkIfExists(pubDate)
        view.onRefreshToolbar(isFavorite)
        return isFavorite
    }
}

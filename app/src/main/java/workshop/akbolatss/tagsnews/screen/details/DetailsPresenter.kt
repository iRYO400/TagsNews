package workshop.akbolatss.tagsnews.screen.details

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.DBRssItemRepository
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.RssFeedItem
import workshop.akbolatss.tagsnews.model.dao.RssSource
import javax.inject.Inject

/**
 * MVP Presenter for #DetailsActivity
 * @see DetailsActivity
 */
class DetailsPresenter @Inject
constructor() : BasePresenter<DetailsView>() {

    @Inject
    lateinit var mDbRssItemRepository: DBRssItemRepository

    @Inject
    lateinit var mDbRssSourceRepository: DBRssSourceRepository

    var mRssFeedItem: RssFeedItem? = null

    var isFavorite = false

    /**
     * Add to favorites to DB
     */
    fun onAddToFavorites() {
        if (!isFavorite) {
            mDbRssItemRepository.addRssItem(mRssFeedItem!!)
            view.onRefreshToolbar(true)
        } else {
            mDbRssItemRepository.removeRssItem(mRssFeedItem!!)
            view.onRefreshToolbar(false)
        }
    }

    /**
     * Get RSS source from DB
     */
    fun getRssSource() {
        mDbRssSourceRepository.getById(mRssFeedItem!!.rssSourceId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({rssSource ->
                    view.loadRssSource(rssSource)
                }, {
                })
    }

    /**
     * Check if RSS feed exists in DB
     */
    fun checkInFavorites(): Boolean {
        isFavorite = mDbRssItemRepository.checkIfExists(mRssFeedItem!!)
        view.onRefreshToolbar(isFavorite)
        return isFavorite
    }
}

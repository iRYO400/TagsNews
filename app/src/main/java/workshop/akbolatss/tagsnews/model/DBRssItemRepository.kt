package workshop.akbolatss.tagsnews.model

import io.reactivex.Observable
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.model.dao.RssFeedItem

/**
 * DBRssItemRepository is the class that contains all methods related to work with #RssItem and DataBase.
 * Repository pattern.
 * @see RssFeedItem
 */
class DBRssItemRepository(private val mDaoSession: DaoSession) : RssItemRepository {

    /**
     * Get list of favorite Rss feeds
     */
    override val favorites: Observable<List<RssFeedItem>>
        get() = Observable.fromCallable {
            val rssItemDao = mDaoSession.rssFeedItemDao
            rssItemDao.loadAll()
        }

    /**
     * Save Rss feed to DB (add to favorites)
     */
    override fun addRssItem(rssFeedItem: RssFeedItem) {
        val rssFeedItemDao = mDaoSession.rssFeedItemDao
        rssFeedItemDao.insert(rssFeedItem)
    }

    /**
     * Remove Rss feed from DB (e.g. favorites). Called only if added to favorites
     */
    override fun removeRssItem(rssFeedItem: RssFeedItem) {
        val rssFeedItemDao = mDaoSession.rssFeedItemDao
        rssFeedItemDao.delete(rssFeedItem)
//        val rssFeedItemList = rssFeedItemDao.loadAll()
//
//        for (i in rssFeedItemList.indices) {
//            if (rssFeedItemList[i].pubDate == pubDate) {
//                rssFeedItemDao.delete(rssFeedItemList[i])
//            }
//        }
    }

    /**
     * Check if this Rss feed exists
     */
    override fun checkIfExists(rssFeedItem: RssFeedItem): Boolean {
        val rssFeedItemDao = mDaoSession.rssFeedItemDao
        val loadedItem = rssFeedItemDao.load(rssFeedItem.id)
        return loadedItem != null
//        rssFeedItemDao.queryBuilder().where(RssFeedItemDao.Properties.PubDate.eq())
//        val rssFeedItemList = rssFeedItemDao.loadAll()
//        for (i in rssFeedItemList.indices) {
//            if (rssFeedItemList[i].pubDate == pubDate) {
//                return true
//            }
//        }
//        return false
    }
}

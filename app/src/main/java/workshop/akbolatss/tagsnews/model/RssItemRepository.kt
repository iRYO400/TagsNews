package workshop.akbolatss.tagsnews.model

import io.reactivex.Observable
import workshop.akbolatss.tagsnews.model.dao.RssFeedItem

/**
 * @see DBRssItemRepository
 */
interface RssItemRepository {

    val favorites: Observable<List<RssFeedItem>>

    fun addRssItem(rssFeedItem: RssFeedItem)

    fun removeRssItem(rssFeedItem: RssFeedItem)

    fun checkIfExists(rssFeedItem: RssFeedItem): Boolean
}

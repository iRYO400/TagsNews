package workshop.akbolatss.tagsnews.model

import io.reactivex.Observable
import me.toptas.rssconverter.RssItem

interface RssItemRepository {

    val favorites: Observable<List<RssItem>>

    fun addRssItem(rssItem: RssItem)

    fun removeRssItem(pubDate: String)

    fun checkIfExists(pubDate: String): Boolean
}

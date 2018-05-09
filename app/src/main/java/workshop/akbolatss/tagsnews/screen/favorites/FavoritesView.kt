package workshop.akbolatss.tagsnews.screen.favorites

import workshop.akbolatss.tagsnews.base.BaseView
import workshop.akbolatss.tagsnews.model.dao.RssFeedItem

/**
 * MVP View for #FavoritesView
 * @see FavoritesActivity
 */
interface FavoritesView : BaseView {

    fun onLoadFavorites(rssItems: List<RssFeedItem>)

    fun onNoContent(isEmpty: Boolean)

}

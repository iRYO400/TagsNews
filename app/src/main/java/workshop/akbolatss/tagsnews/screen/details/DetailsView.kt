package workshop.akbolatss.tagsnews.screen.details

import workshop.akbolatss.tagsnews.base.BaseView
import workshop.akbolatss.tagsnews.model.dao.RssSource

/**
 * MVP View for #DetailsView
 * @see DetailsView
 */
interface DetailsView : BaseView {

    fun onShare()

    fun onShareVk()

    fun onShareFb()

    fun onShareTw()

    fun onOpenSource()

    fun onShareWithWebIntent(socialNetworkId: String)

    fun onRefreshToolbar(isFavorite: Boolean)

    fun loadRssSource(rssSource: RssSource?)
}

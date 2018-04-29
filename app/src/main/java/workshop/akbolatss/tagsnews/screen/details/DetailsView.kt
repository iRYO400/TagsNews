package workshop.akbolatss.tagsnews.screen.details

import workshop.akbolatss.tagsnews.base.BaseView

interface DetailsView : BaseView {

    fun onShare()

    fun onShareVk()

    fun onShareFb()

    fun onShareTw()

    fun onOpenSource()

    fun onShareWithWebIntent(socialNetworkId: String)

    fun onRefreshToolbar(isFavorite: Boolean)
}

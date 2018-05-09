package workshop.akbolatss.tagsnews.screen.board

import workshop.akbolatss.tagsnews.base.BaseView
import workshop.akbolatss.tagsnews.model.dao.RssSource

/**
 * MVP View for #BoardActivity
 * @see BoardActivity
 */
interface BoardView : BaseView {

    fun onInitSources(rssSources: List<RssSource>)

    fun onUpdateSources(rssSources: List<RssSource>)
}

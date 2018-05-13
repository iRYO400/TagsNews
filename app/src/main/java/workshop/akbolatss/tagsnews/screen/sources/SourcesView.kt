package workshop.akbolatss.tagsnews.screen.sources

import workshop.akbolatss.tagsnews.base.BaseView
import workshop.akbolatss.tagsnews.base.ErrorView
import workshop.akbolatss.tagsnews.base.LoadingView
import workshop.akbolatss.tagsnews.model.dao.RssSource

/**
 * MVP View for #SourcesView
 * @see SourcesActivity
 */
interface SourcesView : BaseView, LoadingView, ErrorView {

    fun onLoadSources(rssSourceList: List<RssSource>)

    fun onAddNewSource()
}

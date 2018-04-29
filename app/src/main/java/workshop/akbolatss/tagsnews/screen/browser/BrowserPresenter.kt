package workshop.akbolatss.tagsnews.screen.browser

import javax.inject.Inject

import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.DBRssItemRepository

class BrowserPresenter @Inject
constructor() : BasePresenter<BrowserView>()

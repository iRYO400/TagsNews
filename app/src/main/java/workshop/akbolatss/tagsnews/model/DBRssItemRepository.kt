package workshop.akbolatss.tagsnews.model

import java.util.ArrayList
import java.util.Objects
import java.util.concurrent.Callable

import io.reactivex.Observable
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.model.dao.RssFeedItem
import workshop.akbolatss.tagsnews.model.dao.RssFeedItemDao

class DBRssItemRepository(private val mDaoSession: DaoSession) : RssItemRepository {

    override val favorites: Observable<List<RssItem>>
        get() = Observable.fromCallable {
            val rssItemDao = mDaoSession.rssFeedItemDao
            val rssFeedItemList = rssItemDao.loadAll()

            val mappedItems = ArrayList<RssItem>()

            for (i in rssFeedItemList.indices.reversed()) {
                val rssItem = RssItem()
                rssItem.title = rssFeedItemList[i].title
                rssItem.link = rssFeedItemList[i].link
                rssItem.description = rssFeedItemList[i].description
                rssItem.image = rssFeedItemList[i].image
                rssItem.publishDate = rssFeedItemList[i].pubDate
                mappedItems.add(rssItem)
            }
            mappedItems
        }

    override fun addRssItem(rssItem: RssItem) {
        val rssFeedItem = RssFeedItem()
        rssFeedItem.title = rssItem.title
        rssFeedItem.link = rssItem.link
        rssFeedItem.pubDate = rssItem.publishDate
        rssFeedItem.image = rssItem.image
        rssFeedItem.description = rssItem.description
        //        rssFeedItem.setRssSourceId(rssItem.getDescription()); TODO: Pass RssSourceID

        val rssFeedItemDao = mDaoSession.rssFeedItemDao
        rssFeedItemDao.insert(rssFeedItem)
    }

    override fun removeRssItem(pubDate: String) {
        val rssFeedItemDao = mDaoSession.rssFeedItemDao
        val rssFeedItemList = rssFeedItemDao.loadAll()

        for (i in rssFeedItemList.indices) {
            if (rssFeedItemList[i].pubDate == pubDate) {
                rssFeedItemDao.delete(rssFeedItemList[i])
            }
        }
    }

    override fun checkIfExists(pubDate: String): Boolean {
        val rssFeedItemDao = mDaoSession.rssFeedItemDao
        val rssFeedItemList = rssFeedItemDao.loadAll()
        //TODO: Replace this with RxJava2 EDITION
        //        Observable<RssFeedItem> observable = Observable.fromIterable(rssFeedItemList)
        //                .filter(new Predicate<RssFeedItem>() {
        //                    @Override
        //                    public boolean test(@NonNull RssFeedItem rssFeedItem) throws Exception {
        //                        return rssFeedItem.getFeed_pubDate().equals(pubDate);
        //                    }
        //                })
        //                .subscribeOn(Schedulers.io())
        //                .observeOn(AndroidSchedulers.mainThread());

        for (i in rssFeedItemList.indices) {
            if (rssFeedItemList[i].pubDate == pubDate) {
                return true
            }
        }
        return false
    }
}

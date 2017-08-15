package workshop.akbolatss.tagsnews.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import workshop.akbolatss.tagsnews.repositories.source.RssFeedItem;
import workshop.akbolatss.tagsnews.repositories.source.RssFeedItemDao;


/**
 * Created by AkbolatSS on 15.08.2017.
 */

public class DBRssItemRepository implements RssItemRepository {

    private DaoSession daoSession;

    public DBRssItemRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    @Override
    public Observable<List<RssItem>> getFavorites() {

        return Observable.fromCallable(new Callable<List<RssItem>>() {
            @Override
            public List<RssItem> call() throws Exception {
                RssFeedItemDao rssItemDao = daoSession.getRssFeedItemDao();
                List<RssFeedItem> rssFeedItemList = rssItemDao.loadAll();

                List<RssItem> mappedItems = new ArrayList<>();

                for (int i = 0; i < rssFeedItemList.size(); i++) {
                    RssItem rssItem = new RssItem();
                    rssItem.setTitle(rssFeedItemList.get(i).getFeed_title());
                    rssItem.setLink(rssFeedItemList.get(i).getFeed_link());
                    rssItem.setDescription(rssFeedItemList.get(i).getFeed_description());
                    rssItem.setImage(rssFeedItemList.get(i).getFeed_image());
                    rssItem.setPublishDate(rssFeedItemList.get(i).getFeed_pubDate());
                    mappedItems.add(rssItem);
                }
                return mappedItems;
            }
        });
    }
}

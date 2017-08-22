package workshop.akbolatss.tagsnews.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private DaoSession mDaoSession;

    public DBRssItemRepository(DaoSession mDaoSession) {
        this.mDaoSession = mDaoSession;
    }


    @Override
    public void addRssItem(RssItem rssItem) {
        RssFeedItem rssFeedItem = new RssFeedItem();
        rssFeedItem.setTitle(rssItem.getTitle());
        rssFeedItem.setLink(rssItem.getLink());
        rssFeedItem.setPubDate(rssItem.getPublishDate());
        rssFeedItem.setImage(rssItem.getImage());
        rssFeedItem.setDescription(rssItem.getDescription());
//        rssFeedItem.setRssSourceId(rssItem.getDescription()); TODO: Pass RssSourceID

        RssFeedItemDao rssFeedItemDao = mDaoSession.getRssFeedItemDao();
        rssFeedItemDao.insert(rssFeedItem);
    }

    @Override
    public void removeRssItem(String pubDate) {
        RssFeedItemDao rssFeedItemDao = mDaoSession.getRssFeedItemDao();
        List<RssFeedItem> rssFeedItemList = rssFeedItemDao.loadAll();

        for (int i = 0; i < rssFeedItemList.size(); i++) {
            if (rssFeedItemList.get(i).getPubDate().equals(pubDate)) {
                rssFeedItemDao.delete(rssFeedItemList.get(i));
            }
        }
    }

    @Override
    public boolean checkIfExists(String pubDate) {
        RssFeedItemDao rssFeedItemDao = mDaoSession.getRssFeedItemDao();
        List<RssFeedItem> rssFeedItemList = rssFeedItemDao.loadAll();
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


        for (int i = 0; i < rssFeedItemList.size(); i++) {
            if (Objects.equals(rssFeedItemList.get(i).getPubDate(), pubDate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Observable<List<RssItem>> getFavorites() {

        return Observable.fromCallable(new Callable<List<RssItem>>() {
            @Override
            public List<RssItem> call() throws Exception {
                RssFeedItemDao rssItemDao = mDaoSession.getRssFeedItemDao();
                List<RssFeedItem> rssFeedItemList = rssItemDao.loadAll();

                List<RssItem> mappedItems = new ArrayList<>();

                for (int i = rssFeedItemList.size() - 1; i >= 0 ; i--) {
                    RssItem rssItem = new RssItem();
                    rssItem.setTitle(rssFeedItemList.get(i).getTitle());
                    rssItem.setLink(rssFeedItemList.get(i).getLink());
                    rssItem.setDescription(rssFeedItemList.get(i).getDescription());
                    rssItem.setImage(rssFeedItemList.get(i).getImage());
                    rssItem.setPublishDate(rssFeedItemList.get(i).getPubDate());
                    mappedItems.add(rssItem);
                }
                return mappedItems;
            }
        });
    }
}

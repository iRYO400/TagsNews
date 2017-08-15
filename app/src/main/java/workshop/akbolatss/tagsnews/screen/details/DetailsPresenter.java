package workshop.akbolatss.tagsnews.screen.details;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import workshop.akbolatss.tagsnews.repositories.source.RssFeedItem;
import workshop.akbolatss.tagsnews.repositories.source.RssFeedItemDao;

/**
 * Created by AkbolatSS on 10.08.2017.
 */

public class DetailsPresenter extends BasePresenter<DetailsView> {

    @Inject
    protected DaoSession mDaoSession;

    @Inject
    public DetailsPresenter() {
    }

    public void OnAddToFavorites(RssItem rssItem) {
        RssFeedItem rssFeedItem = new RssFeedItem();
        rssFeedItem.setFeed_title(rssItem.getTitle());
        rssFeedItem.setFeed_link(rssItem.getLink());
        rssFeedItem.setFeed_pubDate(rssItem.getPublishDate());
        rssFeedItem.setFeed_image(rssItem.getImage());
        rssFeedItem.setFeed_description(rssItem.getDescription());


        RssFeedItemDao rssFeedItemDao = mDaoSession.getRssFeedItemDao();

        rssFeedItemDao.insert(rssFeedItem);
        rssFeedItemDao.getKey(rssFeedItem);
        getView().onRefreshToolbar(true);
    }

    public void OnRemoveFromFavorites(final String pubDate){
        RssFeedItemDao rssFeedItemDao = mDaoSession.getRssFeedItemDao();
        List<RssFeedItem> rssFeedItemList = rssFeedItemDao.loadAll();

        for (int i = 0; i < rssFeedItemList.size(); i++) {
            if (rssFeedItemList.get(i).getFeed_pubDate().equals(pubDate)) {
                rssFeedItemDao.delete(rssFeedItemList.get(i));
            }
        }

        getView().onRefreshToolbar(false);
    }

    public boolean onCheckFavorites(final String pubDate) {
        RssFeedItemDao rssFeedItemDao = mDaoSession.getRssFeedItemDao();
        List<RssFeedItem> rssFeedItemList = rssFeedItemDao.loadAll();
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
            if (Objects.equals(rssFeedItemList.get(i).getFeed_pubDate(), pubDate)) {
                return true;
            }
        }
        return false;
    }
}

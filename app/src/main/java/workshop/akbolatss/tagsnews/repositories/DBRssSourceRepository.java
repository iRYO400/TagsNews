package workshop.akbolatss.tagsnews.repositories;

import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.repositories.source.RssSourceDao;
import workshop.akbolatss.tagsnews.screen.sources.FeedlyResponse;

/**
 * Created by AkbolatSS on 16.08.2017.
 */

public class DBRssSourceRepository implements RssSourceRepository {

    private DaoSession mDaoSession;
    private NewsApiService mApiService;

    public DBRssSourceRepository(DaoSession mDaoSession) {
        this.mDaoSession = mDaoSession;
    }

    public DBRssSourceRepository(DaoSession mDaoSession, NewsApiService mApiService) {
        this.mDaoSession = mDaoSession;
        this.mApiService = mApiService;
    }

    @Override
    public void addNewSource(RssSource source) {
        RssSourceDao rssSourceDao = mDaoSession.getRssSourceDao();
        if (source.getLink().startsWith("feed")) {
            source.setLink(source.getLink().substring(5));
        }
        rssSourceDao.insert(source);
    }

    @Override
    public Observable<List<RssSource>> getAllSources() {
        return Observable.fromCallable(new Callable<List<RssSource>>() {
            @Override
            public List<RssSource> call() throws Exception {
                RssSourceDao rssSourceDao = mDaoSession.getRssSourceDao();
                return rssSourceDao.loadAll();
            }
        });
    }

    @Override
    public Single<FeedlyResponse> getQueryResult(String query) {
        return mApiService.getFeedlyResponse(query);
    }


    @Override
    public void updateSource(RssSource source) {
        RssSourceDao rssSourceDao = mDaoSession.getRssSourceDao();
        rssSourceDao.update(source);
    }

    @Override
    public void deleteSource(RssSource source) {
        RssSourceDao rssSourceDao = mDaoSession.getRssSourceDao();
        rssSourceDao.delete(source);
    }
}

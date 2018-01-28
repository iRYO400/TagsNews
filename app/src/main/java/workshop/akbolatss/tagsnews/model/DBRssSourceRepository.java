package workshop.akbolatss.tagsnews.model;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.model.dao.DaoSession;
import workshop.akbolatss.tagsnews.model.dao.RssSource;
import workshop.akbolatss.tagsnews.model.dao.RssSourceDao;

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
    public void initDefaultSource(RssSource source) {
        RssSourceDao rssSourceDao = mDaoSession.getRssSourceDao();
        rssSourceDao.insert(source);
    }

    @Override
    public void addNewSource(RssSource source) {
        RssSourceDao rssSourceDao = mDaoSession.getRssSourceDao();
        int positionIndex = (int) (rssSourceDao.queryBuilder().count() + 1);
        if (source.getLink().startsWith("feed")) {
            source.setLink(source.getLink().substring(5));
        }
        source.setPositionIndex(positionIndex);
        rssSourceDao.insert(source);
    }

    @Override
    public Single<List<RssSource>> getAllSources() {
        return Single.fromCallable(new Callable<List<RssSource>>() {
            @Override
            public List<RssSource> call() throws Exception {
                RssSourceDao rssSourceDao = mDaoSession.getRssSourceDao();
                return rssSourceDao.queryBuilder().orderAsc(RssSourceDao.Properties.PositionIndex).list();
            }
        });
    }

    @Override
    public Single<List<RssSource>> getOnlyActive() {
        return Single.fromCallable(new Callable<List<RssSource>>() {
            @Override
            public List<RssSource> call() throws Exception {
                RssSourceDao rssSourceDao = mDaoSession.getRssSourceDao();
                return rssSourceDao.queryBuilder().where(RssSourceDao.Properties.IsActive.eq(true)).orderAsc(RssSourceDao.Properties.PositionIndex).limit(5).list();
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
    public void swapSources(RssSource from, RssSource to) {
        RssSourceDao rssSourceDao = mDaoSession.getRssSourceDao();
        int f = from.getPositionIndex();
        int t = to.getPositionIndex();

        to.setPositionIndex(f);
        from.setPositionIndex(t);
        rssSourceDao.update(from);
        rssSourceDao.update(to);
    }

    @Override
    public void deleteSource(RssSource source) {
        RssSourceDao rssSourceDao = mDaoSession.getRssSourceDao();
        rssSourceDao.delete(source);
    }
}

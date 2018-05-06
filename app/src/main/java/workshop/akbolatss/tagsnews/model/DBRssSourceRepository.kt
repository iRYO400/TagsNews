package workshop.akbolatss.tagsnews.model

import io.reactivex.Single
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.model.dao.RssSourceDao

class DBRssSourceRepository : RssSourceRepository {

    private var mDaoSession: DaoSession
    private lateinit var mApiService: NewsApiService

    constructor(mDaoSession: DaoSession) {
        this.mDaoSession = mDaoSession
    }

    constructor(mDaoSession: DaoSession, mApiService: NewsApiService) {
        this.mDaoSession = mDaoSession
        this.mApiService = mApiService
    }

    override val allSources: Single<List<RssSource>>
        get() = Single.fromCallable {
            val rssSourceDao = mDaoSession.rssSourceDao
            rssSourceDao.queryBuilder().orderAsc(RssSourceDao.Properties.PositionIndex).list()
        }

    override val onlyActive: Single<List<RssSource>>
        get() = Single.fromCallable {
            val rssSourceDao = mDaoSession.rssSourceDao
            rssSourceDao.queryBuilder().where(RssSourceDao.Properties.IsActive.eq(true)).orderAsc(RssSourceDao.Properties.PositionIndex).limit(5).list()
        }

    override fun initDefaultSource(source: RssSource) {
        val rssSourceDao = mDaoSession.rssSourceDao
        rssSourceDao.insert(source)
    }

    override fun addNewSource(source: RssSource) {
        val rssSourceDao = mDaoSession.rssSourceDao
        val positionIndex = (rssSourceDao.queryBuilder().count() + 1).toInt()
        if (source.link.startsWith("feed")) {
            source.link = source.link.substring(5)
        }
        source.positionIndex = positionIndex
        rssSourceDao.insert(source)
    }

    override fun getQueryResult(query: String): Single<FeedlyResponse> {
        return mApiService.getFeedlyResponse(query)
    }

    override fun updateSource(source: RssSource) {
        val rssSourceDao = mDaoSession.rssSourceDao
        rssSourceDao.update(source)
    }

    override fun swapSources(from: RssSource, to: RssSource) {
        val rssSourceDao = mDaoSession.rssSourceDao
        val f = from.positionIndex!!
        val t = to.positionIndex!!

        to.positionIndex = f
        from.positionIndex = t
        rssSourceDao.update(from)
        rssSourceDao.update(to)
    }

    override fun deleteSource(source: RssSource) {
        val rssSourceDao = mDaoSession.rssSourceDao
        rssSourceDao.delete(source)
    }
}

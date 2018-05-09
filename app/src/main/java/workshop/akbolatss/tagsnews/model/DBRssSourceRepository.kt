package workshop.akbolatss.tagsnews.model

import io.reactivex.Single
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.model.dao.RssSourceDao
import workshop.akbolatss.tagsnews.util.Logger

/**
 * DBReminderItemRepository is the class that contains all methods related to work with #RssSource, API and DataBase.
 * Repository pattern.
 * @see RssSource
 */
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

    /**
     * Load all Rss sources from DB
     */
    override val allSources: Single<List<RssSource>>
        get() = Single.fromCallable {
            val rssSourceDao = mDaoSession.rssSourceDao
            rssSourceDao.queryBuilder().orderAsc(RssSourceDao.Properties.PositionIndex).list()
        }

    /**
     * Load only active sources from DB
     */
    override val onlyActive: Single<List<RssSource>>
        get() = Single.fromCallable {
            val rssSourceDao = mDaoSession.rssSourceDao
            rssSourceDao.queryBuilder().where(RssSourceDao.Properties.IsActive.eq(true)).orderAsc(RssSourceDao.Properties.PositionIndex).limit(5).list()
        }

    /**
     * Load RSS Source by id
     */
    override fun getById(id: Long): Single<RssSource> {
        return Single.fromCallable {
            val rssSourceDao = mDaoSession.rssSourceDao
            rssSourceDao.load(id)
        }
    }

    /**
     * Add new source to DB
     */
    override fun addNewSource(source: RssSource) {
        val rssSourceDao = mDaoSession.rssSourceDao
        val positionIndex = (rssSourceDao.queryBuilder().count() + 1).toInt()
        if (source.link.startsWith("feed")) {
            source.link = source.link.substring(5)
        }
        source.positionIndex = positionIndex
        rssSourceDao.insert(source)
    }

    /**
     * Get Rss sources from API
     */
    override fun getQueryResult(query: String): Single<FeedlyResponse> {
        return mApiService.getFeedlyResponse(query)
    }

    /**
     * Update existing Rss source in DB
     */
    override fun updateSource(source: RssSource) {
        val rssSourceDao = mDaoSession.rssSourceDao
        rssSourceDao.update(source)
    }

    /**
     * Swap positions of Rss Source
     */
    override fun swapSources(from: RssSource, to: RssSource) {
        val rssSourceDao = mDaoSession.rssSourceDao
        val f = from.positionIndex!!
        val t = to.positionIndex!!

        to.positionIndex = f
        from.positionIndex = t
        rssSourceDao.update(from)
        rssSourceDao.update(to)
    }

    /**
     * Remove from DB
     */
    override fun deleteSource(source: RssSource) {
        val rssSourceDao = mDaoSession.rssSourceDao
        rssSourceDao.delete(source)
    }

    override fun deleteSourceByLink(source: RssSource) {
        val rssSourceDao = mDaoSession.rssSourceDao
        val rssSource = rssSourceDao.queryBuilder().where(RssSourceDao.Properties.Link.eq(source.link)).build().unique()
        rssSourceDao.delete(rssSource)
    }

    /**
     * If this RSS source exists in DB
     */
    override fun checkIfExists(source: RssSource): Boolean {
        if (source.link.startsWith("feed")) {
            source.link = source.link.substring(5)
        }
        val rssSourceDao = mDaoSession.rssSourceDao
        val rssSource = rssSourceDao.queryBuilder().where(RssSourceDao.Properties.Link.eq(source.link)).build().unique()
        return rssSource != null
    }
}

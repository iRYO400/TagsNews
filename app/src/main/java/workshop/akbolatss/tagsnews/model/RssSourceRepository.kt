package workshop.akbolatss.tagsnews.model

import io.reactivex.Single
import workshop.akbolatss.tagsnews.model.dao.RssSource

/**
 * @see DBRssSourceRepository
 */
interface RssSourceRepository {

    val allSources: Single<List<RssSource>>

    val onlyActive: Single<List<RssSource>>

    fun getById(id: Long): Single<RssSource>

    fun getQueryResult(query: String): Single<FeedlyResponse>

    fun addNewSource(source: RssSource)

    fun updateSource(source: RssSource)

    fun swapSources(from: RssSource, to: RssSource)

    fun deleteSource(source: RssSource)

    fun checkIfExists(source: RssSource): Boolean
    fun deleteSourceByLink(source: RssSource)
}

package workshop.akbolatss.tagsnews.model

import io.reactivex.Single
import workshop.akbolatss.tagsnews.model.dao.RssSource


interface RssSourceRepository {

    val allSources: Single<List<RssSource>>

    val onlyActive: Single<List<RssSource>>

    fun getQueryResult(query: String): Single<FeedlyResponse>

    fun initDefaultSource(source: RssSource)

    fun addNewSource(source: RssSource)

    fun updateSource(source: RssSource)

    fun swapSources(from: RssSource, to: RssSource)

    fun deleteSource(source: RssSource)
}

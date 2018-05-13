package workshop.akbolatss.tagsnews.model

import com.google.gson.annotations.SerializedName

import workshop.akbolatss.tagsnews.model.dao.RssSource

/**
 * Entity for API response
 */
class FeedlyResponse {

    /**
     * List of Rss sources
     */
    @SerializedName("results")
    val rssSourceList: List<RssSource>? = null

    @SerializedName("related")
    val related: List<String>? = null
}

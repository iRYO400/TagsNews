package workshop.akbolatss.tagsnews.model

import com.google.gson.annotations.SerializedName

import workshop.akbolatss.tagsnews.model.dao.RssSource

class FeedlyResponse {

    @SerializedName("results")
    val rssSourceList: List<RssSource>? = null

    @SerializedName("related")
    val related: List<String>? = null
}

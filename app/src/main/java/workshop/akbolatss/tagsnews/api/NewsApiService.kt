package workshop.akbolatss.tagsnews.api

import io.reactivex.Single
import me.toptas.rssconverter.RssFeed
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import workshop.akbolatss.tagsnews.model.FeedlyResponse

interface NewsApiService {

    @GET
    @Xml
    fun getRss(@Url url: String): Single<RssFeed>

    @GET("v3/search/feeds")
    @Json
    fun getFeedlyResponse(@Query("q") query: String): Single<FeedlyResponse>

    @Retention(AnnotationRetention.RUNTIME)
    annotation class Xml

    @Retention(AnnotationRetention.RUNTIME)
    annotation class Json
}

package workshop.akbolatss.tagsnews.api;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.Observable;
import io.reactivex.Single;
import me.toptas.rssconverter.RssFeed;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import workshop.akbolatss.tagsnews.screen.sources.FeedlyResponse;

public interface NewsApiService {

    @GET
    @Xml
    Single<RssFeed> getRss(@Url String url);

    @GET("v3/search/feeds")
    @Json
    Single<FeedlyResponse> getFeedlyResponse(@Query("q") String query);

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Xml {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Json {
    }
}

package workshop.akbolatss.tagsnews.api;


import io.reactivex.Observable;
import retrofit2.http.GET;

import workshop.akbolatss.tagsnews.screen.news.model.News;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public interface NewsApiService {

    @GET("v1/articles?source=techcrunch&apiKey=fce1a885f25e49f7a8c388dfef814e60")
    Observable<News> getTestNews();

}

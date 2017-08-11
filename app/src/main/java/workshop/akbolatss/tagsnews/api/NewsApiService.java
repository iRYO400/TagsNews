package workshop.akbolatss.tagsnews.api;


import io.reactivex.Observable;
import me.toptas.rssconverter.RssFeed;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public interface NewsApiService {

    @GET
    Observable<RssFeed> getRss(@Url String url);

}

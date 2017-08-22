package workshop.akbolatss.tagsnews.repositories;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.screen.sources.FeedlyResponse;

/**
 * Created by AkbolatSS on 16.08.2017.
 */

public interface RssSourceRepository {

    void addNewSource(RssSource source);

    Observable<List<RssSource>> getAllSources();

    Single<FeedlyResponse> getQueryResult(String query);

    void updateSource(RssSource source);

    void deleteSource(RssSource source);
}

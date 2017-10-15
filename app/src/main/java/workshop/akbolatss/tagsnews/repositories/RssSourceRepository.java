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

    Observable<List<RssSource>> getAllSources();

    Observable<List<RssSource>> getOnlyActive();

    Single<FeedlyResponse> getQueryResult(String query);

    void initDefaultSource(RssSource source);

    void addNewSource(RssSource source);

    void updateSource(RssSource source);

    void swapSources(RssSource from, RssSource to);

    void deleteSource(RssSource source);
}

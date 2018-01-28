package workshop.akbolatss.tagsnews.model;

import java.util.List;

import io.reactivex.Single;
import workshop.akbolatss.tagsnews.model.dao.RssSource;


public interface RssSourceRepository {

    Single<List<RssSource>> getAllSources();

    Single<List<RssSource>> getOnlyActive();

    Single<FeedlyResponse> getQueryResult(String query);

    void initDefaultSource(RssSource source);

    void addNewSource(RssSource source);

    void updateSource(RssSource source);

    void swapSources(RssSource from, RssSource to);

    void deleteSource(RssSource source);
}

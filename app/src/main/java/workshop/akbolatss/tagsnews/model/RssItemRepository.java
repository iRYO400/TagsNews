package workshop.akbolatss.tagsnews.model;

import java.util.List;

import io.reactivex.Observable;
import me.toptas.rssconverter.RssItem;

public interface RssItemRepository {

    void addRssItem(RssItem rssItem);

    void removeRssItem(String pubDate);

    boolean checkIfExists(String pubDate);

    Observable<List<RssItem>> getFavorites();
}

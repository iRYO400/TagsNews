package workshop.akbolatss.tagsnews.repositories;

import java.util.List;

import io.reactivex.Observable;
import me.toptas.rssconverter.RssItem;

/**
 * Created by AkbolatSS on 15.08.2017.
 */

public interface RssItemRepository {

    void addRssItem(RssItem rssItem);

    void removeRssItem(String pubDate);

    boolean checkIfExists(String pubDate);

    Observable<List<RssItem>> getFavorites();
}

package workshop.akbolatss.tagsnews.screen.favorites;

import java.util.List;

import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.base.BaseView;

/**
 * Created by AkbolatSS on 14.08.2017.
 */

public interface FavoritesView extends BaseView {

    public void OnLoadFavorites(List<RssItem> rssItems);

}

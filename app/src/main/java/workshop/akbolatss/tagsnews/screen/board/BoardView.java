package workshop.akbolatss.tagsnews.screen.board;

import android.support.annotation.NonNull;

import java.util.List;

import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.base.BaseView;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

/**
 * Created by AkbolatSS on 22.08.2017.
 */

public interface BoardView extends BaseView {

    void onInitSources(List<RssSource> rssSources);

    void onOpenSource();

    void onShareNews();

    void onOpenItemDetails(@NonNull RssItem rssItem, String sourceName);

    void onRefreshToolbar(boolean isFavorite);

    void onRefreshDrawerDetails();
}

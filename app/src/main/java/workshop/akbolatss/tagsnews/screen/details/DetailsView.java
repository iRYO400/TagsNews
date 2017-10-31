package workshop.akbolatss.tagsnews.screen.details;

import android.support.annotation.NonNull;

import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.base.BaseView;

public interface DetailsView extends BaseView {

    void onOpenSource();

    void onShareNews();

    void onShareVk();

    void onShareFb();

    void onShareTw();

    void onShareWithWebIntent(String socialNetworkId);

    void onOpenItemDetails(@NonNull RssItem rssItem, String sourceName);

    void onRefreshToolbar(boolean isFavorite);

    void onRefreshDrawerDetails();
}

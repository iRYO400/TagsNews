package workshop.akbolatss.tagsnews.screen.details;

import workshop.akbolatss.tagsnews.base.BaseView;

/**
 * Created by AkbolatSS on 10.08.2017.
 */

public interface DetailsView extends BaseView {

    void onOpenSource();

    void onShareNews();

    void onRefreshToolbar(boolean isFavorite);
}

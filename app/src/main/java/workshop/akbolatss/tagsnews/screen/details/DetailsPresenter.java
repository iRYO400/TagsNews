package workshop.akbolatss.tagsnews.screen.details;

import javax.inject.Inject;

import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.model.DBRssItemRepository;

public class DetailsPresenter extends BasePresenter<DetailsView> {

    @Inject
    protected DBRssItemRepository mDbRssItemRepository;

    @Inject
    public DetailsPresenter() {
    }

    public void OnAddToFavorites(RssItem rssItem) {
        mDbRssItemRepository.addRssItem(rssItem);
        getView().onRefreshToolbar(true);
    }

    public void OnRemoveFromFavorites(final String pubDate){
        mDbRssItemRepository.removeRssItem(pubDate);
        getView().onRefreshToolbar(false);
    }

    public boolean onCheckFavorites(final String pubDate) {
        boolean b = mDbRssItemRepository.checkIfExists(pubDate);
        getView().onRefreshToolbar(b);
        return b;
    }
}

package workshop.akbolatss.tagsnews.screen.board;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.repositories.DBRssItemRepository;
import workshop.akbolatss.tagsnews.repositories.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

/**
 * Created by AkbolatSS on 22.08.2017.
 */

public class BoardPresenter extends BasePresenter<BoardView> {

    @Inject
    protected DBRssSourceRepository mRepository;

    @Inject
    protected DBRssItemRepository mDbRssItemRepository;

    @Inject
    public BoardPresenter() {
    }

    public void onLoadSources() {
        mRepository.getOnlyActive()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<RssSource>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<RssSource> rssSources) {
                        getView().onInitSources(rssSources);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
        return mDbRssItemRepository.checkIfExists(pubDate);
    }
}

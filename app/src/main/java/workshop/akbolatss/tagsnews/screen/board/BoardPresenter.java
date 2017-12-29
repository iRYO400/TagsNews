package workshop.akbolatss.tagsnews.screen.board;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.repositories.DBRssItemRepository;
import workshop.akbolatss.tagsnews.repositories.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

public class BoardPresenter extends BasePresenter<BoardView> {

    @Inject
    protected DBRssSourceRepository mRepository;

    @Inject
    public BoardPresenter() {
    }

    public void onLoadSources(final boolean isUpdating) {
        mRepository.getOnlyActive()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<List<RssSource>>() {
                    @Override
                    public void onSuccess(List<RssSource> rssSources) {
                        if (isUpdating) {
                            getView().onUpdateSources(rssSources);
                        } else {
                            getView().onInitSources(rssSources);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}

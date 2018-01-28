package workshop.akbolatss.tagsnews.screen.favorites;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.model.DBRssItemRepository;

/**
 * Created by AkbolatSS on 14.08.2017.
 */

public class FavoritesPresenter extends BasePresenter<FavoritesView> implements Observer<List<RssItem>> {

    @Inject
    protected DBRssItemRepository mRepository;

    @Inject
    public FavoritesPresenter() {
    }

    public void onLoadFavorites() {
        mRepository.getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(this);
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull List<RssItem> rssItems) {
        getView().OnLoadFavorites(rssItems);
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}

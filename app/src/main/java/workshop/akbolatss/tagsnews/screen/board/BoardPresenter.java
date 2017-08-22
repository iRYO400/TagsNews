package workshop.akbolatss.tagsnews.screen.board;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.repositories.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

/**
 * Created by AkbolatSS on 22.08.2017.
 */

public class BoardPresenter extends BasePresenter<BoardView> implements Observer<List<RssSource>> {

    @Inject
    protected DBRssSourceRepository mRepository;

    @Inject
    public BoardPresenter() {
    }

    public void initPresenter() {
        mRepository.getAllSources()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this);
    }

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
}

package workshop.akbolatss.tagsnews.screen.sources;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.repositories.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

public class SourcesPresenter extends BasePresenter<SourcesView> {

    @Inject
    protected DBRssSourceRepository mRepository;

    @Inject
    public SourcesPresenter() {
    }

    public void onLoadSources() {
        mRepository.getAllSources()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<RssSource>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<RssSource> rssSources) {
                        getView().onLoadSources(rssSources);
                        getView().onHideLoading();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onSearchSources(String s) {
        getView().onShowLoading();
        mRepository.getQueryResult(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FeedlyResponse>() {
                    @Override
                    public void accept(FeedlyResponse feedlyResponse) throws Exception {
                        getView().onLoadSources(feedlyResponse.getRssSourceList());
                        getView().onHideLoading();
                    }
                });
    }

    public void onSwapPositions(RssSource from, RssSource to){
        mRepository.swapSources(from, to);
    }

    public void onAddNewSource(RssSource rssSource) {

        mRepository.addNewSource(rssSource);
    }

    public void onUpdateSource(RssSource rssSource) {
        mRepository.updateSource(rssSource);
    }

    public void onRemoveSource(RssSource rssSource) {
        mRepository.deleteSource(rssSource);
    }
}

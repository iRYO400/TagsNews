package workshop.akbolatss.tagsnews.screen.sources;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.repositories.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.repositories.FeedlyResponse;
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
                .subscribe(new DisposableSingleObserver<List<RssSource>>() {
                    @Override
                    public void onSuccess(List<RssSource> rssSources) {
                        getView().onHideLoading();
                        if (rssSources.size() > 0) {
                            getView().onLoadSources(rssSources);
                            getView().onNoContent(false);
                        } else {
                            getView().onNoContent(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException) {
                            getView().onTimeout();
                        } else if (e instanceof IOException) {
                            getView().onNetworkError();
                        } else {
                            getView().onUnknownError(e.getMessage());
                        }
                        getView().onHideLoading();
                    }
                });
    }

    public void onSearchSources(String s) {
        getView().onShowLoading();
        mRepository.getQueryResult(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<FeedlyResponse>() {
                    @Override
                    public void onSuccess(FeedlyResponse feedlyResponse) {
                        getView().onHideLoading();
                        if (feedlyResponse.getRssSourceList().size() > 0) {
                            getView().onLoadSources(feedlyResponse.getRssSourceList());
                            getView().onNoContent(false);
                        } else {
                            getView().onNoContent(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException) {
                            getView().onTimeout();
                        } else if (e instanceof IOException) {
                            getView().onNetworkError();
                        } else {
                            getView().onUnknownError(e.getMessage());
                        }
                        getView().onHideLoading();
                    }
                });
    }

    public void onSwapPositions(RssSource from, RssSource to) {
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

package workshop.akbolatss.tagsnews.screen.news;

import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import me.toptas.rssconverter.RssFeed;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.base.BasePresenter;

public class NewsPresenter extends BasePresenter<NewsView> {

    private String mUrl;

    @Inject
    public NewsApiService mApiService;

    @Inject
    public NewsPresenter() {
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public void onLoadNews() {
        getView().onShowLoading();

        mApiService.getRss(getUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<RssFeed>() {
                    @Override
                    public void onSuccess(@NonNull RssFeed rssFeed) {
                        getView().onLoadNews(rssFeed);
                        getView().onHideLoading();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
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
}

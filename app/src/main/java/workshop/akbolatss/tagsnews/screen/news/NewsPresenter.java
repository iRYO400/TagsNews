package workshop.akbolatss.tagsnews.screen.news;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.toptas.rssconverter.RssFeed;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.base.BasePresenter;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public class NewsPresenter extends BasePresenter<NewsView> implements Observer<RssFeed>{

    private static final String TAG = "BolaDebug";
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

    public void onLoadNews(){
        getView().onShowLoading();

        mApiService.getRss(getUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
    }

    @Override
    public void onNext(@NonNull RssFeed news) {
        getView().onLoadNews(news);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        getView().onHideLoading();
        getView().onShowError();
    }

    @Override
    public void onComplete() {
        getView().onHideLoading();
    }
}

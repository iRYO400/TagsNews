package workshop.akbolatss.tagsnews.screen.news;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.screen.news.model.News;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public class NewsPresenter extends BasePresenter<NewsView> implements Observer<News>{

    private static final String TAG = "TAG";

    @Inject
    public NewsApiService mApiService;

    @Inject
    public NewsPresenter() {
    }

    public void onLoadNews(){
        getView().onShowLoading();

        mApiService.getTestNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

//    private void handleResponse(News news){
//        getView().onHideLoading();
//        getView().onLoadNews(news);
//    }

//    private void handleError(Throwable throwable){
//        getView().onShowError();
//    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        Log.d(TAG, "onSubscribe " + d.isDisposed());
    }

    @Override
    public void onNext(@NonNull News news) {
        Log.d(TAG, "onNext");
        getView().onLoadNews(news);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        Log.d(TAG, "onError " + e.getMessage());
        getView().onShowError();
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete ");
        getView().onHideLoading();
    }
}

package workshop.akbolatss.tagsnews.screen.news

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.base.BasePresenter
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class NewsPresenter @Inject
constructor() : BasePresenter<NewsView>() {

    internal var url: String? = null

    @Inject
    lateinit var mApiService: NewsApiService

    fun onLoadNews() {
        view.onShowLoading()

        mApiService.getRss(url!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rssFeed ->
                    view.onLoadNews(rssFeed)
                    view.onHideLoading()
                }, { e ->
                    if (e is SocketTimeoutException) {
                        view.onTimeout()
                    } else if (e is IOException) {
                        view.onNetworkError()
                    } else {
                        view.onUnknownError(e.message!!)
                    }
                    view.onHideLoading()
                })
    }

}

package workshop.akbolatss.tagsnews.screen.sources

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.FeedlyResponse
import workshop.akbolatss.tagsnews.model.dao.RssSource
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class SourcesPresenter @Inject
constructor() : BasePresenter<SourcesView>() {

    @Inject
    lateinit var mRepository: DBRssSourceRepository

    fun onLoadSources() {
        mRepository.allSources
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableSingleObserver<List<RssSource>>() {
                    override fun onSuccess(rssSources: List<RssSource>) {
                        view.onHideLoading()
                        if (rssSources.isNotEmpty()) {
                            view.onLoadSources(rssSources)
                            view.onNoContent(false)
                        } else {
                            view.onNoContent(true)
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is SocketTimeoutException) {
                            view.onTimeout()
                        } else if (e is IOException) {
                            view.onNetworkError()
                        } else {
                            view.onUnknownError(e.message!!)
                        }
                        view.onHideLoading()
                    }
                })
    }

    fun onSearchSources(s: String) {
        view.onShowLoading()
        mRepository.getQueryResult(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableSingleObserver<FeedlyResponse>() {
                    override fun onSuccess(feedlyResponse: FeedlyResponse) {
                        view.onHideLoading()
                        if (feedlyResponse.rssSourceList!!.isNotEmpty()) {
                            view.onLoadSources(feedlyResponse.rssSourceList)
                            view.onNoContent(false)
                        } else {
                            view.onNoContent(true)
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is SocketTimeoutException) {
                            view.onTimeout()
                        } else if (e is IOException) {
                            view.onNetworkError()
                        } else {
                            view.onUnknownError(e.message!!)
                        }
                        view.onHideLoading()
                    }
                })
    }

    fun onSwapPositions(from: RssSource, to: RssSource) {
        mRepository.swapSources(from, to)
    }

    fun onAddNewSource(rssSource: RssSource) {
        mRepository.addNewSource(rssSource)
    }

    fun onUpdateSource(rssSource: RssSource) {
        mRepository.updateSource(rssSource)
    }

    fun onRemoveSource(rssSource: RssSource) {
        mRepository.deleteSource(rssSource)
    }
}

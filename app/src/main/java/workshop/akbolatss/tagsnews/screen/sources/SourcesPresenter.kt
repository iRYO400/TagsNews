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

/**
 * MVP Presenter for #SourcesActivity
 * @see SourcesActivity
 */
class SourcesPresenter @Inject
constructor() : BasePresenter<SourcesView>() {

    @Inject
    lateinit var mRepository: DBRssSourceRepository

    /**
     * Load all existing RSS sources from DB
     */
    fun loadSources() {
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

    /**
     * Look for RSS source by #query
     * @param query submitted query
     */
    fun searchSources(query: String) {
        view.onShowLoading()
        mRepository.getQueryResult(query)
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

    /**
     * Swap RSS source positions
     */
    fun swapPositions(from: RssSource, to: RssSource) {
        mRepository.swapSources(from, to)
    }

    /**
     * Add new RSS source
     */
    fun addNewSource(rssSource: RssSource) {
        mRepository.addNewSource(rssSource)
    }

    /**
     * Update RSS source
     */
    fun updateSource(rssSource: RssSource) {
        mRepository.updateSource(rssSource)
    }

    /**
     * Remove RSS source from DB by ID
     */
    fun removeSource(rssSource: RssSource) {
        mRepository.deleteSource(rssSource)
    }

    /**
     * Remove RSS source from DB by Url link
     */
    fun removeSourceByLink(rssSource: RssSource) {
        mRepository.deleteSourceByLink(rssSource)
    }

    /**
     * Check if RSS source exists in DB
     */
    fun checkInSources(rssSource: RssSource): Boolean {
        return mRepository.checkIfExists(rssSource)
    }
}

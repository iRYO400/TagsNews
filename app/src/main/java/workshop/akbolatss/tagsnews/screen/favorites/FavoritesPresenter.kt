package workshop.akbolatss.tagsnews.screen.favorites

import javax.inject.Inject

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.DBRssItemRepository
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Created by AkbolatSS on 14.08.2017.
 */

class FavoritesPresenter @Inject
constructor() : BasePresenter<FavoritesView>() {

    @Inject
    lateinit var mRepository: DBRssItemRepository

    fun onLoadFavorites() {
        mRepository.favorites
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rssFeeds ->
                    view.onLoadFavorites(rssFeeds)
                    view.onHideLoading()
                }, { e ->
                    view.onHideLoading()
                })
    }
}

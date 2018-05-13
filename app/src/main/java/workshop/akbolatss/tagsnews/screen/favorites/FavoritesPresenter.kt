package workshop.akbolatss.tagsnews.screen.favorites

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.DBRssItemRepository

/**
 * MVP Presenter for #FavoritesActivity
 * @see FavoritesActivity
 */
class FavoritesPresenter @Inject
constructor() : BasePresenter<FavoritesView>() {

    @Inject
    lateinit var mRepository: DBRssItemRepository

    /**
     * Load list of all favorite items in DB
     */
    fun onLoadFavorites() {
        mRepository.favorites
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rssFeeds ->
                    view.onLoadFavorites(rssFeeds)

                }, { e ->

                })
    }
}

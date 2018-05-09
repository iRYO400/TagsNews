package workshop.akbolatss.tagsnews.screen.board

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.RssSource

/**
 * MVP Presenter for #BoardActivity
 * @see BoardActivity
 */
class BoardPresenter @Inject constructor() : BasePresenter<BoardView>() {

    @Inject
    lateinit var mRepository: DBRssSourceRepository

    /**
     * Load list of active RSS sources from DB
     */
    fun onLoadSources(isUpdating: Boolean) {
        mRepository.onlyActive
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ rssSources ->
                    if (isUpdating) {
                        view.onUpdateSources(rssSources)
                    } else {
                        view.onInitSources(rssSources)
                    }
                }, {
                })
    }
}

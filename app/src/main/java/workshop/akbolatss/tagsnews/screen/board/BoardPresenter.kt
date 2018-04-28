package workshop.akbolatss.tagsnews.screen.board

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.RssSource

class BoardPresenter @Inject public constructor() : BasePresenter<BoardView>() {

    @Inject
    lateinit var mRepository: DBRssSourceRepository

    fun onLoadSources(isUpdating: Boolean) {
        mRepository.onlyActive
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : DisposableSingleObserver<List<RssSource>>() {
                    override fun onSuccess(rssSources: List<RssSource>) {
                        if (isUpdating) {
                            view.onUpdateSources(rssSources)
                        } else {
                            view.onInitSources(rssSources)
                        }
                    }

                    override fun onError(e: Throwable) {

                    }
                })
    }
}

package workshop.akbolatss.tagsnews.base

import javax.inject.Inject

/**
 * BasePresenter for child presenters(MVP architecture)
 */
open class BasePresenter<V : BaseView> {

    @Inject
    lateinit var view: V
}

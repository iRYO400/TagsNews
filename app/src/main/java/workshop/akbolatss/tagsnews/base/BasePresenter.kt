package workshop.akbolatss.tagsnews.base

import javax.inject.Inject

open class BasePresenter<V : BaseView> {

    @Inject
    lateinit var view: V
}

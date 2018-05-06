package workshop.akbolatss.tagsnews.base

interface LoadingView {

    fun onShowLoading()

    fun onHideLoading()

    fun onNoContent(isEmpty: Boolean)
}

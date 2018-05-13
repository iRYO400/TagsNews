package workshop.akbolatss.tagsnews.base

/**
 * MVP View for Loading states
 */
interface LoadingView {

    fun onShowLoading()

    fun onHideLoading()

    fun onNoContent(isEmpty: Boolean)
}

package workshop.akbolatss.tagsnews.base

/**
 * MVP View to handle error cases
 */
interface ErrorView {

    fun onUnknownError(errorMessage: String)

    fun onTimeout()

    fun onNetworkError()
}

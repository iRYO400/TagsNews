package workshop.akbolatss.tagsnews.base

interface ErrorView {

    fun onUnknownError(errorMessage: String)

    fun onTimeout()

    fun onNetworkError()
}

package workshop.akbolatss.tagsnews.screen.news

import android.support.v4.app.Fragment

import workshop.akbolatss.tagsnews.screen.recommendations.RecommendationsFragment

class NewsSource(val name: String, val url: String?, private val isRecommendations: Boolean) {
    var fragment: Fragment
        private set

    init {
        fragment = if (isRecommendations) {
            RecommendationsFragment()
        } else {
            NewsFragment()
        }
    }
}

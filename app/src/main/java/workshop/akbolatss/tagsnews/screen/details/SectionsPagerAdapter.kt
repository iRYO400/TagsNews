package workshop.akbolatss.tagsnews.screen.details

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import workshop.akbolatss.tagsnews.model.dao.RssFeedItem
import workshop.akbolatss.tagsnews.util.SmartFragmentStatePagerAdapter

/**
 * FragmentStatePager adapter to handle #RecommendationsFragment and #DetailsFragment in #BrowserFragment
 * @see DetailsActivity
 */
class SectionsPagerAdapter(fm: FragmentManager, private val rssItem: RssFeedItem)
    : SmartFragmentStatePagerAdapter<Fragment>(fm) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            DetailsFragment.newInstance(rssItem)
        } else {
            BrowserFragment.newInstance(rssItem)
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) {
            "Details"
        } else {
            "Browser"
        }
    }
}

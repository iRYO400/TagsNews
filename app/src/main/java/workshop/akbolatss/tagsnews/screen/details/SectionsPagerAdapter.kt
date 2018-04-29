package workshop.akbolatss.tagsnews.screen.details

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.screen.browser.BrowserFragment
import workshop.akbolatss.tagsnews.util.SmartFragmentStatePagerAdapter


class SectionsPagerAdapter(fm: FragmentManager, private val rssItem: RssItem)
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
            "TAB1"
        } else {
            "TAB2"
        }
    }
}

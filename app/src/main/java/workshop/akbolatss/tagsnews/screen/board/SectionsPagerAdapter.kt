package workshop.akbolatss.tagsnews.screen.board

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.screen.news.NewsFragment
import workshop.akbolatss.tagsnews.screen.recommendations.RecommendationsFragment
import workshop.akbolatss.tagsnews.util.SmartFragmentStatePagerAdapter

/**
 * FragmentStatePager adapter to handle #RecommendationsFragment and #NewsFragment in #BoardActivity
 * @see BoardActivity
 */
class SectionsPagerAdapter(fm: FragmentManager, private val sections: MutableList<RssSource>) : SmartFragmentStatePagerAdapter<Fragment>(fm) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            RecommendationsFragment()
        } else {
            NewsFragment.newInstance(sections[position - 1].id, sections[position - 1].link)
        }
    }

    override fun getCount(): Int {
        return sections.size + 1
    }

    fun onUpdate(newSections: List<RssSource>) {
        if (count > 1) {
            for (i in sections.size downTo 1) {
                sections.removeAt(i - 1)
            }
        }
        sections.addAll(newSections)
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) {
            "+"
        } else {
            sections[position - 1].title
        }
    }
}

package workshop.akbolatss.tagsnews.screen.board

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

import workshop.akbolatss.tagsnews.screen.news.NewsSource
import workshop.akbolatss.tagsnews.util.SmartFragmentStatePagerAdapter


class SectionsPagerAdapter(fm: FragmentManager, private val sections: MutableList<NewsSource>?) : SmartFragmentStatePagerAdapter<Fragment>(fm) {

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString(position.toString(), sections!![position].name)
        bundle.putString(getPageTitle(position)!!.toString(), sections[position].url)
        sections[position].fragment.arguments = bundle
        return sections[position].fragment
    }

    override fun getCount(): Int {
        return sections?.size ?: 0
    }

    fun onUpdate(newSections: List<NewsSource>) {
        for (i in count - 1 downTo 1) {
            sections!!.removeAt(i)
        }
        sections!!.addAll(newSections)
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return sections!![position].name
    }
}

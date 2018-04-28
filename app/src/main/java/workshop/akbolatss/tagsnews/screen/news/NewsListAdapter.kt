package workshop.akbolatss.tagsnews.screen.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.R
import java.util.*

class NewsListAdapter(private val mClickListener: OnRssClickListener, private val mViewItemMode: Int) : RecyclerView.Adapter<NewsListAdapter.NewsHolder>() {

    private val mNewsList: MutableList<RssItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val mLayoutInflater = LayoutInflater.from(parent.context)
        var view: View? = null
        when (mViewItemMode) {
            0 -> view = mLayoutInflater.inflate(R.layout.rv_news_item_text_only, parent, false)
            1 -> view = mLayoutInflater.inflate(R.layout.rv_news_item_small, parent, false)
            2 -> view = mLayoutInflater.inflate(R.layout.rv_news_item, parent, false)
        }
        return NewsHolder(view!!)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val rssItem = mNewsList!![position]
        holder.bind(rssItem, mViewItemMode == 0, mClickListener)
    }

    override fun getItemCount(): Int {
        return mNewsList?.size ?: 0
    }

    fun onAddItems(rssItems: List<RssItem>?) {
        if (rssItems != null) {
            mNewsList!!.addAll(rssItems)
            notifyDataSetChanged()
        }
    }

    interface OnRssClickListener {
        fun OnItemClick(rssItem: RssItem)
    }

    inner class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(rssItem: RssItem, isTextOnly: Boolean, listener: OnRssClickListener) {

//            tvTitle.text = rssItem.title
//            tvTimestamp.text = rssItem.publishDate
//
//            frameLayout.setOnClickListener { listener.OnItemClick(rssItem) }
//
//            if (!isTextOnly) {
//                tvDescription.text = rssItem.description
//
//                if (rssItem.image != null) {
//                    imgView.visibility = View.VISIBLE
//                    Picasso.with(itemView.context)
//                            .load(rssItem.image)
//                            .memoryPolicy(MemoryPolicy.NO_CACHE)
//                            .placeholder(R.drawable.placeholder)
//                            .into(imgView)
//                } else {
//                    imgView.visibility = View.GONE
//                }
//            }
        }

        //        private String getTimestamp(String time){
        //            SimpleDateFormat dateFormat = new SimpleDateFormat("")
        //        }
    }
}

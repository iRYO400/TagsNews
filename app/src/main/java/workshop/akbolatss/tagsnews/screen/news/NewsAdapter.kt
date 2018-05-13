package workshop.akbolatss.tagsnews.screen.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.synthetic.main.rv_news_item_small.view.*
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.util.Constants.ITEM_VIEW_MAIN
import workshop.akbolatss.tagsnews.util.UtilityMethods
import java.util.*

/**
 * Custom RecyclerView.Adapter for News items
 */
class NewsAdapter(private val mClickListener: NewsListener) : RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    /**
     * List of RSS feeds
     */
    private val mNewsList: MutableList<RssItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val mLayoutInflater = LayoutInflater.from(parent.context)
        val view: View = mLayoutInflater.inflate(R.layout.rv_news_item_small, parent, false)
        return NewsHolder(view)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val rssItem = mNewsList[position]
        holder.bind(rssItem, mClickListener)
    }

    override fun getItemCount(): Int {
        return mNewsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_MAIN
    }

    /**
     * Load list of items to this adapter
     */
    fun onAddItems(rssItems: List<RssItem>?) {
        if (rssItems != null) {
            mNewsList.clear()
            mNewsList.addAll(rssItems)
            notifyDataSetChanged()
        }
    }

    interface NewsListener {
        fun onItemClick(rssItem: RssItem)
    }

    /**
     * Custom View Holder where View instantiates
     */
    inner class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(rssItem: RssItem, listener: NewsListener) {

            itemView.tvTitle.text = rssItem.title
            if (rssItem.publishDate != null && rssItem.publishDate.isNotEmpty())
                itemView.tvTimestamp.text = UtilityMethods.convertTime(rssItem.publishDate)

            itemView.tvDescription.text = rssItem.description

            if (rssItem.image != null) {
                itemView.imgImage.visibility = View.VISIBLE
                Picasso.with(itemView.context)
                        .load(rssItem.image)
                        .placeholder(R.drawable.placeholder)
                        .into(itemView.imgImage)
            } else {
                itemView.imgImage.visibility = View.GONE
            }

            itemView.flItem.setOnClickListener {
                listener.onItemClick(rssItem)
            }
            itemView.flImage.setOnClickListener {
                ImageViewer.Builder(itemView.context, arrayListOf(rssItem.image))
                        .setStartPosition(0)
                        .show()
            }
        }
    }
}

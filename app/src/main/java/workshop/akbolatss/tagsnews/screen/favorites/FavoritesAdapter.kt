package workshop.akbolatss.tagsnews.screen.favorites

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.synthetic.main.rv_news_item_small.view.*
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.model.dao.RssFeedItem
import workshop.akbolatss.tagsnews.util.Constants.ITEM_VIEW_MAIN
import workshop.akbolatss.tagsnews.util.UtilityMethods
import java.util.*

/**
 * Custom RecyclerView.Adapter for Favorite items
 */
class FavoritesAdapter(private val mClickListener: FavoritesListener) : RecyclerView.Adapter<FavoritesAdapter.NewsHolder>() {

    /**
     * List of favorites
     */
    private val mNewsList: MutableList<RssFeedItem> = ArrayList()

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
    fun onAddItems(rssItems: List<RssFeedItem>?) {
        if (rssItems != null) {
            mNewsList.clear()
            mNewsList.addAll(rssItems)
            notifyDataSetChanged()
        }
    }

    /**
     * Click listener
     */
    interface FavoritesListener {
        /**
         * Open details
         */
        fun onItemClick(rssFeedItem: RssFeedItem)
    }

    /**
     * Custom View Holder where View inits
     */
    inner class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(rssFeedItem: RssFeedItem, listener: FavoritesListener) {

            itemView.tvTitle.text = rssFeedItem.title
            if (rssFeedItem.pubDate != null && rssFeedItem.pubDate.isNotEmpty())
                itemView.tvTimestamp.text = UtilityMethods.convertTime(rssFeedItem.pubDate)

            itemView.tvDescription.text = rssFeedItem.description

            if (rssFeedItem.image != null) {
                itemView.imgImage.visibility = View.VISIBLE
                Picasso.with(itemView.context)
                        .load(rssFeedItem.image)
                        .placeholder(R.drawable.placeholder)
                        .into(itemView.imgImage)
            } else {
                itemView.imgImage.visibility = View.GONE
            }

            itemView.flItem.setOnClickListener {
                listener.onItemClick(rssFeedItem)
            }
        }
    }
}

package workshop.akbolatss.tagsnews.screen.recommendations

import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_search_item.view.*
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.util.Constants.EMPTY_ITEM
import java.util.*

class RecommendationsAdapter(private val mListener: onSourceClickListener) : RecyclerView.Adapter<RecommendationsAdapter.NewsHolder>() {

    private val mNewsList: MutableList<RssSource> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val mLayoutInflater = LayoutInflater.from(parent.context)
        val view = mLayoutInflater.inflate(R.layout.rv_search_item, parent, false)
        return NewsHolder(view!!)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val rssSource = mNewsList[position]
        holder.bind(rssSource, mListener)
    }

    override fun getItemCount(): Int {
        return mNewsList.size
    }

    fun onAddItems(rssSources: List<RssSource>?) {
        if (rssSources != null) {
            mNewsList.clear()
            mNewsList.addAll(rssSources)
            notifyDataSetChanged()
        }
    }

    fun onClearItems() {
        mNewsList.clear()
        notifyDataSetChanged()
    }

    interface onSourceClickListener {
        fun onSourceClick(rssSource: RssSource, toDelete: Boolean)
    }

    inner class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var isAdded: Boolean = false

        fun bind(rssSource: RssSource, listener: onSourceClickListener) {
            itemView.tvTitle.text = rssSource.title
            itemView.tvLink.text = rssSource.link?.removeRange(0, 5)
            itemView.tvSubscribers.text = rssSource.subscribers.toString() + " Subscribers"
            itemView.tvDescription.text = rssSource.description
            itemView.imgAdd.setImageResource(R.drawable.ic_add_24dp)
            isAdded = false

            Picasso.with(itemView.context)
                    .load(rssSource.visualUrl)
                    .placeholder(R.drawable.ic_rss_feed_24dp)
                    .error(R.drawable.ic_rss_feed_24dp)
                    .into(itemView.imgIcon)

            itemView.imgAdd.setOnClickListener {
                if (!isAdded) {
                    val builder = AlertDialog.Builder(itemView.context)
                    builder.setMessage(itemView.context.resources.getString(R.string.tv_add_channel) + " " + rssSource.title + " ?")
                    builder.setNegativeButton(R.string.tv_no, null)

                    builder.setPositiveButton(R.string.tv_yes, { dialog, _ ->
                        rssSource.isActive = true
                        listener.onSourceClick(rssSource, false)
                        itemView.imgAdd.setImageResource(R.drawable.ic_done_24dp)
                        isAdded = true
                        dialog.dismiss()
                    })
                    val dialog = builder.create()
                    dialog.show()
                } else {
                    listener.onSourceClick(rssSource, true)
                    itemView.imgAdd.setImageResource(R.drawable.ic_add_24dp)
                    isAdded = false
                }
            }
        }
    }
}

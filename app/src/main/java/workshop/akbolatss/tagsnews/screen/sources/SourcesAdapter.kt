package workshop.akbolatss.tagsnews.screen.sources

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_source_item.view.*
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.screen.sources.helper.ItemTouchHelperAdapter
import workshop.akbolatss.tagsnews.screen.sources.helper.ItemTouchHelperViewHolder
import java.util.*

/**
 * Custom RecyclerView.Adapter for RSS source items
 */
class SourcesAdapter(private val mClickListener: SourceListener) : RecyclerView.Adapter<SourcesAdapter.NewsHolder>(), ItemTouchHelperAdapter {

    private val mSourcesList: MutableList<RssSource> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val mLayoutInflater = LayoutInflater.from(parent.context)
        val view = mLayoutInflater.inflate(R.layout.rv_source_item, parent, false)
        return NewsHolder(view)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val rssSource = mSourcesList[position]
        holder.bind(rssSource, mClickListener)
    }

    override fun getItemCount(): Int {
        return mSourcesList.size
    }

    /**
     * Load list of items to this adapter
     */
    fun onAddItems(rssItems: List<RssSource>?) {
        if (rssItems != null) {
            mSourcesList.clear()
            mSourcesList.addAll(rssItems)
            notifyDataSetChanged()
        }
    }

    /**
     * Add item to this adapter
     */
    fun onAddItem(rssSource: RssSource?) {
        if (rssSource != null) {
            mSourcesList.add(rssSource)
            notifyItemInserted(mSourcesList.size + 1)
        }
    }

    /**
     * Update item in specific position
     * @param pos position
     */
    fun onUpdateItem(rssSource: RssSource?, pos: Int) {
        if (rssSource != null) {
            mSourcesList[pos] = rssSource
            notifyItemChanged(pos)
        }
    }

    /**
     * Remove item from specific position
     * @param pos position
     */
    fun onRemoveItem(pos: Int) {
        mSourcesList.removeAt(pos)
        notifyItemRemoved(pos)
    }

    /**
     * Swap items
     */
    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(mSourcesList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemsMoved(fromPosition: Int, toPosition: Int) {
        mClickListener.onItemsSwapped(mSourcesList[fromPosition], mSourcesList[toPosition])
    }

    interface SourceListener {

        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)

        fun onItemsSwapped(from: RssSource, to: RssSource)

        fun onSourceOptions(rssSource: RssSource, view: View, pos: Int)

        fun onSourceSwitch(rssSource: RssSource, isActive: Boolean, pos: Int)
    }

    /**
     * Custom View Holder where View instantiates
     */
    inner class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(rssSource: RssSource, listener: SourceListener) {
            itemView.tvTitle.text = rssSource.title
            itemView.tvLink.text = rssSource.link
            itemView.tvSubscribers.text = rssSource.subscribers.toString() + " Subscribers"
            itemView.tvDescription.text = rssSource.description

            Picasso.with(itemView.context)
                    .load(rssSource.visualUrl)
                    .placeholder(R.drawable.ic_rss_feed_24dp)
                    .error(R.drawable.ic_rss_feed_24dp)
                    .into(itemView.imgIcon)

            iconState(rssSource.isActive)

            itemView.imgOptions.setOnClickListener {
                listener.onSourceOptions(rssSource, it, adapterPosition)
            }

            itemView.imgAdd.setOnClickListener {
                listener.onSourceSwitch(rssSource, !rssSource.isActive, adapterPosition)
                iconState(rssSource.isActive)
            }
            itemView.flSwap.setOnTouchListener { _, _ ->
                listener.onStartDrag(this)
                return@setOnTouchListener true
            }
        }

        private fun iconState(isActive: Boolean) {
            if (isActive) {
                itemView.imgAdd.setImageResource(R.drawable.ic_done_24dp)
            } else {
                itemView.imgAdd.setImageResource(R.drawable.ic_add_24dp)
            }
        }
    }
}

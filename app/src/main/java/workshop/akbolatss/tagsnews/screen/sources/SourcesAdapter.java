package workshop.akbolatss.tagsnews.screen.sources;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.model.dao.RssSource;
import workshop.akbolatss.tagsnews.screen.sources.helper.ItemTouchHelperAdapter;
import workshop.akbolatss.tagsnews.screen.sources.helper.ItemTouchHelperViewHolder;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.NewsHolder> implements ItemTouchHelperAdapter {

    private List<RssSource> mSourcesList;

    private final OnRssClickListener mClickListener;

    public SourcesAdapter(OnRssClickListener mClickListener) {
        mSourcesList = new ArrayList<>();
        this.mClickListener = mClickListener;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View view = mLayoutInflater.inflate(R.layout.rv_source_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        RssSource rssSource = mSourcesList.get(position);
        holder.bind(rssSource, mClickListener);

    }

    @Override
    public int getItemCount() {
        if (mSourcesList == null) {
            return 0;
        }
        return mSourcesList.size();
    }

    public void onAddItems(List<RssSource> rssItems) {
        if (rssItems != null) {
            mSourcesList.clear();
            mSourcesList.addAll(rssItems);
            notifyDataSetChanged();
        }
    }

    public void onAddItem(RssSource rssSource) {
        if (rssSource != null) {
            mSourcesList.add(rssSource);
            notifyItemInserted(mSourcesList.size() + 1);
        }
    }

    public void onUpdateItem(RssSource rssSource, int pos) {
        if (rssSource != null) {
            mSourcesList.set(pos, rssSource);
            notifyItemChanged(pos);
        }
    }

    public void onRemoveItem(int pos) {
        mSourcesList.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mSourcesList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemsMoved(int fromPosition, int toPosition) {
        mClickListener.onItemsSwapped(mSourcesList.get(fromPosition), mSourcesList.get(toPosition));
    }

    public interface OnRssClickListener {

        public void onItemsSwapped(RssSource from, RssSource to);

        public void onSourceOptions(RssSource rssSource, View view, int pos);

        public void onSourceSwitch(RssSource rssSource, boolean isActive, int pos);
    }

    public class NewsHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

//        @BindView(R.id.cardView)
//        CardView cardView;
//        @BindView(R.id.tvTitle)
//        TextView mTitle;
//        @BindView(R.id.tvLink)
//        TextView mLink;
//        @BindView(R.id.imgIcon)
//        ImageView mImgIcon;
//        @BindView(R.id.imgOptions)
//        ImageView mImgOptions;
//        @BindView(R.id.cbInit)
        CheckBox mCheckBox;
        Context context;

        public NewsHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
        }

        public void bind(final RssSource rssSource, final OnRssClickListener listener) {
//            mTitle.setText(rssSource.getTitle());
//            mLink.setText(rssSource.getLink());
//            mCheckBox.setChecked(rssSource.getIsActive());
//
//            Picasso.with(context)
//                    .load(rssSource.getVisualUrl())
//                    .placeholder(R.drawable.ic_rss_feed_24dp)
//                    .error(R.drawable.ic_rss_feed_24dp)
//                    .into(mImgIcon);
//
//            mImgOptions.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onSourceOptions(rssSource, v, getLayoutPosition());
//                }
//            });

            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listener.onSourceSwitch(rssSource, isChecked, getLayoutPosition());
                }
            });
        }

        @Override
        public void onItemSelected() {
//            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorCardDragged));
        }

        @Override
        public void onItemClear() {
//            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorCardBackground));
        }
    }
}

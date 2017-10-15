package workshop.akbolatss.tagsnews.screen.sources;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.screen.sources.helper.ItemTouchHelperAdapter;
import workshop.akbolatss.tagsnews.screen.sources.helper.ItemTouchHelperViewHolder;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.NewsHolder> implements ItemTouchHelperAdapter {

    private List<RssSource> mSourcesList;

    private final OnRssClickInterface mClickInterface;

    public SourcesAdapter(OnRssClickInterface mClickInterface) {
        mSourcesList = new ArrayList<>();
        this.mClickInterface = mClickInterface;
    }

    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RssSource rssSource = (RssSource) view.getTag();
            mClickInterface.onItemCheckBoxClick(rssSource, view);
        }
    };

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View view = mLayoutInflater.inflate(R.layout.rv_source_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        RssSource rssSource = mSourcesList.get(position);
        holder.bind(rssSource);

        holder.mImgOptions.setOnClickListener(mInternalListener);
        holder.mImgOptions.setTag(rssSource);

        holder.mCheckBox.setOnClickListener(mInternalListener);
        holder.mCheckBox.setTag(rssSource);
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

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mSourcesList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemsMoved(int fromPosition, int toPosition) {
        mClickInterface.onItemsSwapped(mSourcesList.get(fromPosition), mSourcesList.get(toPosition));
    }

    public interface OnRssClickInterface {

        public void onItemsSwapped(RssSource from, RssSource to);

        public void onItemCheckBoxClick(RssSource rssSource, View view);
    }

    public class NewsHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.tvTitle)
        TextView mTitle;
        @BindView(R.id.tvLink)
        TextView mLink;
        @BindView(R.id.imgOptions)
        ImageView mImgOptions;
        @BindView(R.id.cbInit)
        CheckBox mCheckBox;

        Context context;

        public NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void bind(RssSource rssItem) {
            mTitle.setText(rssItem.getTitle());
            mLink.setText(rssItem.getLink());
            mCheckBox.setChecked(rssItem.getIsActive());
        }

        @Override
        public void onItemSelected() {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimarySecondary));
        }

        @Override
        public void onItemClear() {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }
}

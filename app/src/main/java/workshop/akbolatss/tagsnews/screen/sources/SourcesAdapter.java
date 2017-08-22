package workshop.akbolatss.tagsnews.screen.sources;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.NewsHolder> {

    private List<RssSource> mNewsList;

    private final OnRssClickInterface mClickInterface;

    public SourcesAdapter(OnRssClickInterface mClickInterface) {
        mNewsList = new ArrayList<>();
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
        RssSource rssSource = mNewsList.get(position);
        holder.bind(rssSource);

        holder.mImgOptions.setOnClickListener(mInternalListener);
        holder.mImgOptions.setTag(rssSource);

        holder.mCheckBox.setOnClickListener(mInternalListener);
        holder.mCheckBox.setTag(rssSource);

    }

    @Override
    public int getItemCount() {
        if (mNewsList == null) {
            return 0;
        }
        return mNewsList.size();
    }

    public void onAddItems(List<RssSource> rssItems) {
        if (rssItems != null) {
            mNewsList.clear();
            mNewsList.addAll(rssItems); // TODO throws exception NULL POINTER
            notifyDataSetChanged();
        }
    }

    public interface OnRssClickInterface {

        public void onItemCheckBoxClick(RssSource rssSource, View view);
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView mTitle;
        @BindView(R.id.tvLink)
        TextView mLink;
        @BindView(R.id.imgOptions)
        ImageView mImgOptions;
        @BindView(R.id.cbInit)
        CheckBox mCheckBox;

        public NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(RssSource rssItem) {
            mTitle.setText(rssItem.getTitle());
            mLink.setText(rssItem.getLink());
            mCheckBox.setChecked(rssItem.getIsActive());
        }
    }
}

package workshop.akbolatss.tagsnews.screen.sources;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

public class SourcesSearchAdapter extends RecyclerView.Adapter<SourcesSearchAdapter.NewsHolder> {

    private List<RssSource> mNewsList;

    private final SourcesAdapter.OnRssClickInterface mClickInterface;

    public SourcesSearchAdapter(SourcesAdapter.OnRssClickInterface mClickInterface) {
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
        View view = mLayoutInflater.inflate(R.layout.rv_search_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        RssSource rssSource = mNewsList.get(position);
        holder.bind(rssSource);

        holder.mCBox.setOnClickListener(mInternalListener);
        holder.mCBox.setTag(rssSource);
    }

    @Override
    public int getItemCount() {
        if (mNewsList == null) {
            return 0;
        }
        return mNewsList.size();
    }

    public void onAddItems(List<RssSource> rssSources) {
        if (rssSources != null) {
            mNewsList.clear();
            mNewsList.addAll(rssSources); // TODO throws exception NULL POINTER
            notifyDataSetChanged();
        }
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView mTitle;
        @BindView(R.id.tvLink)
        TextView mLink;
        @BindView(R.id.cbAdd)
        CheckBox mCBox;

        public NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(RssSource rssSource) {
            mTitle.setText(rssSource.getTitle());
            mLink.setText(rssSource.getLink());
        }
    }
}

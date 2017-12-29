package workshop.akbolatss.tagsnews.screen.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.R;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsHolder> {

    private List<RssItem> mNewsList;
    private int mViewItemMode;

    private final OnRssClickListener mClickListener;

    public NewsListAdapter(OnRssClickListener mClickListener, int mViewItemMode) {
        mNewsList = new ArrayList<>();
        this.mViewItemMode = mViewItemMode;
        this.mClickListener = mClickListener;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View view = null;
        switch (mViewItemMode) {
            case 0:
                view = mLayoutInflater.inflate(R.layout.rv_news_item_text_only, parent, false);
                break;
            case 1:
                view = mLayoutInflater.inflate(R.layout.rv_news_item_small, parent, false);
                break;
            case 2:
                view = mLayoutInflater.inflate(R.layout.rv_news_item, parent, false);
                break;
            default:
        }
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        RssItem rssItem = mNewsList.get(position);
        holder.bind(rssItem, mViewItemMode == 0, mClickListener);

    }

    @Override
    public int getItemCount() {
        if (mNewsList == null) {
            return 0;
        }
        return mNewsList.size();
    }

    public void onAddItems(List<RssItem> rssItems) {
        if (rssItems != null) {
            mNewsList.addAll(rssItems);
            notifyDataSetChanged();
        }
    }

    public interface OnRssClickListener {
        public void OnItemClick(RssItem rssItem);
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        private Context mContext;
        @BindView(R.id.imgView)
        ImageView mImage;
        @BindView(R.id.tvTitle)
        TextView mTitle;
        @BindView(R.id.tvDescription)
        TextView mDescription;
        @BindView(R.id.tvTimestamp)
        TextView mTimestamp;
        @BindView(R.id.frameLayout)
        FrameLayout mFrameLayout;

        public NewsHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(final RssItem rssItem, boolean isTextOnly, final OnRssClickListener listener) {
            mTitle.setText(rssItem.getTitle());
            mTimestamp.setText(rssItem.getPublishDate());

            mFrameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(rssItem);
                }
            });

            if (!isTextOnly) {
                mDescription.setText(rssItem.getDescription());

                if (rssItem.getImage() != null) {
                    mImage.setVisibility(View.VISIBLE);
                    Picasso.with(mContext)
                            .load(rssItem.getImage())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
//                            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                            .placeholder(R.drawable.placeholder)
                            .into(mImage);
                } else {
                    mImage.setVisibility(View.GONE);
                }
            }
        }
    }
}

package workshop.akbolatss.tagsnews.screen.favorites;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.screen.news.NewsListAdapter;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.NewsHolder> {

    private List<RssItem> mNewsList;
    private int mViewItemMode;

    private final NewsListAdapter.OnRssClickListener mClickListener;

    public FavoritesListAdapter(NewsListAdapter.OnRssClickListener clickListener, int mViewItemMode) {
        mNewsList = new ArrayList<>();
        this.mClickListener = clickListener;
        this.mViewItemMode = mViewItemMode;
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
        holder.bind(rssItem, mClickListener);

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

    public class NewsHolder extends RecyclerView.ViewHolder {

        private Context mContext;
//        @BindView(R.id.imgView)
//        ImageView mImage;
//        @BindView(R.id.tvTitle)
//        TextView mTitle;
//        @BindView(R.id.tvDescription)
//        TextView mDescription;
//        @BindView(R.id.tvTimestamp)
//        TextView mTimestamp;
//        @BindView(R.id.frameLayout)
//        FrameLayout mFrameLayout;

        public NewsHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();

        }

        public void bind(final RssItem rssItem, final NewsListAdapter.OnRssClickListener mClickListener) {
//            mTitle.setText(rssItem.getTitle());
//            mTimestamp.setText(rssItem.getPublishDate());
//
//            mDescription.setText(rssItem.getDescription());
//
//            mFrameLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mClickListener.OnItemClick(rssItem);
//                }
//            });
//
//            if (rssItem.getImage() != null) {
//                mImage.setVisibility(View.VISIBLE);
//                Picasso.with(mContext)
//                        .load(rssItem.getImage())
//                        .placeholder(R.drawable.placeholder)
//                        .into(mImage);
//            } else {
//                mImage.setVisibility(View.GONE);
//            }
        }
    }
}

package workshop.akbolatss.tagsnews.screen.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.base.BaseView;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsHolder> {


    private List<RssItem> mNewsList;

    private final OnRssClickInterface mClickInterface;

    public NewsListAdapter(OnRssClickInterface mClickInterface) {
        mNewsList = new ArrayList<>();
        this.mClickInterface = mClickInterface;
    }

    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RssItem rssItem = (RssItem) view.getTag();
            mClickInterface.OnItemClick(rssItem);
        }
    };

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View view = mLayoutInflater.inflate(R.layout.rv_news_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        RssItem rssItem = mNewsList.get(position);
        holder.bind(rssItem);


        holder.mFrameLayout.setOnClickListener(mInternalListener);
        holder.mFrameLayout.setTag(rssItem);
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
            mNewsList.addAll(rssItems); // TO-DO throws exception NULL POINTER
            notifyDataSetChanged();
        }

        if (rssItems.size() == 0) {
            Log.d("BolaDebug","RSS size is NULL");
        }
    }

    public interface OnRssClickInterface{

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

        public void bind(RssItem rssItem) {
            mTitle.setText(rssItem.getTitle());
            mDescription.setText(rssItem.getDescription());

            mTimestamp.setText(rssItem.getPublishDate());

            Picasso.with(mContext)
                    .load(rssItem.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(mImage);
        }
    }
}

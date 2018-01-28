package workshop.akbolatss.tagsnews.screen.recommendations;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.model.dao.RssSource;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.NewsHolder> {

    private List<RssSource> mNewsList;
    private onSourceClickListener mListener;

    public RecommendationsAdapter(onSourceClickListener mListener) {
        mNewsList = new ArrayList<>();
        this.mListener = mListener;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View view = mLayoutInflater.inflate(R.layout.rv_search_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        RssSource rssSource = mNewsList.get(position);
        holder.bind(rssSource, mListener);
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
            mNewsList.addAll(rssSources);
            notifyDataSetChanged();
        }
    }

    public void onClearItems() {
        mNewsList.clear();
        notifyDataSetChanged();
    }

    public interface onSourceClickListener {
        public void onSourceClick(RssSource rssSource, boolean toDelete);
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btnAdd)
        ImageView imgAdd;
        @BindView(R.id.tvTitle)
        TextView mTitle;
        @BindView(R.id.tvLink)
        TextView mLink;
        @BindView(R.id.imgIcon)
        ImageView imgIcon;
        Context context;
        boolean isAdded;

        public NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void bind(final RssSource rssSource, final onSourceClickListener listener) {
            mTitle.setText(rssSource.getTitle());
            mLink.setText(rssSource.getLink());

            Picasso.with(context)
                    .load(rssSource.getVisualUrl())
                    .placeholder(R.drawable.ic_rss_feed_24dp)
                    .error(R.drawable.ic_rss_feed_24dp)
                    .into(imgIcon);


            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isAdded) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setNegativeButton(R.string.tv_no, null);
                        builder.setMessage(context.getResources().getString(R.string.tv_add_channel) + " " + rssSource.getTitle() + " ?");

                        builder.setPositiveButton(R.string.tv_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                rssSource.setIsActive(true);
                                listener.onSourceClick(rssSource, false);
                                imgAdd.setImageResource(R.drawable.ic_done_24dp);
                                isAdded = true;
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
//                  TODO: Instant REMOVE added RSS Channel
//                  listener.onSourceClick(rssSource, true);
//                  imgAdd.setImageResource(R.drawable.ic_add_24dp);
//                  isAdded = false;
                }
            });

        }
    }
}

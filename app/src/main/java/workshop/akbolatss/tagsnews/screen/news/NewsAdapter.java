package workshop.akbolatss.tagsnews.screen.news;

import android.content.Context;
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
import workshop.akbolatss.tagsnews.screen.news.model.Article;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {


    private LayoutInflater mLayoutInflater;
    private List<Article> mNewsList;

    public NewsAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
        mNewsList = new ArrayList<>();
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.rv_news_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        holder.bind(mNewsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void onAddArticles(List<Article> articles) {
        mNewsList.addAll(articles);
        notifyDataSetChanged();
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

        public NewsHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(Article article) {
            mTitle.setText(article.getTitle());
            mDescription.setText(article.getDescription());

            mTimestamp.setText(article.getTimestamp());

            Picasso.with(mContext).load(article.getImageUrl())
                    .into(mImage);
        }
    }
}

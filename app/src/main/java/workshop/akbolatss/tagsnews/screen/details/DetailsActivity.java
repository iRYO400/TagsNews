package workshop.akbolatss.tagsnews.screen.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerDetailsComponent;
import workshop.akbolatss.tagsnews.di.module.DetailsModule;
import workshop.akbolatss.tagsnews.util.Constants;

/**
 * Created by AkbolatSS on 10.08.2017.
 */

public class DetailsActivity extends BaseActivity implements DetailsView {

    @Inject
    protected DetailsPresenter mPresenter;

    @Inject
    protected Context mContext;

    private RssItem mRssItem;
    @BindView(R.id.tvTitle)
    protected TextView mTitle;
    @BindView(R.id.tvTimestamp)
    protected TextView mTimestamp;
    @BindView(R.id.tvDescription)
    protected TextView mDescription;
    @BindView(R.id.imgView)
    protected ImageView mImage;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        DaggerDetailsComponent.builder()
                .appComponent(App.getAppComponent())
                .detailsModule(new DetailsModule(this))
                .build().inject(this);

        mRssItem = (RssItem) intent.getSerializableExtra(Constants.INTENT_RSS_ITEM);

        if (mRssItem != null){
            mTitle.setText(mRssItem.getTitle());
            mTimestamp.setText(mRssItem.getPublishDate());
            mDescription.setText(mRssItem.getDescription());

            Picasso.with(mContext)
                    .load(mRssItem.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(mImage);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_details;
    }

    @OnClick(R.id.btnOpenSource)
    @Override
    public void onOpenSource() {
        //Start Custom ChromeExtension
        Toast.makeText(mContext, "Yeah! " + mRssItem.getTitle(), Toast.LENGTH_SHORT).show();
    }
}

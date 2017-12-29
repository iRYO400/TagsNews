package workshop.akbolatss.tagsnews.screen.news;

import android.support.v4.app.Fragment;

import workshop.akbolatss.tagsnews.screen.recommendations.RecommendationsFragment;

public class NewsSource {

    private String mName;
    private String mUrl;
    private Fragment mFragment;
    private boolean isRecommendations;

    public NewsSource(String mName, String mUrl, boolean isRecommendations) {
        this.mName = mName;
        this.mUrl = mUrl;
        this.isRecommendations = isRecommendations;
        if (isRecommendations) {
            mFragment = new RecommendationsFragment();
        } else {
            mFragment = new NewsFragment();
        }
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public boolean isRecommendations() {
        return isRecommendations;
    }
}

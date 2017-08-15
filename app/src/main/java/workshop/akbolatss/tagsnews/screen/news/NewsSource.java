package workshop.akbolatss.tagsnews.screen.news;

/**
 * Created by AkbolatSS on 10.08.2017.
 */

public class NewsSource {

    private String mName;
    private String mUrl;
    private NewsFragment mFragment;

    public NewsSource(String mName, String mUrl) {
        this.mName = mName;
        this.mUrl = mUrl;
        mFragment = new NewsFragment();
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public NewsFragment getFragment() {
        return mFragment;
    }
}

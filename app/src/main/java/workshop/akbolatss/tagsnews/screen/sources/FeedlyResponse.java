package workshop.akbolatss.tagsnews.screen.sources;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.Observable;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

/**
 * Created by AkbolatSS on 17.08.2017.
 */

public class FeedlyResponse {

    @SerializedName("results")
    private List<RssSource> rssSourceList;

    @SerializedName("related")
    private List<String> related;

    public List<RssSource> getRssSourceList() {
        return rssSourceList;
    }

    public List<String> getRelated() {
        return related;
    }
}

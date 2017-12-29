package workshop.akbolatss.tagsnews.repositories;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.Observable;
import workshop.akbolatss.tagsnews.repositories.BaseResponse;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

public class FeedlyResponse extends BaseResponse {

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

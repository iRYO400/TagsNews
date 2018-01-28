package workshop.akbolatss.tagsnews.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import workshop.akbolatss.tagsnews.model.dao.RssSource;

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

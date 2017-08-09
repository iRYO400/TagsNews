package workshop.akbolatss.tagsnews.screen.news.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public class News  {

    @SerializedName("status")
    private String status;
    @SerializedName("source")
    private String source;
    @SerializedName("sortBy")
    private String sortBy;

    @SerializedName("articles")
    private List<Article> articles;


    public String getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public List<Article> getArticles() {
        return articles;
    }
}

package workshop.akbolatss.tagsnews.screen.news.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public class Article {

    @SerializedName("author")
    private String author;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("url")
    private String url;
    @SerializedName("urlToImage")
    private String imageUrl;
    @SerializedName("publishedAt")
    private String timestamp;

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

package workshop.akbolatss.tagsnews.repositories.source;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "RSS_FEED_ITEM".
 */
@Entity
public class RssFeedItem {

    @Id(autoincrement = true)
    private Long id;
    private String feed_title;
    private String feed_link;
    private String feed_pubDate;
    private String feed_image;
    private String feed_description;
    private String feed_source;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated(hash = 10683631)
    public RssFeedItem() {
    }

    public RssFeedItem(Long id) {
        this.id = id;
    }

    @Generated(hash = 473389392)
    public RssFeedItem(Long id, String feed_title, String feed_link, String feed_pubDate, String feed_image, String feed_description, String feed_source) {
        this.id = id;
        this.feed_title = feed_title;
        this.feed_link = feed_link;
        this.feed_pubDate = feed_pubDate;
        this.feed_image = feed_image;
        this.feed_description = feed_description;
        this.feed_source = feed_source;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeed_title() {
        return feed_title;
    }

    public void setFeed_title(String feed_title) {
        this.feed_title = feed_title;
    }

    public String getFeed_link() {
        return feed_link;
    }

    public void setFeed_link(String feed_link) {
        this.feed_link = feed_link;
    }

    public String getFeed_pubDate() {
        return feed_pubDate;
    }

    public void setFeed_pubDate(String feed_pubDate) {
        this.feed_pubDate = feed_pubDate;
    }

    public String getFeed_image() {
        return feed_image;
    }

    public void setFeed_image(String feed_image) {
        this.feed_image = feed_image;
    }

    public String getFeed_description() {
        return feed_description;
    }

    public void setFeed_description(String feed_description) {
        this.feed_description = feed_description;
    }

    public String getFeed_source() {
        return feed_source;
    }

    public void setFeed_source(String feed_source) {
        this.feed_source = feed_source;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}

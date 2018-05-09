package workshop.akbolatss.tagsnews.model.dao;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.*;

/**
 * Entity mapped to table "RSS_SOURCE". This class represents Rss source
 * @see workshop.akbolatss.tagsnews.model.DBRssSourceRepository
 */
@Entity
public class RssSource {

    @Id(autoincrement = true)
    private Long id;
    /**
     * Position index
     */
    private Integer positionIndex;
    /**
     * Is source active
     */
    private Boolean isActive;
    /**
     * Title
     */
    @SerializedName("title")
    private String title;
    /**
     * Link to RSS of source
     */
    @SerializedName("feedId")
    private String link;
    /**
     * Rss description
     */
    @SerializedName("description")
    private String description;
    /**
     * Subscribers count
     */
    @SerializedName("subscribers")
    private int subscribers;
    /**
     * Link to website
     */
    @SerializedName("website")
    private String website;

    /**
     * Link to RSS icon
     */
    @SerializedName("visualUrl")
    private String visualUrl;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated(hash = 1330183901)
    public RssSource() {
    }

    public RssSource(Long id) {
        this.id = id;
    }

    @Generated(hash = 812238265)
    public RssSource(Long id, Integer positionIndex, Boolean isActive, String title, String link, String description, int subscribers, String website, String visualUrl) {
        this.id = id;
        this.positionIndex = positionIndex;
        this.isActive = isActive;
        this.title = title;
        this.link = link;
        this.description = description;
        this.subscribers = subscribers;
        this.website = website;
        this.visualUrl = visualUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(Integer positionIndex) {
        this.positionIndex = positionIndex;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getVisualUrl() {
        return visualUrl;
    }

    public void setVisualUrl(String visualUrl) {
        this.visualUrl = visualUrl;
    }

    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(int subscribers) {
        this.subscribers = subscribers;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}

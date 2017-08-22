package workshop.akbolatss.tagsnews.repositories.source;

import org.greenrobot.greendao.annotation.*;

import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import org.greenrobot.greendao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "RSS_FEED_ITEM".
 */
@Entity(active = true)
public class RssFeedItem {

    @Id(autoincrement = true)
    private Long id;
    private String title;
    private String link;
    private String pubDate;
    private String image;
    private String description;
    private Long rssSourceId;

    /** Used to resolve relations */
    @Generated
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated
    private transient RssFeedItemDao myDao;

    @ToOne(joinProperty = "rssSourceId")
    private RssFeedItem rssFeedItem;

    @Generated
    private transient Long rssFeedItem__resolvedKey;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public RssFeedItem() {
    }

    public RssFeedItem(Long id) {
        this.id = id;
    }

    @Generated
    public RssFeedItem(Long id, String title, String link, String pubDate, String image, String description, Long rssSourceId) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.image = image;
        this.description = description;
        this.rssSourceId = rssSourceId;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRssFeedItemDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRssSourceId() {
        return rssSourceId;
    }

    public void setRssSourceId(Long rssSourceId) {
        this.rssSourceId = rssSourceId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated
    public RssFeedItem getRssFeedItem() {
        Long __key = this.rssSourceId;
        if (rssFeedItem__resolvedKey == null || !rssFeedItem__resolvedKey.equals(__key)) {
            __throwIfDetached();
            RssFeedItemDao targetDao = daoSession.getRssFeedItemDao();
            RssFeedItem rssFeedItemNew = targetDao.load(__key);
            synchronized (this) {
                rssFeedItem = rssFeedItemNew;
            	rssFeedItem__resolvedKey = __key;
            }
        }
        return rssFeedItem;
    }

    @Generated
    public void setRssFeedItem(RssFeedItem rssFeedItem) {
        synchronized (this) {
            this.rssFeedItem = rssFeedItem;
            rssSourceId = rssFeedItem == null ? null : rssFeedItem.getId();
            rssFeedItem__resolvedKey = rssSourceId;
        }
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void delete() {
        __throwIfDetached();
        myDao.delete(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void update() {
        __throwIfDetached();
        myDao.update(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void refresh() {
        __throwIfDetached();
        myDao.refresh(this);
    }

    @Generated
    private void __throwIfDetached() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}

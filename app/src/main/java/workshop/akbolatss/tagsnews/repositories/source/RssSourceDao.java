package workshop.akbolatss.tagsnews.repositories.source;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RSS_SOURCE".
*/
public class RssSourceDao extends AbstractDao<RssSource, Long> {

    public static final String TABLENAME = "RSS_SOURCE";

    /**
     * Properties of entity RssSource.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PositionIndex = new Property(1, Integer.class, "positionIndex", false, "POSITION_INDEX");
        public final static Property IsActive = new Property(2, Boolean.class, "isActive", false, "IS_ACTIVE");
        public final static Property Title = new Property(3, String.class, "title", false, "TITLE");
        public final static Property Link = new Property(4, String.class, "link", false, "LINK");
        public final static Property Description = new Property(5, String.class, "description", false, "DESCRIPTION");
        public final static Property Subscribers = new Property(6, int.class, "subscribers", false, "SUBSCRIBERS");
        public final static Property Website = new Property(7, String.class, "website", false, "WEBSITE");
        public final static Property VisualUrl = new Property(8, String.class, "visualUrl", false, "VISUAL_URL");
    }


    public RssSourceDao(DaoConfig config) {
        super(config);
    }
    
    public RssSourceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RSS_SOURCE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"POSITION_INDEX\" INTEGER," + // 1: positionIndex
                "\"IS_ACTIVE\" INTEGER," + // 2: isActive
                "\"TITLE\" TEXT," + // 3: title
                "\"LINK\" TEXT," + // 4: link
                "\"DESCRIPTION\" TEXT," + // 5: description
                "\"SUBSCRIBERS\" INTEGER NOT NULL ," + // 6: subscribers
                "\"WEBSITE\" TEXT," + // 7: website
                "\"VISUAL_URL\" TEXT);"); // 8: visualUrl
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RSS_SOURCE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RssSource entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer positionIndex = entity.getPositionIndex();
        if (positionIndex != null) {
            stmt.bindLong(2, positionIndex);
        }
 
        Boolean isActive = entity.getIsActive();
        if (isActive != null) {
            stmt.bindLong(3, isActive ? 1L: 0L);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(5, link);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(6, description);
        }
        stmt.bindLong(7, entity.getSubscribers());
 
        String website = entity.getWebsite();
        if (website != null) {
            stmt.bindString(8, website);
        }
 
        String visualUrl = entity.getVisualUrl();
        if (visualUrl != null) {
            stmt.bindString(9, visualUrl);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RssSource entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer positionIndex = entity.getPositionIndex();
        if (positionIndex != null) {
            stmt.bindLong(2, positionIndex);
        }
 
        Boolean isActive = entity.getIsActive();
        if (isActive != null) {
            stmt.bindLong(3, isActive ? 1L: 0L);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(5, link);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(6, description);
        }
        stmt.bindLong(7, entity.getSubscribers());
 
        String website = entity.getWebsite();
        if (website != null) {
            stmt.bindString(8, website);
        }
 
        String visualUrl = entity.getVisualUrl();
        if (visualUrl != null) {
            stmt.bindString(9, visualUrl);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RssSource readEntity(Cursor cursor, int offset) {
        RssSource entity = new RssSource( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // positionIndex
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // isActive
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // title
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // link
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // description
            cursor.getInt(offset + 6), // subscribers
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // website
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // visualUrl
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RssSource entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPositionIndex(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setIsActive(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setTitle(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLink(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDescription(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSubscribers(cursor.getInt(offset + 6));
        entity.setWebsite(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setVisualUrl(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RssSource entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RssSource entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RssSource entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

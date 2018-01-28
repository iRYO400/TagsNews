package workshop.akbolatss.tagsnews.repositories.source;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import workshop.akbolatss.tagsnews.repositories.source.ReminderItem;
import workshop.akbolatss.tagsnews.repositories.source.RssFeedItem;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

import workshop.akbolatss.tagsnews.repositories.source.ReminderItemDao;
import workshop.akbolatss.tagsnews.repositories.source.RssFeedItemDao;
import workshop.akbolatss.tagsnews.repositories.source.RssSourceDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig reminderItemDaoConfig;
    private final DaoConfig rssFeedItemDaoConfig;
    private final DaoConfig rssSourceDaoConfig;

    private final ReminderItemDao reminderItemDao;
    private final RssFeedItemDao rssFeedItemDao;
    private final RssSourceDao rssSourceDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        reminderItemDaoConfig = daoConfigMap.get(ReminderItemDao.class).clone();
        reminderItemDaoConfig.initIdentityScope(type);

        rssFeedItemDaoConfig = daoConfigMap.get(RssFeedItemDao.class).clone();
        rssFeedItemDaoConfig.initIdentityScope(type);

        rssSourceDaoConfig = daoConfigMap.get(RssSourceDao.class).clone();
        rssSourceDaoConfig.initIdentityScope(type);

        reminderItemDao = new ReminderItemDao(reminderItemDaoConfig, this);
        rssFeedItemDao = new RssFeedItemDao(rssFeedItemDaoConfig, this);
        rssSourceDao = new RssSourceDao(rssSourceDaoConfig, this);

        registerDao(ReminderItem.class, reminderItemDao);
        registerDao(RssFeedItem.class, rssFeedItemDao);
        registerDao(RssSource.class, rssSourceDao);
    }
    
    public void clear() {
        reminderItemDaoConfig.clearIdentityScope();
        rssFeedItemDaoConfig.clearIdentityScope();
        rssSourceDaoConfig.clearIdentityScope();
    }

    public ReminderItemDao getReminderItemDao() {
        return reminderItemDao;
    }

    public RssFeedItemDao getRssFeedItemDao() {
        return rssFeedItemDao;
    }

    public RssSourceDao getRssSourceDao() {
        return rssSourceDao;
    }

}

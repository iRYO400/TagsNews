package workshop.akbolatss.tagsnews.model;


import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import workshop.akbolatss.tagsnews.model.dao.DaoSession;
import workshop.akbolatss.tagsnews.model.dao.ReminderItem;
import workshop.akbolatss.tagsnews.model.dao.ReminderItemDao;

public class DBReminderItemRepository implements ReminderItemRepository {

    private DaoSession mDaoSession;

    public DBReminderItemRepository(DaoSession daoSession) {
        this.mDaoSession = daoSession;
    }

    @Override
    public Single<List<ReminderItem>> onLoadReminders() {
        return Single.fromCallable(new Callable<List<ReminderItem>>() {
            @Override
            public List<ReminderItem> call() throws Exception {
                ReminderItemDao reminderItemDao = mDaoSession.getReminderItemDao();
                return reminderItemDao.loadAll();
            }
        });
    }

    @Override
    public void onAddReminder(ReminderItem rItem) {
        ReminderItemDao reminderItemDao = mDaoSession.getReminderItemDao();
        reminderItemDao.insert(rItem);
    }

    @Override
    public void onRemoveReminder(ReminderItem rItem) {
        ReminderItemDao reminderItemDao = mDaoSession.getReminderItemDao();
        reminderItemDao.delete(rItem);
    }

    @Override
    public void onUpdateReminder(ReminderItem rItem) {
        ReminderItemDao reminderItemDao = mDaoSession.getReminderItemDao();
        reminderItemDao.update(rItem);
    }

    @Override
    public void onActivateReminder(ReminderItem rItem) {
        ReminderItemDao reminderItemDao = mDaoSession.getReminderItemDao();
        rItem.setIsActive(true);
        reminderItemDao.update(rItem);
    }

    @Override
    public void onDeactivateReminder(ReminderItem rItem) {
        ReminderItemDao reminderItemDao = mDaoSession.getReminderItemDao();
        rItem.setIsActive(false);
        reminderItemDao.update(rItem);
    }
}

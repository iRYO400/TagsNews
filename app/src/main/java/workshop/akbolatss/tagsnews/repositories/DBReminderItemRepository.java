package workshop.akbolatss.tagsnews.repositories;


import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import workshop.akbolatss.tagsnews.repositories.source.ReminderItem;
import workshop.akbolatss.tagsnews.repositories.source.ReminderItemDao;

public class DBReminderItemRepository implements ReminderItemRepository {

    private DaoSession mDaoSession;

    public DBReminderItemRepository(DaoSession daoSession) {
        this.mDaoSession = daoSession;
    }

    @Override
    public Observable<List<ReminderItem>> onLoadReminders() {
        return Observable.fromCallable(new Callable<List<ReminderItem>>() {
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

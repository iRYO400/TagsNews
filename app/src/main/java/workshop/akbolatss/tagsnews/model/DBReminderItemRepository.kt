package workshop.akbolatss.tagsnews.model


import io.reactivex.Single
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.model.dao.ReminderItem

class DBReminderItemRepository(private val mDaoSession: DaoSession) : ReminderItemRepository {

    override fun loadReminders(): Single<List<ReminderItem>> {
        return Single.fromCallable {
            val reminderItemDao = mDaoSession.reminderItemDao
            reminderItemDao.loadAll()
        }
    }

    override fun addReminder(rItem: ReminderItem) {
        val reminderItemDao = mDaoSession.reminderItemDao
        reminderItemDao.insert(rItem)
    }

    override fun removeReminder(rItem: ReminderItem) {
        val reminderItemDao = mDaoSession.reminderItemDao
        reminderItemDao.delete(rItem)
    }

    override fun updateReminder(rItem: ReminderItem) {
        val reminderItemDao = mDaoSession.reminderItemDao
        reminderItemDao.update(rItem)
    }

    override fun activateReminder(rItem: ReminderItem) {
        val reminderItemDao = mDaoSession.reminderItemDao
        rItem.isActive = true
        reminderItemDao.update(rItem)
    }

    override fun deactivateReminder(rItem: ReminderItem) {
        val reminderItemDao = mDaoSession.reminderItemDao
        rItem.isActive = false
        reminderItemDao.update(rItem)
    }
}

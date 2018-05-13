package workshop.akbolatss.tagsnews.model


import io.reactivex.Single
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.model.dao.ReminderItem

/**
 * DBReminderItemRepository is the class that contains all methods related to work with #Reminder and DataBase.
 * Repository pattern.
 * @see ReminderItem
 */
class DBReminderItemRepository(private val mDaoSession: DaoSession) : ReminderItemRepository {

    /**
     * Load list of all reminders
     */
    override fun loadReminders(): Single<List<ReminderItem>> {
        return Single.fromCallable {
            val reminderItemDao = mDaoSession.reminderItemDao
            reminderItemDao.loadAll()
        }
    }

    /**
     * Add new reminder to DB
     */
    override fun addReminder(rItem: ReminderItem) {
        val reminderItemDao = mDaoSession.reminderItemDao
        reminderItemDao.insert(rItem)
    }

    /**
     * Remove existing reminder
     */
    override fun removeReminder(rItem: ReminderItem) {
        val reminderItemDao = mDaoSession.reminderItemDao
        reminderItemDao.delete(rItem)
    }

    /**
     * Update values to existing reminder
     */
    override fun updateReminder(rItem: ReminderItem) {
        val reminderItemDao = mDaoSession.reminderItemDao
        reminderItemDao.update(rItem)
    }

    /**
     * Set active selected reminder
     */
    override fun activateReminder(rItem: ReminderItem) {
        val reminderItemDao = mDaoSession.reminderItemDao
        rItem.isActive = true
        reminderItemDao.update(rItem)
    }

    /**
     * Set inactive reminder
     */
    override fun deactivateReminder(rItem: ReminderItem) {
        val reminderItemDao = mDaoSession.reminderItemDao
        rItem.isActive = false
        reminderItemDao.update(rItem)
    }
}

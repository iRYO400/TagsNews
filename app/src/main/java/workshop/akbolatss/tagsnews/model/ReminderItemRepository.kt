package workshop.akbolatss.tagsnews.model


import io.reactivex.Single
import workshop.akbolatss.tagsnews.model.dao.ReminderItem

/**
 * @see DBReminderItemRepository
 */
interface ReminderItemRepository {

    fun loadReminders(): Single<List<ReminderItem>>

    fun addReminder(rItem: ReminderItem)

    fun removeReminder(rItem: ReminderItem)

    fun updateReminder(rItem: ReminderItem)

    fun activateReminder(rItem: ReminderItem)

    fun deactivateReminder(rItem: ReminderItem)
}

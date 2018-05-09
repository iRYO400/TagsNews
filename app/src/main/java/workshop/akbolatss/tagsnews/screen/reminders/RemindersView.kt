package workshop.akbolatss.tagsnews.screen.reminders

import workshop.akbolatss.tagsnews.base.BaseView
import workshop.akbolatss.tagsnews.model.dao.ReminderItem

/**
 * MVP View for #RemindersView
 * @see RemindersActivity
 */
interface RemindersView : BaseView {

    fun onNoContent(isEmpty: Boolean)

    fun onAddReminder()

    fun onShowReminders(reminderItems: List<ReminderItem>)
}

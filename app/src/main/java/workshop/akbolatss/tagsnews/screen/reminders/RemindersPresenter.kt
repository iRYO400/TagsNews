package workshop.akbolatss.tagsnews.screen.reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.DBReminderItemRepository
import workshop.akbolatss.tagsnews.model.dao.ReminderItem
import workshop.akbolatss.tagsnews.util.Constants.INTENT_REQUEST_CODE
import workshop.akbolatss.tagsnews.util.UtilityMethods
import java.util.*
import javax.inject.Inject

/**
 * MVP Presenter for #RemindersActivity
 * @see RemindersActivity
 */
class RemindersPresenter @Inject constructor() : BasePresenter<RemindersView>() {

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mRepository: DBReminderItemRepository

    /**
     * Activate Reminder. Uses AlarmManager to trigger BroadcastReceiver and execute JobScheduler.
     * #RequestCode uses as ID to turn On/Off AlarmManager
     * @see ReminderReceiver
     * @see UtilityMethods.scheduleJob
     * @see UtilityMethods.scheduleOffJob
     */
    fun onActivateNotification(rItem: ReminderItem, isNewAdded: Boolean) {
        if (!isNewAdded) {
            mRepository.activateReminder(rItem)
        }

        val amc = mContext.getSystemService(ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(mContext, ReminderReceiver::class.java)
        myIntent.putExtra(INTENT_REQUEST_CODE, rItem.requestCode)
        val pendingIntent = PendingIntent.getBroadcast(mContext, rItem.requestCode!!, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, rItem.hour!!)
        calendar.set(Calendar.MINUTE, rItem.minute!!)
        amc.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 1000 * 60 * 60 * 24, pendingIntent)
    }

    /**
     * Deactivate activated reminder using #RequestCode
     */
    fun onDeactivateNotification(rItem: ReminderItem) {
        mRepository.deactivateReminder(rItem)

        UtilityMethods.scheduleOffJob(mContext, rItem.requestCode)//Immediately cancel from JobScheduler

        val myIntent = Intent(mContext, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(mContext, rItem.requestCode!!, myIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        if (pendingIntent != null) {
            val amc = mContext.getSystemService(ALARM_SERVICE) as AlarmManager
            amc.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    /**
     * Add new reminder to DB
     */
    fun onAddReminder(rItem: ReminderItem) {
        onActivateNotification(rItem, true)
        mRepository.addReminder(rItem)
    }

    /**
     * Update existing reminder in DB
     */
    fun onUpdate(rItem: ReminderItem) {
        mRepository.updateReminder(rItem)
    }

    /**
     *  Remove existing reminder from DB
     */
    fun onRemoveReminder(rItem: ReminderItem) {
        onDeactivateNotification(rItem)
        mRepository.removeReminder(rItem)
    }

    /**
     * Load list of all reminder
     */
    fun onLoadReminders() {
        mRepository.loadReminders()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableSingleObserver<List<ReminderItem>>() {
                    override fun onSuccess(reminderItems: List<ReminderItem>) {
                        view.onShowReminders(reminderItems)
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }
}

package workshop.akbolatss.tagsnews.screen.splash

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import com.orhanobut.hawk.Hawk

import java.util.Calendar

import javax.inject.Inject

import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.DBReminderItemRepository
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.ReminderItem
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.screen.reminders.ReminderReceiver
import workshop.akbolatss.tagsnews.util.Constants

import android.content.Context.ALARM_SERVICE
import workshop.akbolatss.tagsnews.util.Constants.INTENT_REQUEST_CODE

class SplashPresenter @Inject
constructor() : BasePresenter<SplashView>() {

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mRepository: DBRssSourceRepository

    @Inject
    lateinit var mReminderRepository: DBReminderItemRepository

    private val isFirstInit: Boolean
        get() {
            if (Hawk.get(Constants.FIRST_START, false)) {
                return true
            } else {
                Hawk.put(Constants.FIRST_START, true)
                return false
            }
        }

    fun onInitBaseSources() {

        if (!isFirstInit) {
            val rssSource = RssSource()
            rssSource.isActive = true
            rssSource.title = "DTF.ru"
            rssSource.link = "https://dtf.ru/rss/all"
            rssSource.description = "DTF — игры, разработка, монетизация, продвижение"
            rssSource.positionIndex = 0
            mRepository.initDefaultSource(rssSource)

            var rItem = ReminderItem()
            rItem.isActive = true
            rItem.hour = 14
            rItem.minute = 46
            rItem.pM_AM = "AM"
            rItem.requestCode = -100
            mReminderRepository.addReminder(rItem)

            onActivateNotification(rItem)

            rItem = ReminderItem()
            rItem.isActive = false
            rItem.hour = 18
            rItem.minute = 0
            rItem.pM_AM = "PM"
            rItem.requestCode = -200
            mReminderRepository.addReminder(rItem)
        }
    }

    private fun onActivateNotification(rItem: ReminderItem) {
        val amc = mContext.getSystemService(ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(mContext, ReminderReceiver::class.java)
        myIntent.putExtra(INTENT_REQUEST_CODE, rItem.requestCode)
        val pendingIntent = PendingIntent.getBroadcast(mContext, rItem.requestCode!!, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, rItem.hour!!)
        calendar.set(Calendar.MINUTE, rItem.minute!!)
        amc.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}

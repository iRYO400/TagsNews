package workshop.akbolatss.tagsnews.screen.splash

import android.content.Context
import com.orhanobut.hawk.Hawk
import io.reactivex.Single
import workshop.akbolatss.tagsnews.base.BasePresenter
import workshop.akbolatss.tagsnews.model.DBReminderItemRepository
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.ReminderItem
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.util.Constants
import javax.inject.Inject

/**
 * MVP Presenter for #SplashActivity
 * @see SplashActivity
 */
class SplashPresenter @Inject
constructor() : BasePresenter<SplashView>() {

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mRepository: DBRssSourceRepository

    @Inject
    lateinit var mReminderRepository: DBReminderItemRepository

    val isFirstInit: Boolean
        get() {
            return if (Hawk.get(Constants.FIRST_START, false)) {
                true
            } else {
                Hawk.put(Constants.FIRST_START, true)
                false
            }
        }

    fun onInitBaseSources(): Single<Boolean> {
        return Single.fromCallable {
            var rssSource = RssSource()
            rssSource.isActive = true
            rssSource.title = "DTF.ru"
            rssSource.link = "https://dtf.ru/rss/all"
            rssSource.description = "DTF — игры, разработка, монетизация, продвижение"
            rssSource.positionIndex = 0
            mRepository.addNewSource(rssSource)

            rssSource = RssSource()
            rssSource.isActive = true
            rssSource.title = "IGN"
            rssSource.link = "http://feeds.ign.com/ign/games-all"
            rssSource.description = "IGN GAMES"
            rssSource.positionIndex = 1
            mRepository.addNewSource(rssSource)

            rssSource = RssSource()
            rssSource.isActive = true
            rssSource.title = "Канобу"
            rssSource.link = "http://kanobu.ru/rss/best/"
            rssSource.description = "Канобу"
            rssSource.positionIndex = 2
            mRepository.addNewSource(rssSource)

            var rItem = ReminderItem()
            rItem.isActive = true
            rItem.hour = 14
            rItem.minute = 46
            rItem.requestCode = -100
            mReminderRepository.addReminder(rItem)

            onActivateNotification(rItem)

            rItem = ReminderItem()
            rItem.isActive = false
            rItem.hour = 18
            rItem.minute = 0
            rItem.requestCode = -200
            mReminderRepository.addReminder(rItem)
            return@fromCallable true
        }
    }

    private fun onActivateNotification(rItem: ReminderItem) {
//        val amc = mContext.getSystemService(ALARM_SERVICE) as AlarmManager
//        val myIntent = Intent(mContext, ReminderReceiver::class.java)
//        myIntent.putExtra(INTENT_REQUEST_CODE, rItem.requestCode)
//        val pendingIntent = PendingIntent.getBroadcast(mContext, rItem.requestCode!!, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = System.currentTimeMillis()
//        calendar.set(Calendar.HOUR_OF_DAY, rItem.hour!!)
//        calendar.set(Calendar.MINUTE, rItem.minute!!)
//        amc.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}

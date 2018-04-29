package workshop.akbolatss.tagsnews.screen.reminders

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import me.toptas.rssconverter.RssFeed
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.application.App
import workshop.akbolatss.tagsnews.di.component.DaggerRemindersComponent
import workshop.akbolatss.tagsnews.di.module.RemindersModule
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.RssSource

import workshop.akbolatss.tagsnews.util.Constants.INTENT_HOUR
import workshop.akbolatss.tagsnews.util.Constants.INTENT_MINUTE
import workshop.akbolatss.tagsnews.util.Constants.INTENT_REQUEST_CODE
import workshop.akbolatss.tagsnews.util.Constants.NOTIFICATION_CHANNEL_DESCRIPTION
import workshop.akbolatss.tagsnews.util.Constants.NOTIFICATION_CHANNEL_ID
import workshop.akbolatss.tagsnews.util.Constants.NOTIFICATION_CHANNEL_NAME
import java.util.*

class ReminderService : JobService() {

    @Inject
    lateinit var mNewsApiService: NewsApiService

    @Inject
    lateinit var mRepository: DBRssSourceRepository

    @Inject
    lateinit var mContext: Context

    private var mCompositeDisposable: CompositeDisposable? = null

    private var inboxStyle: NotificationCompat.InboxStyle? = null

    //Buffer Data
//    private var mRepeatRequestCode: Int = 0
//    private var mRepeatHour: Int = 0
//    private var mRepeatMinute: Int = 0

    override fun onCreate() {
        super.onCreate()
        DaggerRemindersComponent.builder()
                .appComponent((application as App).appComponent)
                .remindersModule(RemindersModule())
                .build()
                .inject(this)

        mCompositeDisposable = CompositeDisposable()

        inboxStyle = NotificationCompat.InboxStyle()
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
//        mRepeatRequestCode = intent.getIntExtra(INTENT_REQUEST_CODE, -1)
//        mRepeatHour = intent.getIntExtra(INTENT_HOUR, -1)
//        mRepeatMinute = intent.getIntExtra(INTENT_MINUTE, -1)

        mRepository.onlyActive
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : DisposableSingleObserver<List<RssSource>>() {
                    override fun onSuccess(rssSources: List<RssSource>) {
                        for (i in rssSources.indices) {
                            val rssSource = rssSources[i]
                            mCompositeDisposable!!.add(mNewsApiService.getRss(rssSource.link)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({ rssFeed: RssFeed ->
                                        pushNotification(rssSource, rssFeed)

                                        onStopJob(params)
                                    }, {
//                                        onRepeatNotification()
                                        onStopJob(params)
                                    }))
                        }

                    }

                    override fun onError(e: Throwable) {
//                        onRepeatNotification()
                    }
                })
        return true
    }


    private fun pushNotification(rssSource: RssSource, rssFeed: RssFeed) {

        val text = rssSource.title + " - " + rssFeed.items[0].title
        inboxStyle!!.addLine(text)

        val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setAutoCancel(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(icon)
                .setStyle(inboxStyle)
                .setColor(Color.GREEN)
                .setLights(Color.GREEN, 1000, 2000)
                .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.ic_launcher)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            channel.description = NOTIFICATION_CHANNEL_DESCRIPTION
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.GREEN
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationManager.createNotificationChannel(channel)
        }

        val random = Random()
        val m = random.nextInt(9999 - 1000) + 1000
        notificationManager.notify(m, notificationBuilder.build())
    }

//    private fun onRepeatNotification() {
//        val amc = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val myIntent = Intent(mContext, ReminderReceiver::class.java)
//
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = System.currentTimeMillis()
//
//        val buffMinute = mRepeatMinute + 30
//        if (buffMinute >= 60) {
//            mRepeatHour++
//            mRepeatMinute = buffMinute - 60
//            if (mRepeatHour >= 24) {
//                mRepeatHour = 0
//            }
//        } else {
//            mRepeatMinute = buffMinute
//        }
//
//        calendar.set(Calendar.HOUR_OF_DAY, mRepeatHour)
//        calendar.set(Calendar.MINUTE, mRepeatMinute)
//
//        myIntent.putExtra(INTENT_REQUEST_CODE, mRepeatRequestCode)
//        myIntent.putExtra(INTENT_HOUR, mRepeatHour)
//        myIntent.putExtra(INTENT_MINUTE, mRepeatMinute)
//        val pendingIntent = PendingIntent.getBroadcast(mContext, mRepeatRequestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        amc.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//    }

    override fun onDestroy() {
        mCompositeDisposable!!.clear()
        super.onDestroy()
    }
}

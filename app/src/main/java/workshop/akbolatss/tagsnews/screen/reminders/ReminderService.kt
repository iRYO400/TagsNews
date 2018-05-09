package workshop.akbolatss.tagsnews.screen.reminders

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.toptas.rssconverter.RssFeed
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.application.App
import workshop.akbolatss.tagsnews.di.component.DaggerRemindersComponent
import workshop.akbolatss.tagsnews.di.module.RemindersModule
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.screen.splash.SplashActivity

import workshop.akbolatss.tagsnews.util.Constants.NOTIFICATION_CHANNEL_DESCRIPTION
import workshop.akbolatss.tagsnews.util.Constants.NOTIFICATION_CHANNEL_ID
import workshop.akbolatss.tagsnews.util.Constants.NOTIFICATION_CHANNEL_NAME
import workshop.akbolatss.tagsnews.util.Constants.NOTIFICATION_ID
import workshop.akbolatss.tagsnews.util.Logger
import java.util.*

class ReminderService : JobService() {

    @Inject
    lateinit var mNewsApiService: NewsApiService

    @Inject
    lateinit var mRepository: DBRssSourceRepository

    @Inject
    lateinit var mContext: Context

    private var mCompositeDisposable: CompositeDisposable? = null


    override fun onCreate() {
        super.onCreate()
        DaggerRemindersComponent.builder()
                .appComponent((application as App).appComponent)
                .remindersModule(RemindersModule())
                .build()
                .inject(this)

        mCompositeDisposable = CompositeDisposable()


    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Logger.i("TAG", " Job Scheduler stopped")
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
//        mRepeatRequestCode = intent.getIntExtra(INTENT_REQUEST_CODE, -1)
//        mRepeatHour = intent.getIntExtra(INTENT_HOUR, -1)
//        mRepeatMinute = intent.getIntExtra(INTENT_MINUTE, -1)
        Logger.i("TAG", " Job Scheduler started")

        var counter = 0
        mRepository.onlyActive
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ rssSources ->
                    counter = rssSources.size
                    val inboxStyle = NotificationCompat.InboxStyle()

                    for (i in rssSources.indices) {
                        val rssSource = rssSources[i]
                        mCompositeDisposable!!.add(mNewsApiService.getRss(rssSource.link)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe({ rssFeed: RssFeed ->
                                    counter++
                                    if (rssFeed.items.isNotEmpty()) {
                                        val text = rssSource.title + " - " + rssFeed.items[0].title
                                        inboxStyle.addLine(text)
                                        pushNotification(rssSource, rssFeed, inboxStyle)
                                    }
                                    if (counter >= rssSources.size)
                                        onStopJob(params)
                                }, {
                                    onStopJob(params)
                                }))
                    }
                }, {
                })
        return true
    }


    private fun pushNotification(rssSource: RssSource, rssFeed: RssFeed, inboxStyle: NotificationCompat.InboxStyle) {



        val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

        val intent = Intent(mContext, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setAutoCancel(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(icon)
                .setContentIntent(pendingIntent)
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

//        val random = Random()
//        val m = random.nextInt(9999 - 1000) + 1000
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    override fun onDestroy() {
        mCompositeDisposable!!.clear()
        super.onDestroy()
    }
}

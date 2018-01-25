package workshop.akbolatss.tagsnews.screen.reminders;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import me.toptas.rssconverter.RssFeed;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.repositories.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.screen.splash.SplashActivity;

import static workshop.akbolatss.tagsnews.util.Constants.INTENT_HOUR;
import static workshop.akbolatss.tagsnews.util.Constants.INTENT_MINUTE;
import static workshop.akbolatss.tagsnews.util.Constants.INTENT_REQUEST_CODE;

public class ReminderService extends Service {

    public static final String TAG = "TAG";
    public static final int NOTIFICATION_ID = 99999;
    public static final String NOTIFICATION_CHANNEL_ID = "workshop.akbolatss.tagsnews.channel";
    public static final String NOTIFICATION_CHANNEL_NAME = "Tags News";
    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Description of Tags News";

    @Inject
    protected NewsApiService mNewsApiService;

    @Inject
    protected DBRssSourceRepository mRepository;

    @Inject
    protected Context mContext;

    private CompositeDisposable mCompositeDisposable;

    private NotificationCompat.Builder builder;
    private NotificationCompat.BigTextStyle bigTextStyle;
    private NotificationCompat.InboxStyle inboxStyle;
    private String bigText;

    //Buffer Data
    private int mRepeateRequestCode;
    private int mRepeatHour;
    private int mRepeatMinute;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((App) getApplicationContext()).getAppComponent().inject(this);

        mCompositeDisposable = new CompositeDisposable();

        builder = new NotificationCompat.Builder(mContext, getResources().getString(R.string.app_name));
        bigTextStyle = new NotificationCompat.BigTextStyle();
        inboxStyle = new NotificationCompat.InboxStyle();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bigText = "";
        mRepeateRequestCode = intent.getIntExtra(INTENT_REQUEST_CODE, -1);
        mRepeatHour = intent.getIntExtra(INTENT_HOUR, -1);
        mRepeatMinute = intent.getIntExtra(INTENT_MINUTE, -1);

        mRepository.getOnlyActive()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<List<RssSource>>() {
                    @Override
                    public void onSuccess(List<RssSource> rssSources) {
                        for (int i = 0; i < rssSources.size(); i++) {
                            final RssSource rssSource = rssSources.get(i);
                            mCompositeDisposable.add(mNewsApiService.getRss(rssSource.getLink())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribeWith(new DisposableSingleObserver<RssFeed>() {
                                        @Override
                                        public void onSuccess(RssFeed rssFeed) {
                                            String text = rssSource.getTitle() + " - " + rssFeed.getItems().get(0).getTitle();
                                            inboxStyle.addLine(text);

                                            builder.setSmallIcon(R.drawable.ic_icon)
                                                    .setContentTitle(getString(R.string.notification_title))
                                                    .setContentText(getString(R.string.notification_text))
                                                    .setAutoCancel(true)
                                                    .setWhen(System.currentTimeMillis())
                                                    .setStyle(inboxStyle)
                                                    .setChannelId(NOTIFICATION_CHANNEL_ID)
                                                    .setContentIntent(PendingIntent.getActivity(mContext, 0,
                                                            new Intent(mContext, SplashActivity.class), 0))
                                                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);

                                            NotificationManager notificationManager = (NotificationManager)
                                                    mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                                            if (Build.VERSION.SDK_INT >= 26) {
                                                if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
                                                    int importance = NotificationManager.IMPORTANCE_LOW;

                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);

                                                    // Configure the notification channel.
                                                    mChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);

                                                    mChannel.enableLights(true);
                                                    // Sets the notification light color for notifications posted to this
                                                    // channel, if the device supports this feature.
                                                    mChannel.setLightColor(Color.GREEN);

                                                    mChannel.enableVibration(true);
                                                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                                                    notificationManager.createNotificationChannel(mChannel);
                                                }
                                            }

                                            notificationManager.notify(NOTIFICATION_ID, builder.build());
                                        }

                                        @Override
                                        public void onError(Throwable e) {
//                                            Log.d(TAG, "onError 2 " + e.getLocalizedMessage());
                                            onRepeatNotification();
                                        }
                                    }));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.d(TAG, "onError 1 " + e.getLocalizedMessage());
                        onRepeatNotification();
                    }
                });

        return START_NOT_STICKY;
    }

    private void onRepeatNotification() {
        AlarmManager amc = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(mContext, ReminderReceiver.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int buffMinute = mRepeatMinute + 30;
        if (buffMinute >= 60) {
            mRepeatHour++;
            mRepeatMinute = buffMinute - 60;
            if (mRepeatHour >= 24) {
                mRepeatHour = 0;
            }
        } else {
            mRepeatMinute = buffMinute;
        }

        calendar.set(Calendar.HOUR_OF_DAY, mRepeatHour);
        calendar.set(Calendar.MINUTE, mRepeatMinute);

        myIntent.putExtra(INTENT_REQUEST_CODE, mRepeateRequestCode);
        myIntent.putExtra(INTENT_HOUR, mRepeatHour);
        myIntent.putExtra(INTENT_MINUTE, mRepeatMinute);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, mRepeateRequestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        amc.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        mContext.stopService(new Intent(mContext, ReminderService.class));
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }
}

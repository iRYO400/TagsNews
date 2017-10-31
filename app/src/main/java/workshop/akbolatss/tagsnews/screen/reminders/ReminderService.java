package workshop.akbolatss.tagsnews.screen.reminders;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import me.toptas.rssconverter.RssFeed;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.repositories.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.screen.board.BoardActivity;
import workshop.akbolatss.tagsnews.screen.splash.SplashActivity;

public class ReminderService extends Service {

    public static final String TAG = "TAG";
    public static final String SEND_NOTIFICATION = "workshop.akbolatss.tagsnews.screen.reminders.sent_noti";
    public static final String NOTIFICATIONS_DATA = "workshop.akbolatss.tagsnews.screen.reminders.data";

    @Inject
    public NewsApiService mNewsApiService;

    @Inject
    protected DBRssSourceRepository mRepository;

    private Context mContext;
    private NotificationCompat.Builder builder;
    private NotificationCompat.InboxStyle inboxStyle;
    private NotificationCompat.BigTextStyle bigTextStyle;
    private String bigText;
    private int mCount;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ((App) getApplicationContext()).getAppComponent().inject(this);
        builder = new NotificationCompat.Builder(mContext);
        inboxStyle = new NotificationCompat.InboxStyle();
        bigTextStyle = new NotificationCompat.BigTextStyle();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCount = 0;
        bigText = "";
        mRepository.getOnlyActive()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<RssSource>, ObservableSource<RssSource>>() {
                    @Override
                    public ObservableSource<RssSource> apply(@NonNull List<RssSource> rssSources) throws Exception {
                        return Observable.fromIterable(rssSources);
                    }
                })
                .subscribe(new Observer<RssSource>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull final RssSource rssSource) {
                        mNewsApiService.getRss(rssSource.getLink())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe( new DisposableSingleObserver<RssFeed>() {
                                    @Override
                                    public void onSuccess(@NonNull RssFeed rssFeed) {
                                        mCount++;
                                        if (mCount <= 4) {
                                        Log.d(TAG, "onSuccess: " + mCount);
                                            String text = rssSource.getTitle() + " - " + rssFeed.getItems().get(0).getTitle();

                                            bigText = text + "\n" + bigText;
                                            bigTextStyle.bigText(bigText);
                                            builder.setAutoCancel(true)
                                                    .setDefaults(Notification.DEFAULT_ALL)
                                                    .setWhen(System.currentTimeMillis())
                                                    .setSmallIcon(R.mipmap.ic_main)
                                                    .setContentTitle(getString(R.string.notification_title))
                                                    .setContentText(getString(R.string.notification_text))
                                                    .setStyle(bigTextStyle)
                                                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);

                                            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                                                    new Intent(mContext, SplashActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                                            builder.setContentIntent(contentIntent);
                                            NotificationManager notificationManager = (NotificationManager)
                                                    mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.notify(1, builder.build());

                                        } else {
                                            mContext.stopService(new Intent(mContext, ReminderService.class));
                                        }
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
//                                        mContext.stopService(new Intent(mContext, ReminderService.class));
                                    }
                                });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
//                        mContext.stopService(new Intent(mContext, ReminderService.class));
                    }

                    @Override
                    public void onComplete() {
                        mContext.stopService(new Intent(mContext, ReminderService.class));
                    }
                });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

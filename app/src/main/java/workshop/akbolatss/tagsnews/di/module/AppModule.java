package workshop.akbolatss.tagsnews.di.module;

import android.content.Context;
import android.support.annotation.NonNull;

import org.greenrobot.greendao.database.Database;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.api.XmlOrJsonConverterFactory;
import workshop.akbolatss.tagsnews.model.DBOpenHelper;
import workshop.akbolatss.tagsnews.model.dao.DaoMaster;
import workshop.akbolatss.tagsnews.model.dao.DaoSession;

import static workshop.akbolatss.tagsnews.util.Constants.DB_NAME;

@Module
public class AppModule {

    private Context mContext;
    private String mBaseUrl;

    public AppModule(Context mContext, String mBaseUrl) {
        this.mContext = mContext;
        this.mBaseUrl = mBaseUrl;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return mContext;
    }

    @Singleton
    @Provides
    DaoSession provideDaoSession(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context, DB_NAME);
        Database db = helper.getWritableDb();
        return new DaoMaster(db).newSession();
    }

    @Singleton
    @Provides
    NewsApiService provideNewsApiService(@NonNull Retrofit retrofit) {
        return retrofit.create(NewsApiService.class);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttp() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(new XmlOrJsonConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }
}

package workshop.akbolatss.tagsnews.di.module;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.toptas.rssconverter.RssConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import workshop.akbolatss.tagsnews.api.NewsApiService;

/**
 * Created by AkbolatSS on 08.08.2017.
 */
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
    NewsApiService provideNewsApiService(@NonNull Retrofit retrofit) {
        return retrofit.create(NewsApiService.class);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttp() {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
//                .addInterceptor(logging)
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(RssConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }
}

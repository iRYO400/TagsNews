package workshop.akbolatss.tagsnews.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import workshop.akbolatss.tagsnews.BuildConfig
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.api.XmlOrJsonConverterFactory
import workshop.akbolatss.tagsnews.model.DBOpenHelper
import workshop.akbolatss.tagsnews.model.dao.DaoMaster
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.util.Constants.DB_NAME
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Main Dagger2 module for AppComponent
 * @see workshop.akbolatss.tagsnews.di.component.AppComponent
 */
@Module
class AppModule(private val mContext: Context, private val mBaseUrl: String) {

    /**
     * Context provider
     */
    @Singleton
    @Provides
    internal fun provideContext(): Context {
        return mContext
    }

    /**
     * DaoSession provider for GreenData Data Base
     * @see DaoSession
     */
    @Singleton
    @Provides
    internal fun provideDaoSession(context: Context): DaoSession {
        val helper = DBOpenHelper(context, DB_NAME)
        val db = helper.writableDb
        return DaoMaster(db).newSession()
    }

    /**
     * API provider
     * @see NewsApiService
     */
    @Singleton
    @Provides
    internal fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    /**
     * OkHttpClient provider for Retrofit2
     * @see okhttp3.OkHttpClient
     */
    @Singleton
    @Provides
    internal fun provideOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(10, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
//            builder.addInterceptor(logging)
        }
        return builder.build()
    }

    /**
     * Retrofit2 provider for http calls
     */
    @Singleton
    @Provides
    internal fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(XmlOrJsonConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }
}

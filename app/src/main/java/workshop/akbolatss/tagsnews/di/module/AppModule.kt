package workshop.akbolatss.tagsnews.di.module

import android.content.Context

import org.greenrobot.greendao.database.Database

import java.util.concurrent.TimeUnit

import javax.inject.Singleton

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

@Module
class AppModule(private val mContext: Context, private val mBaseUrl: String) {

    @Singleton
    @Provides
    internal fun provideContext(): Context {
        return mContext
    }

    @Singleton
    @Provides
    internal fun provideDaoSession(context: Context): DaoSession {
        val helper = DBOpenHelper(context, DB_NAME)
        val db = helper.writableDb
        return DaoMaster(db).newSession()
    }

    @Singleton
    @Provides
    internal fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(10, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }
        return builder.build()
    }

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

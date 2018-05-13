package workshop.akbolatss.tagsnews.di.component

import android.content.Context

import javax.inject.Singleton

import dagger.Component
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.di.module.AppModule
import workshop.akbolatss.tagsnews.model.dao.DaoSession

/**
 * Main Dagger2 component based on AppModule
 * @see AppModule
 */
@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    fun injectApi(): NewsApiService

    fun injectService(): Context

    fun injectDaoSession(): DaoSession

}

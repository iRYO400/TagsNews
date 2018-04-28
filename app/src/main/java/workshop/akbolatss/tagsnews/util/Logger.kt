package workshop.akbolatss.tagsnews.util

import android.util.Log
import workshop.akbolatss.tagsnews.BuildConfig

/**
 * Author: Akbolat Sadvakassov
 * Date: 06.03.2018
 */
class Logger {
    companion object {
        fun i(name: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.d(name, msg)
            }
        }

        fun i(s: String) {
            if (BuildConfig.DEBUG) {
                Log.d("PaperFeed", s)
            }
        }
    }
}
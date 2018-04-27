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
                Log.i(name, msg)
            }
        }

        fun i(s: String) {
            if (BuildConfig.DEBUG) {
                val maxLogSize = 1000
                for (i in 0..s.length / maxLogSize) {
                    val start = i * maxLogSize
                    var end = (i + 1) * maxLogSize
                    end = if (end > s.length) s.length else end
//                    Log.v("*************** Mine: ", s.substring(start, end))
                }
            }
        }
    }
}
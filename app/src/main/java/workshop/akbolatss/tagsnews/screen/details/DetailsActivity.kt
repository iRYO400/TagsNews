package workshop.akbolatss.tagsnews.screen.details

import android.os.Bundle
import android.os.PersistableBundle
import me.yokeyword.swipebackfragment.SwipeBackActivity
import workshop.akbolatss.tagsnews.R

/**
 * Author: Akbolat Sadvakassov
 * Date: 27.04.2018
 */

public class DetailsActivity : SwipeBackActivity(){

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_details)

    }
}
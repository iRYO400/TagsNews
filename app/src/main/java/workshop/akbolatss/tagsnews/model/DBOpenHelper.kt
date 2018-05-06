package workshop.akbolatss.tagsnews.model

import android.content.Context

import org.greenrobot.greendao.database.Database

import workshop.akbolatss.tagsnews.model.dao.DaoMaster

class DBOpenHelper(context: Context, name: String) : DaoMaster.OpenHelper(context, name) {

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        super.onUpgrade(db, oldVersion, newVersion)
        when (oldVersion) {
            1 -> {
            }

            2 -> {
            }
        }
    }
}

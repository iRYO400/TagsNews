package workshop.akbolatss.tagsnews.model;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import workshop.akbolatss.tagsnews.model.dao.DaoMaster;

/**
 * Author: Akbolat Sadvakassov
 * Date: 28.01.2018
 */

public class DBOpenHelper extends DaoMaster.OpenHelper {

    public DBOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        switch (oldVersion) {
            case 1:

                break;

            case 2:
                break;
        }
    }
}

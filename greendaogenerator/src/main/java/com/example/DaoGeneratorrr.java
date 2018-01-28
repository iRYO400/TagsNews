package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;

public class DaoGeneratorrr {
    public static void main(String[] args) {
        Schema schema = new Schema(2, "workshop.akbolatss.tagsnews.repositories.source"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema, "./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        addRssItem(schema);
        addRssSource(schema);
        addReminders(schema);
    }

    // This is use to describe the colums of your table
    private static void addRssItem(final Schema schema) {
        Entity rssFeedItem = schema.addEntity("RssFeedItem");
        rssFeedItem.addIdProperty().primaryKey().autoincrement();
        rssFeedItem.addStringProperty("title");
        rssFeedItem.addStringProperty("link");
        rssFeedItem.addStringProperty("pubDate");
        rssFeedItem.addStringProperty("image");
        rssFeedItem.addStringProperty("description");
        Property IdProperty = rssFeedItem.addLongProperty("rssSourceId").getProperty();
        rssFeedItem.addToOne(schema.getEntities().get(0), IdProperty);
    }

    private static void addRssSource(final Schema schema) {
        Entity rssSource = schema.addEntity("RssSource");
        rssSource.addIdProperty().primaryKey().autoincrement();
        rssSource.addIntProperty("positionIndex");
        rssSource.addBooleanProperty("isActive");
        rssSource.addStringProperty("title");
        rssSource.addStringProperty("link");
        rssSource.addStringProperty("description");
        rssSource.addStringProperty("website");
        rssSource.addStringProperty("visualUrl");
        rssSource.addIntProperty("subscribers");
    }

    private static void addReminders(final Schema schema) {
        Entity rItem = schema.addEntity("ReminderItem");
        rItem.addIdProperty().primaryKey().autoincrement();
        rItem.addIntProperty("requestCode");
        rItem.addBooleanProperty("isActive");
        rItem.addIntProperty("hour");
        rItem.addIntProperty("minute");
        rItem.addStringProperty("PM_AM");
    }
}

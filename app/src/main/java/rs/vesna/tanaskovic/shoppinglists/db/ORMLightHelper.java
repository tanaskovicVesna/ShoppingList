package rs.vesna.tanaskovic.shoppinglists.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;



import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import rs.vesna.tanaskovic.shoppinglists.model.ShoppingList;
import rs.vesna.tanaskovic.shoppinglists.model.Article;

/**
 * Created by Tanaskovic on 4/25/2017.
 */

public class ORMLightHelper extends OrmLiteSqliteOpenHelper{ private static final String DATABASE_NAME    = "db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<Article, Integer> mArticleDao = null;
    private Dao<ShoppingList, Integer> mShoppinglistDao = null;

    public ORMLightHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Article.class);
            TableUtils.createTable(connectionSource, ShoppingList.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Article.class, true);
            TableUtils.dropTable(connectionSource, ShoppingList.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Article, Integer> getArticleDao() throws SQLException {
        if (mArticleDao == null) {
            mArticleDao = getDao(Article.class);
        }

        return mArticleDao;
    }

    public Dao<ShoppingList, Integer> getShoppinglistDao() throws SQLException {
        if (mShoppinglistDao == null) {
            mShoppinglistDao = getDao(ShoppingList.class);
        }

        return mShoppinglistDao;
    }

    @Override
    public void close() {
        mArticleDao = null;
        mShoppinglistDao = null;

        super.close();
    }

}

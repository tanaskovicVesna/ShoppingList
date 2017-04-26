package rs.vesna.tanaskovic.shoppinglists.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Tanaskovic on 4/25/2017.
 */

@DatabaseTable(tableName = ShoppingList.TABLE_NAME_USERS)
public class ShoppingList {
    public static final String TABLE_NAME_USERS = "actor";
    public static final String FIELD_NAME_ID     = "id";
    public static final String TABLE_ARTICLE_NAME = "name";
    public static final String TABLE_ARTICLE_ARTICLES = "movies";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = TABLE_ARTICLE_NAME)
    private String mName;



    @ForeignCollectionField(columnName = ShoppingList.TABLE_ARTICLE_ARTICLES, eager = true)
    private ForeignCollection<Article> articles;

    public ShoppingList() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public ForeignCollection<Article> getArticle() {
        return articles;
    }

    public void setArticle(ForeignCollection<Article> articles) {
        this.articles= articles;
    }



    @Override
    public String toString() {
        return mName;
    }

}

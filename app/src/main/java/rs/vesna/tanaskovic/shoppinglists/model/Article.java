package rs.vesna.tanaskovic.shoppinglists.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Tanaskovic on 4/25/2017.
 */

@DatabaseTable(tableName = Article.TABLE_NAME_USERS)
public class Article {

    public static final String TABLE_NAME_USERS = "article";
    public static final String FIELD_NAME_ID     = "id";
    public static final String TABLE_ARTICLE_NAME = "name";
    public static final String FIELD_NAME_AMOUNT = "amount";
    public static final String FIELD_NAME_USER  = "user";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = TABLE_ARTICLE_NAME)
    private String mName;

    @DatabaseField(columnName = FIELD_NAME_AMOUNT)
    private String mAmount;

    @DatabaseField(columnName = FIELD_NAME_USER, foreign = true, foreignAutoRefresh = true)
    private ShoppingList mUser;

    public Article() {
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

    public ShoppingList getmUser() {
        return mUser;
    }

    public void setmUser(ShoppingList mUser) {
        this.mUser = mUser;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    @Override
    public String toString() {
        return mName;
    }
}

package rs.vesna.tanaskovic.shoppinglists.activities;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rs.vesna.tanaskovic.shoppinglists.R;
import rs.vesna.tanaskovic.shoppinglists.db.ORMLightHelper;
import rs.vesna.tanaskovic.shoppinglists.model.ShoppingList;
import rs.vesna.tanaskovic.shoppinglists.model.Article;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;


import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import static rs.vesna.tanaskovic.shoppinglists.activities.ListActivity.NOTIF_TOAST;
import static rs.vesna.tanaskovic.shoppinglists.activities.ListActivity.NOTIF_STATUS;

public class DetailActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private SharedPreferences prefs;
    private ShoppingList a;
    private TextView nameShoppingListText;
    private EditText nameShoppingList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(ListActivity.SHOPPINGLIST_KEY);

        try {

           a = getDatabaseHelper().getShoppinglistDao().queryForId(key);
           nameShoppingListText = (TextView) findViewById(R.id.shopping_list_name);
           nameShoppingListText.setText(a.getmName());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.shoppinglist_articles);

        try {
            List<Article> list = getDatabaseHelper().getArticleDao().queryBuilder()
                    .where()
                    .eq(Article.FIELD_NAME_USER, a.getmId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Article m = (Article) listView.getItemAtPosition(position);
                    Toast.makeText(DetailActivity.this, m.getmName()+" "+m.getmAmount(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.shoppinglist_articles);

        if (listview != null){
            ArrayAdapter<Article> adapter = (ArrayAdapter<Article>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Article> list = getDatabaseHelper().getArticleDao().queryBuilder()
                            .where()
                            .eq(Article.FIELD_NAME_USER, a.getmId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_notification);
        mBuilder.setContentTitle("Shopping Lists App");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);
       
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void showMessage(String message){
      
        boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
        boolean status = prefs.getBoolean(NOTIF_STATUS, false);

        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status){
            showStatusMesage(message);
        }
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_article:
              
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.layout_add_article);

                Button add = (Button) dialog.findViewById(R.id.button_add_article);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.article_name);

                        EditText amount = (EditText) dialog.findViewById(R.id.article_amount);

                        Article m = new Article();
                        m.setmName(name.getText().toString());

                        m.setmAmount(amount.getText().toString());
                        m.setmUser(a);

                        try {
                            getDatabaseHelper().getArticleDao().create(m);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        refresh();

                        showMessage("New article added to shopping list");

                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;


            case R.id.edit_shopping_list:

                final Dialog dialogEdit = new Dialog(this);
                dialogEdit.setContentView(R.layout.layout_edit);

                Button edit = (Button) dialogEdit.findViewById(R.id.edit_button);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        nameShoppingList = (EditText) dialogEdit.findViewById(R.id.shopping_list_name);

                        a.setmName(nameShoppingList.getText().toString());


                        try {

                            getDatabaseHelper().getShoppinglistDao().update(a);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        showMessage("Shopping list detail updated");
                        dialogEdit.dismiss();
                    }
                });

                dialogEdit.show();


                break;


            case R.id.remove_shopping_list:
                try {
                    getDatabaseHelper().getShoppinglistDao().delete(a);

                    showMessage("Shopping list deleted");

                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return databaseHelper;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}

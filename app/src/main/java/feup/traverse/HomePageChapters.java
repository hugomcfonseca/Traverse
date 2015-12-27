package feup.traverse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author hugof
 * @date 27/12/2015.
 */
public class HomePageChapters extends AppCompatActivity {


    private DataBaseAdapter dataBaseAdapter;

    private CustomDrawer drawer;
    private ImageView[] im_chapters = new ImageView[8];
    private LinearLayout[] ll_chapters = new LinearLayout[8];
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private Session session;

    int[] status;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_chapters);

        session = new Session(this.getBaseContext());
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.homepage_chapters_drawerlayout),
                (NavigationView) findViewById(R.id.homepage_chapters_nav_view), toolbar);

        //Initiate imageViews components
        im_chapters[0] = (ImageView)findViewById(R.id.im_status_chapter1);
        im_chapters[1] = (ImageView)findViewById(R.id.im_status_chapter2);
        im_chapters[2] = (ImageView)findViewById(R.id.im_status_chapter3);
        im_chapters[3] = (ImageView)findViewById(R.id.im_status_chapter4);
        im_chapters[4] = (ImageView)findViewById(R.id.im_status_chapter5);
        im_chapters[5] = (ImageView)findViewById(R.id.im_status_chapter6);
        im_chapters[6] = (ImageView)findViewById(R.id.im_status_chapter7);
        im_chapters[7] = (ImageView)findViewById(R.id.im_status_chapter8);
        ll_chapters[0] = (LinearLayout)findViewById(R.id.linearLayout_chapter1);
        ll_chapters[1] = (LinearLayout)findViewById(R.id.linearLayout_chapter2);
        ll_chapters[2] = (LinearLayout)findViewById(R.id.linearLayout_chapter3);
        ll_chapters[3] = (LinearLayout)findViewById(R.id.linearLayout_chapter4);
        ll_chapters[4] = (LinearLayout)findViewById(R.id.linearLayout_chapter5);
        ll_chapters[5] = (LinearLayout)findViewById(R.id.linearLayout_chapter6);
        ll_chapters[6] = (LinearLayout)findViewById(R.id.linearLayout_chapter7);
        ll_chapters[7] = (LinearLayout)findViewById(R.id.linearLayout_chapter8);

        setUIContents();

        for (int i = 0; i < 2; i++){
            if (status[i] == 0 ){
                ll_chapters[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(HomePageChapters.this, "Clicked.",Toast.LENGTH_LONG).show();
                    }
                });
            }
            else ;
        }

    }

    private void setUIContents (){
        Cursor cursor = dataBaseAdapter.getChaptersInfo(session.getusername());
        String[] local = new String[cursor.getCount()];
        int i = 0;

        status = new int[cursor.getCount()];

        do {
            local[i] = cursor.getString(cursor.getColumnIndex("local"));
            status[i] = cursor.getInt(cursor.getColumnIndex("locked"));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                if (status[i] == 1) {
                    im_chapters[i].setImageDrawable(getResources().getDrawable(R.drawable.locked, getTheme()));
                    ll_chapters[i].setClickable(false);
                } else {
                    im_chapters[i].setImageDrawable(getResources().getDrawable(R.drawable.unlocked, getTheme()));
                    ll_chapters[i].setClickable(true);
                }
            } else {
                if (status[i] == 1) {
                    im_chapters[i].setImageDrawable(getResources().getDrawable(R.drawable.locked));
                    ll_chapters[i].setClickable(false);
                } else {
                    im_chapters[i].setImageDrawable(getResources().getDrawable(R.drawable.unlocked));
                    ll_chapters[i].setClickable(true);
                }
            }


             i++;
        } while (cursor.moveToNext());

        cursor.close();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawer.toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawer.toggle.onConfigurationChanged(newConfig);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        dataBaseAdapter.close();
    }

    @Override
    public void onBackPressed() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View view = getLayoutInflater().inflate(R.layout.logout_form, null);

                alertDialogBuilder = new AlertDialog.Builder(HomePageChapters.this);
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.deleteUsername();

                                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();

                            }
                        });

                alertDialogBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }
}

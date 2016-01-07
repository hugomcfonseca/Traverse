package feup.traverse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * @author hugof
 * @date 27/12/2015.
 */
public class HomePageChapters extends AppCompatActivity {


    private DataBaseAdapter dataBaseAdapter;

    private CustomDrawer drawer;
    private ImageView[] im_chapters = new ImageView[8];
    private LinearLayout[] ll_chapters = new LinearLayout[8];
    private TextView[] tv_placeChapters = new TextView[8];

    private Session session;

    int[] status;
    private Typeface regularF;
    private Typeface boldF;

    CheckBox cb_audio_chapter1,cb_audio_chapter2,checkBox, checkBox2,checkBox3,checkBox4,	checkBox5,checkBox6;


    TextView tv_place_chapter1,tv_place_chapter2,tv_place_chapter3,tv_place_chapter4,tv_place_chapter5,tv_place_chapter6,tv_place_chapter7,tv_place_chapter8;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_chapters);

        regularF = Typeface.createFromAsset(getAssets(),
                "fonts/qsR.otf");
        boldF = Typeface.createFromAsset(getAssets(),
                "fonts/qsB.otf");

        session = new Session(this.getBaseContext());
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.homepage_chapters_drawerlayout),
                (NavigationView) findViewById(R.id.homepage_chapters_nav_view), toolbar);


        //checkbox
        cb_audio_chapter1=(CheckBox)findViewById(R.id.cb_audio_chapter1);
        cb_audio_chapter1.setTypeface(regularF);
        cb_audio_chapter2=(CheckBox)findViewById(R.id.cb_audio_chapter2);
        cb_audio_chapter2.setTypeface(regularF);
        checkBox=(CheckBox)findViewById(R.id.checkBox);
        checkBox.setTypeface(regularF);
        checkBox2=(CheckBox)findViewById(R.id.checkBox2);
        checkBox2.setTypeface(regularF);
        checkBox3=(CheckBox)findViewById(R.id.checkBox3);
        checkBox3.setTypeface(regularF);
        checkBox4=(CheckBox)findViewById(R.id.checkBox4);
        checkBox4.setTypeface(regularF);
        checkBox5=(CheckBox)findViewById(R.id.checkBox5);
        checkBox5.setTypeface(regularF);
        checkBox6=(CheckBox)findViewById(R.id.checkBox6);
        checkBox6.setTypeface(regularF);

        //
        tv_place_chapter1 = (TextView)findViewById(R.id.tv_place_chapter1);
        tv_place_chapter1.setTypeface(regularF);
        tv_place_chapter2 = (TextView)findViewById(R.id.tv_place_chapter2);
        tv_place_chapter2.setTypeface(regularF);
        tv_place_chapter3 = (TextView)findViewById(R.id.tv_place_chapter3);
        tv_place_chapter3.setTypeface(regularF);
        tv_place_chapter4 = (TextView)findViewById(R.id.tv_place_chapter4);
        tv_place_chapter4.setTypeface(regularF);
        tv_place_chapter5 = (TextView)findViewById(R.id.tv_place_chapter5);
        tv_place_chapter5.setTypeface(regularF);
        tv_place_chapter6 = (TextView)findViewById(R.id.tv_place_chapter6);
        tv_place_chapter6.setTypeface(regularF);
        tv_place_chapter7 = (TextView)findViewById(R.id.tv_place_chapter7);
        tv_place_chapter7.setTypeface(regularF);
        tv_place_chapter8 = (TextView)findViewById(R.id.tv_place_chapter8);
        tv_place_chapter8.setTypeface(regularF);

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
        tv_placeChapters[0] = (TextView)findViewById(R.id.tv_place_chapter1);
        tv_placeChapters[1] = (TextView)findViewById(R.id.tv_place_chapter2);
        tv_placeChapters[2] = (TextView)findViewById(R.id.tv_place_chapter3);
        tv_placeChapters[3] = (TextView)findViewById(R.id.tv_place_chapter4);
        tv_placeChapters[4] = (TextView)findViewById(R.id.tv_place_chapter5);
        tv_placeChapters[5] = (TextView)findViewById(R.id.tv_place_chapter6);
        tv_placeChapters[6] = (TextView)findViewById(R.id.tv_place_chapter7);
        tv_placeChapters[7] = (TextView)findViewById(R.id.tv_place_chapter8);

        setUIContents();

        for (int i = 0; i < 8; i++){
            if (status[i] == 0 ){
                final int chapter_passed = i+1;
                final int finalI = i;
                ll_chapters[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ll_chapters[finalI].setBackground(getResources().getDrawable(R.drawable.rect_round_corner_selected));
                        Intent nextStep = new Intent("feup.traverse.ViewChapter");
                        nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        nextStep.putExtra("chapter_selected",chapter_passed);
                        startActivity(nextStep);
                        finish();
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
            tv_placeChapters[i].setText(local[i]);

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

        if (drawer.layout.isDrawerOpen(GravityCompat.START)) {
            drawer.layout.closeDrawer(GravityCompat.START);
            return;
        } else {
            Intent i = new Intent(this, HomePageGeneral.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

        super.onBackPressed();

    }
}

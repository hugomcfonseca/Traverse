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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private Session session;

    int[] status;
    private Typeface regularF;
    private Typeface boldF;

    TextView textView4,textView5,textView6,textView7,textView8,textView9;
    TextView textView10,textView11,textView12,textView13,textView14,textView16,textView17,textView18,textView19,textView20,
            textView21,textView23,textView24,textView25,textView26,textView27,textView29,textView30,textView31,
            textView32,textView33,textView34,textView36,textView37,textView38,textView39,textView40,textView41,
            textView43,textView44,textView45,textView46,textView47,textView48,textView50,textView51,textView52,
            textView53,textView54,textView55,textView57,textView58;

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

        //Text
        textView4 = (TextView)findViewById(R.id.textView4);
        textView4.setTypeface(boldF);
        textView5 = (TextView)findViewById(R.id.textView5);
        textView5.setTypeface(boldF);
        textView6 = (TextView)findViewById(R.id.textView6);
        textView6.setTypeface(boldF);
        textView7 = (TextView)findViewById(R.id.textView7);
        textView7.setTypeface(regularF);
        textView8 = (TextView)findViewById(R.id.textView8);
        textView8.setTypeface(boldF);
        textView9 = (TextView)findViewById(R.id.textView9);
        textView9.setTypeface(boldF);
        textView10 = (TextView)findViewById(R.id.textView10);
        textView10.setTypeface(boldF);


        textView11 = (TextView)findViewById(R.id.textView11);
        textView11.setTypeface(boldF);
        textView12 = (TextView)findViewById(R.id.textView12);
        textView12.setTypeface(boldF);
        textView13 = (TextView)findViewById(R.id.textView13);
        textView13.setTypeface(regularF);
        textView14 = (TextView)findViewById(R.id.textView14);
        textView14.setTypeface(boldF);
        textView16 = (TextView)findViewById(R.id.textView16);
        textView16.setTypeface(boldF);
        textView17 = (TextView)findViewById(R.id.textView17);
        textView17.setTypeface(boldF);

        textView18 = (TextView)findViewById(R.id.textView18);
        textView18.setTypeface(boldF);
        textView19= (TextView)findViewById(R.id.textView19);
        textView19.setTypeface(boldF);
        textView20 = (TextView)findViewById(R.id.textView20);
        textView20.setTypeface(regularF);
        textView21 = (TextView)findViewById(R.id.textView21);
        textView21.setTypeface(boldF);
        textView23 = (TextView)findViewById(R.id.textView23);
        textView23.setTypeface(boldF);

        textView24 = (TextView)findViewById(R.id.textView24);
        textView24.setTypeface(boldF);
        textView25 = (TextView)findViewById(R.id.textView25);
        textView25.setTypeface(boldF);
        textView26 = (TextView)findViewById(R.id.textView26);
        textView26.setTypeface(regularF);
        textView27 = (TextView)findViewById(R.id.textView27);
        textView27.setTypeface(boldF);
        textView29 = (TextView)findViewById(R.id.textView29);
        textView29.setTypeface(boldF);
        textView30 = (TextView)findViewById(R.id.textView30);
        textView30.setTypeface(boldF);

        textView30 = (TextView)findViewById(R.id.textView30);
        textView30.setTypeface(boldF);
        textView31 = (TextView)findViewById(R.id.textView31);
        textView31.setTypeface(boldF);
        textView32 = (TextView)findViewById(R.id.textView32);
        textView32.setTypeface(boldF);
        textView33 = (TextView)findViewById(R.id.textView33);
        textView33.setTypeface(regularF);
        textView34 = (TextView)findViewById(R.id.textView34);
        textView34.setTypeface(boldF);
        textView36 = (TextView)findViewById(R.id.textView36);
        textView36.setTypeface(boldF);
        textView37 = (TextView)findViewById(R.id.textView37);
        textView37.setTypeface(boldF);

        textView38 = (TextView)findViewById(R.id.textView38);
        textView38.setTypeface(boldF);
        textView39 = (TextView)findViewById(R.id.textView39);
        textView39.setTypeface(boldF);
        textView40 = (TextView)findViewById(R.id.textView40);
        textView40.setTypeface(regularF);
        textView41 = (TextView)findViewById(R.id.textView41);
        textView41.setTypeface(boldF);
        textView43 = (TextView)findViewById(R.id.textView43);
        textView43.setTypeface(boldF);
        textView44 = (TextView)findViewById(R.id.textView44);
        textView44.setTypeface(boldF);

        textView45 = (TextView)findViewById(R.id.textView45);
        textView45.setTypeface(boldF);
        textView46 = (TextView)findViewById(R.id.textView46);
        textView46.setTypeface(boldF);
        textView47 = (TextView)findViewById(R.id.textView47);
        textView47.setTypeface(regularF);
        textView48 = (TextView)findViewById(R.id.textView48);
        textView48.setTypeface(boldF);
        textView50 = (TextView)findViewById(R.id.textView50);
        textView50.setTypeface(boldF);
        textView51 = (TextView)findViewById(R.id.textView51);
        textView51.setTypeface(boldF);

        textView52 = (TextView)findViewById(R.id.textView52);
        textView52.setTypeface(boldF);
        textView53 = (TextView)findViewById(R.id.textView53);
        textView53.setTypeface(boldF);
        textView54 = (TextView)findViewById(R.id.textView54);
        textView54.setTypeface(regularF);
        textView55 = (TextView)findViewById(R.id.textView55);
        textView55.setTypeface(boldF);
        textView57 = (TextView)findViewById(R.id.textView57);
        textView57.setTypeface(boldF);
        textView58 = (TextView)findViewById(R.id.textView58);
        textView58.setTypeface(boldF);



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

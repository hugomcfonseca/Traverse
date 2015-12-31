package feup.traverse;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Filipe on 23/12/2015.
 */
public class ViewProfile extends AppCompatActivity {

    private CustomDrawer drawer;

    private TextView tv_Username, tv_Name, tv_birthDate,  tv_Email, tv_typeCharacter, tv_progressValue, tv_userStatus;
    private ProgressBar pb_gameProgress;
    private Button btn_editUserData, btn_seeProgress;
    private CircleImageView img_user, im_userStatus;
    private Session session;//global variable

    DataBaseAdapter dataBaseAdapter;
    DbBitmapUtility bitmapUtility;

    private Typeface regularF;
    private Typeface boldF;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        regularF = Typeface.createFromAsset(getAssets(),
                "fonts/qsR.otf");
        boldF = Typeface.createFromAsset(getAssets(),
                "fonts/qsB.otf");

        session = new Session(this.getBaseContext()); //in oncreate
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        bitmapUtility = new DbBitmapUtility();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.view_profile_drawerlayout),
                (NavigationView) findViewById(R.id.view_profile_nav_view), toolbar);

        //Create Variable
        tv_Name = (TextView)findViewById(R.id.tv_name);
        tv_Name.setTypeface(regularF);
        tv_Username = (TextView)findViewById(R.id.tv_username);
        tv_Username.setTypeface(regularF);
        tv_birthDate = (TextView)findViewById(R.id.tv_birthdate);
        tv_birthDate.setTypeface(regularF);
        tv_Email = (TextView)findViewById(R.id.tv_email);
        tv_Email.setTypeface(regularF);
        tv_typeCharacter = (TextView)findViewById(R.id.tv_type_persona);
        tv_typeCharacter.setTypeface(regularF);
        tv_userStatus = (TextView)findViewById(R.id.tv_user_status);
        tv_userStatus.setTypeface(regularF);
        pb_gameProgress = (ProgressBar)findViewById(R.id.pb_game_progress);
        im_userStatus = (CircleImageView)findViewById(R.id.im_user_status);
        tv_progressValue = (TextView)findViewById(R.id.tv_progress_value);
        tv_progressValue.setTypeface(regularF);
        btn_editUserData = (Button)findViewById(R.id.btn_edit_profile);
        btn_editUserData.setTypeface(boldF);
        btn_seeProgress = (Button)findViewById(R.id.btn_profile_progress);
        btn_seeProgress.setTypeface(boldF);
        img_user =(CircleImageView)findViewById(R.id.profile_image);

        Cursor cursor = dataBaseAdapter.getProfileData(session.getusername());


        if(dataBaseAdapter.verifyImage(session.getusername())== null ){
            Bitmap icon = BitmapFactory.decodeResource(getBaseContext().getResources(),
                    R.drawable.userprofile_logo);
            img_user.setImageBitmap(icon);
        }
        else{
            img_user.setImageBitmap(bitmapUtility.getImage(dataBaseAdapter.verifyImage(session.getusername())));
        }
        tv_Username.setText(session.getusername());
        tv_birthDate.setText(cursor.getString(cursor.getColumnIndex("date")));
        tv_typeCharacter.setText(cursor.getString(cursor.getColumnIndex("persona")));
        tv_Name.setText(cursor.getString(cursor.getColumnIndex("name")));
        tv_Email.setText(cursor.getString(cursor.getColumnIndex("email")));

        updateStatus(cursor);

        pb_gameProgress.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
        tv_progressValue.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex("progress"))));

        btn_editUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextStep = new Intent("feup.traverse.ProfileSettings");
                nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextStep);
                closeThisActivity();
            }
        });

        btn_seeProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextStep = new Intent("feup.traverse.ViewProgress");
                nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextStep);
                closeThisActivity();
            }
        });

    }

    private void updateStatus (Cursor cursor){

        if (cursor.getInt(cursor.getColumnIndex("status")) == 0){

            tv_userStatus.setText("Offline");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                im_userStatus.setImageDrawable(getResources().getDrawable(R.color.status_offline, getTheme()));
            else
                im_userStatus.setImageDrawable(getResources().getDrawable(R.color.status_offline));

        } else if (cursor.getInt(cursor.getColumnIndex("status")) == 1){

            tv_userStatus.setText("Online [on Android]");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                im_userStatus.setImageDrawable(getResources().getDrawable(R.color.status_online_app, getTheme()));
            else
                im_userStatus.setImageDrawable(getResources().getDrawable(R.color.status_online_app));

        } else if (cursor.getInt(cursor.getColumnIndex("status")) == 2) {

            tv_userStatus.setText("Online [on both]");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                im_userStatus.setImageDrawable(getResources().getDrawable(R.color.status_online,getTheme()));
            else
                im_userStatus.setImageDrawable(getResources().getDrawable(R.color.status_online));

        } else ;
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

    public void closeThisActivity(){
        finish();
    }

    @Override
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
            closeThisActivity();
        }

        super.onBackPressed();
    }

}




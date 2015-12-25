package feup.traverse;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Filipe on 23/12/2015.
 */
public class ViewProfile extends AppCompatActivity {

    private CustomDrawer drawer;

    private TextView tv_Username, tv_Name, tv_birthDate,  tv_Email, tv_typeCharacter, tv_progressValue;
    private ProgressBar pb_gameProgress;
    private RadioButton rb_Status;

    private Session session;//global variable

    DataBaseAdapter dataBaseAdapter;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new Session(this.getBaseContext()); //in oncreate
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.signon_drawerlayout),
                (NavigationView) findViewById(R.id.signon_nav_view), toolbar);

        //Create Variable
        tv_Name = (TextView)findViewById(R.id.tv_name);
        tv_Username = (TextView)findViewById(R.id.tv_username);
        tv_birthDate = (TextView)findViewById(R.id.tv_birthdate);
        tv_Email = (TextView)findViewById(R.id.tv_email);
        tv_typeCharacter = (TextView)findViewById(R.id.tv_type_persona);
        pb_gameProgress = (ProgressBar)findViewById(R.id.pb_game_progress);
        rb_Status = (RadioButton)findViewById(R.id.rb_status);
        tv_progressValue = (TextView)findViewById(R.id.tv_progress_value);

        Cursor cursor = dataBaseAdapter.getProfileData(session.getusername());

        //get Username
        tv_Username.setText(session.getusername());
        //get BirthDate
        tv_birthDate.setText(cursor.getString(cursor.getColumnIndex("date")));
        //Persona
        tv_typeCharacter.setText(cursor.getString(cursor.getColumnIndex("persona")));
        //Progress
        tv_Name.setText("Hugo M. Fonseca");
        //Number of hour in Game
        tv_Email.setText(cursor.getString(cursor.getColumnIndex("email")));

        if (cursor.getInt(cursor.getColumnIndex("status")) == 1){
            rb_Status.setText("Busy");
            rb_Status.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#00930C")));
        }

        pb_gameProgress.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
        tv_progressValue.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex("progress"))));

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            Intent i = new Intent("feup.traverse.SetSettings");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            closeThisActivity();
        }*/

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
        }

        super.onBackPressed();
    }

}



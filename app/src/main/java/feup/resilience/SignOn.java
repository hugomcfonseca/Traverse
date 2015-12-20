package feup.resilience;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignOn extends AppCompatActivity {

    FragmentManager manager;
    private CustomDrawer drawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.signon_drawerlayout),
                (NavigationView) findViewById(R.id.signon_nav_view), toolbar);



        manager = getFragmentManager();


        SignOnFragment f1 = new SignOnFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.sign, f1, "SignOn");
        transaction.commit();

    }
    // private CustomDrawer drawer;

    public void addFragSingON(View v) {
        SignOnFragment f1 = new SignOnFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.sign, f1, "SignOn");
        transaction.commit();

    }

    public void addFragQuestion(View v) {
        SignOnFragment f2 = new SignOnFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.sign, f2, "Questionary");
        transaction.commit();
    }

    public void addFragQuestionBut(View v) {

    }

    public void removeFragSingON(View v) {

    }

    public void removeFragQuestion(View v) {

    }

    public void removeFragQuestionBut(View v) {

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
            Intent i = new Intent("feup.resilience.SetSettings");
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
    public void onBackPressed() {

        if (drawer.layout.isDrawerOpen(GravityCompat.START)) {
            drawer.layout.closeDrawer(GravityCompat.START);
            return;
        }

        else {
            Intent i = new Intent(this, MainMenu.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            closeThisActivity();
        }

        super.onBackPressed();
    }

}


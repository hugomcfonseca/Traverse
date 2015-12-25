package feup.traverse;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hugof on 25/12/2015.
 */
public class ChangeProfile extends AppCompatActivity {

    private CustomDrawer drawer;

    private EditText et_Name, et_birthDate,  et_Email, et_Password, et_Password2;
    private CircleImageView iv_settings_photo;
    private Button btn_settingsUpdate;
    private TextView tv_settingsUsername;

    private Session session;//global variable

    DataBaseAdapter dataBaseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        session = new Session(this.getBaseContext()); //in oncreate
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.settings_drawerlayout),
                (NavigationView) findViewById(R.id.settings_nav_view), toolbar);

        //Create Variable
        et_Name = (EditText)findViewById(R.id.et_settings_name);
        et_birthDate = (EditText)findViewById(R.id.et_settings_birthdate);
        et_Email = (EditText)findViewById(R.id.et_settings_email);
        et_Password = (EditText)findViewById(R.id.et_settings_password);
        et_Password2 = (EditText)findViewById(R.id.et_settings_password2);
        tv_settingsUsername = (TextView)findViewById(R.id.tv_settings_username);
        iv_settings_photo = (CircleImageView)findViewById(R.id.iv_settings_photo);
        btn_settingsUpdate = (Button)findViewById(R.id.btn_settings_update);

        Cursor cursor = dataBaseAdapter.getProfileData(session.getusername());

        tv_settingsUsername.setText(session.getusername());
        et_birthDate.setText(cursor.getString(cursor.getColumnIndex("date")));
        et_Name.setText(cursor.getString(cursor.getColumnIndex("name")));
        et_Email.setText(cursor.getString(cursor.getColumnIndex("email")));

        iv_settings_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangeProfile.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_settingsUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateUserData()){
                    Toast.makeText(ChangeProfile.this, "User data updated!", Toast.LENGTH_SHORT).show();

                    Intent nextStep = new Intent("feup.traverse.ViewProfile");
                    nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(nextStep);
                    closeThisActivity();
                }
                else ;
            }
        });


    }

    public boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean updateUserData (){
        boolean isGood = true;

        if (!isEmailValid(et_Email.getText().toString())){
            et_Email.setError("Invalid email address.");
            isGood = false;
        }
        else if (et_Name.getText().toString().length() > 30){
            et_Name.setError("Set a name with a 30 characters at maximum.");
            isGood = false;
        }
        else if (!et_Password.getText().toString().matches(et_Password2.getText().toString())){
            et_Password.setError("Set valid passwords.");
            isGood = false;
        }
        else {
            isGood = true;
            dataBaseAdapter.updateEntry(session.getusername(),et_Name.getText().toString(),
                    et_Email.getText().toString(),et_birthDate.getText().toString(),
                    et_Password.getText().toString());
        }


        return isGood;
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
        } else {
            Intent i = new Intent(this, ViewProfile.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            closeThisActivity();
        }

        super.onBackPressed();
    }
}
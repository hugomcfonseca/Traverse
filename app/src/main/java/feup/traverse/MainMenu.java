package feup.traverse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainMenu extends AppCompatActivity {

    private CustomDrawer drawer;

    private Button btn_SignIn, btn_SignOn;
    private LoginButton loginButton;
    private EditText et_Username, et_Password, et_Email;
    private TextView tv_recoverCredentials;
    private CheckBox cb_saveCredentials;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    DataBaseAdapter dataBaseAdapter;
    private CallbackManager callbackManager;

    SharedPreferences sharedPreferences;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_mainmenu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        callbackManager = CallbackManager.Factory.create();

        // create a instance of SQLite Database
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        session = new Session(this.getBaseContext()); //in oncreate

        drawer = new CustomDrawer( this, (DrawerLayout)findViewById(R.id.mainmenu_drawerlayout),
                (NavigationView)findViewById(R.id.mainmenu_nav_view), toolbar );

        sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        btn_SignIn = (Button) findViewById(R.id.btn_login);
        btn_SignOn = (Button) findViewById(R.id.btn_signon);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        tv_recoverCredentials = (TextView)findViewById(R.id.tv_lostpassword);

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userLogin();
            }
        });

        btn_SignOn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent nextStep = new Intent("feup.traverse.SignOn");
                nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextStep);
                closeThisActivity();

            }
        });

        // TODO: 06/12/2015
        // Test it with Facebook app and verify which message log is generated
        // Substitute it with a transparent login on Facebook?
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainMenu.this, "User ID: " + loginResult.getAccessToken().getUserId(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Facebook", "Entrei");
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainMenu.this, "Login attempt canceled!", Toast.LENGTH_SHORT).show();
                Log.d("Facebook", "Cancelei");
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainMenu.this, "Login attempt failed!", Toast.LENGTH_SHORT).show();
                Log.d("Facebook", "Errei");
            }
        });

        // TODO: 06/12/2015
        // Implement way to send email with password and username
        tv_recoverCredentials.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                recoverCredentials();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
        protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    private void userLogin(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View view = getLayoutInflater().inflate(R.layout.login_form, null);
                et_Username = (EditText) view.findViewById(R.id.et_username);
                et_Password = (EditText) view.findViewById(R.id.et_password);
                cb_saveCredentials = (CheckBox) view.findViewById(R.id.cb_savecredentials);

                if (sharedPreferences != null){
                    et_Username.setText(sharedPreferences.getString("Username",et_Username.getText().toString()));
                    et_Password.setText(sharedPreferences.getString("Password",et_Password.getText().toString()));
                    cb_saveCredentials.setChecked(sharedPreferences.getBoolean("Checked",false));
                }

                alertDialogBuilder = new AlertDialog.Builder(MainMenu.this);
                alertDialogBuilder.setTitle("Login");
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setPositiveButton("Done!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String username = et_Username.getText().toString();
                                String password = et_Password.getText().toString();
                                boolean checkBox = cb_saveCredentials.isEnabled();

                                // fetch the Password form database for respective user name
                                String storedPassword = dataBaseAdapter.getSingleEntry(username);

                                session.setusername(username);

                                // check if the Stored password matches with  Password entered by user
                                if (password.equals(storedPassword)){

                                    if (cb_saveCredentials.isChecked()){
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString("Username", username);
                                        editor.putString("Password", password);
                                        editor.putBoolean("Checked", checkBox);
                                        editor.apply();
                                    }

                                    else {
                                        sharedPreferences.edit().clear().commit();
                                    }

                                    Toast.makeText(MainMenu.this, "Login Successful!",
                                            Toast.LENGTH_LONG).show();
                                    Intent nextStep = new Intent("feup.traverse.ViewProfile");
                                    nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(nextStep);
                                    closeThisActivity();
                                }
                                else {
                                    Toast.makeText(MainMenu.this, "Wrong or inexistent credentials.",
                                            Toast.LENGTH_LONG).show();
                                }
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

    private void recoverCredentials(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View view = getLayoutInflater().inflate(R.layout.recover_form, null);
                et_Email = (EditText)view.findViewById(R.id.et_email);

                alertDialogBuilder = new AlertDialog.Builder(MainMenu.this);
                alertDialogBuilder.setTitle("Recover Credentials");
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setPositiveButton("Send Email!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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

package feup.traverse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

public class MainMenu extends AppCompatActivity {


    private Button btn_SignIn, btn_SignOn;
    private EditText et_Username, et_Password, et_Email;
    private TextView tv_recoverCredentials;
    private CheckBox cb_saveCredentials;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    DataBaseAdapter dataBaseAdapter;

    SharedPreferences sharedPreferences;
    private Session session;

    private String error;

    Typeface regularF;
    Typeface boldF;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        regularF = Typeface.createFromAsset(getAssets(),"fonts/qsR.otf");
        boldF = Typeface.createFromAsset(getAssets(),"fonts/qsB.otf");

        setContentView(R.layout.activity_mainmenu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create a instance of SQLite Database
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        session = new Session(this.getBaseContext()); //in oncreate

        sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        btn_SignIn = (Button) findViewById(R.id.btn_login);
        btn_SignIn.setTypeface(boldF);
        btn_SignOn = (Button) findViewById(R.id.btn_signon);
        btn_SignOn.setTypeface(boldF);

        tv_recoverCredentials = (TextView)findViewById(R.id.tv_lostpassword);
        tv_recoverCredentials.setTypeface(boldF);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userLogin();

                LoginManager.getInstance().logInWithReadPermissions(MainMenu.this, Arrays.asList("public_profile"));
                LoginManager.getInstance().logInWithPublishPermissions(MainMenu.this, Arrays.asList("publish_actions"));
            }
        });

        btn_SignOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent nextStep = new Intent("feup.traverse.SignOn");
                nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextStep);
                closeThisActivity();

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
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                et_Username.setTypeface(regularF);
                et_Password = (EditText) view.findViewById(R.id.et_password);
                et_Password.setTypeface(regularF);
                cb_saveCredentials = (CheckBox) view.findViewById(R.id.cb_savecredentials);
                cb_saveCredentials.setTypeface(regularF);

                if (sharedPreferences != null) {
                    et_Username.setText(sharedPreferences.getString("Username", et_Username.getText().toString()));
                    et_Password.setText(sharedPreferences.getString("Password", et_Password.getText().toString()));
                    cb_saveCredentials.setChecked(sharedPreferences.getBoolean("Checked", false));
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
                                if (password.equals(storedPassword)) {

                                    if (cb_saveCredentials.isChecked()) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString("Username", username);
                                        editor.putString("Password", password);
                                        editor.putBoolean("Checked", checkBox);
                                        editor.apply();
                                    } else {
                                        sharedPreferences.edit().clear().apply();
                                    }

                                    Toast.makeText(MainMenu.this, "Login Successful!",
                                            Toast.LENGTH_LONG).show();
                                    Intent nextStep = new Intent("feup.traverse.HomePageGeneral");
                                    nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(nextStep);
                                    closeThisActivity();
                                } else {
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
                              et_Email = (EditText) view.findViewById(R.id.et_email);
                              et_Email.setTypeface(regularF);
                              if (error == "Empty") {
                                  et_Email.setError("Text is empty");
                              } else if (error == "NULL") {
                                  et_Email.setError("Inexistence email");
                              }

                              alertDialogBuilder = new AlertDialog.Builder(MainMenu.this);
                              alertDialogBuilder.setTitle("Recover Credentials");
                              alertDialogBuilder.setView(view);
                              alertDialogBuilder.setPositiveButton("Send Email!",
                                      new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {
                                              if ((et_Email.getText().toString()).isEmpty()) {
                                                  error = "Empty";
                                                  run();
                                              } else if (dataBaseAdapter.verifyEmail(et_Email.getText().toString()) == "false") {
                                                  error = "NULL";
                                                  run();
                                              } else {
                                                  String username=dataBaseAdapter.verifyEmail(et_Email.getText().toString());
                                                  String password=dataBaseAdapter.getSingleEntry(username);
                                                  //TODo  talk with website

                                              }
                                          }
                                      }

                              );

                              alertDialogBuilder.setNegativeButton("Cancel",
                                      new DialogInterface.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {

                                          }
                                      }

                              );

                              alertDialog = alertDialogBuilder.create();
                              alertDialog.show();
                          }
                      }

        );
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

        super.onBackPressed();
    }
}

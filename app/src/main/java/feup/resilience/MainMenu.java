package feup.resilience;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

public class MainMenu extends AppCompatActivity {

    private CustomDrawer drawer;

    private Button btn_SignIn, btn_SignOn;
    private EditText et_Username, et_Password, et_Email;
    private TextView tv_recoverCredentials;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    DataBaseAdapter dataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create a instance of SQLite Database
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        //teste
        dataBaseAdapter.createUser("hugo","ee11178@fe.up.pt","1234");

        drawer = new CustomDrawer( this, (DrawerLayout)findViewById(R.id.mainmenu_drawerlayout),
                (NavigationView)findViewById(R.id.mainmenu_nav_view), toolbar );

        btn_SignIn = (Button) findViewById(R.id.btn_login);
        btn_SignOn = (Button) findViewById(R.id.btn_signon);

        tv_recoverCredentials = (TextView)findViewById(R.id.tv_lostpassword);

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userLogin();
            }
        });

        btn_SignOn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent nextStep = new Intent("feup.resilience.SignOnActivity");
                nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextStep);
                closeThisActivity();
            }
        });

        tv_recoverCredentials.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                recoverCredentials();
            }
        });


    }

    private void userLogin(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View view = getLayoutInflater().inflate(R.layout.login_form, null);
                et_Username = (EditText) view.findViewById(R.id.et_username);
                et_Password = (EditText) view.findViewById(R.id.et_password);

                alertDialogBuilder = new AlertDialog.Builder(MainMenu.this);
                alertDialogBuilder.setTitle("Login");
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setPositiveButton("Done!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // get The User name and Password
                                String username = et_Username.getText().toString();
                                String password = et_Password.getText().toString();

                                // fetch the Password form database for respective user name
                                String storedPassword = dataBaseAdapter.getSingleEntry(username);

                                // check if the Stored password matches with  Password entered by user
                                if(password.equals(storedPassword)){
                                    Toast.makeText(MainMenu.this, "Login Successfull!",
                                            Toast.LENGTH_LONG).show();
                                    Intent nextStep = new Intent("feup.resilience.MapsActivity");
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
            Intent i = new Intent("feup.resilience.SetSettings");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            closeThisActivity();
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void closeThisActivity(){
        finish();
        System.exit(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        dataBaseAdapter.close();
    }
}

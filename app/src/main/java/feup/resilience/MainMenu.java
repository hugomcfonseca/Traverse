package feup.resilience;

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
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {

    private Button btn_SignIn, btn_SignOn;
    private CustomDrawer drawer;
    private EditText et_Username, et_Password;
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = new CustomDrawer( this, (DrawerLayout)findViewById(R.id.mainmenu_drawerlayout), (NavigationView)findViewById(R.id.mainmenu_nav_view), toolbar );


        btn_SignIn = (Button) findViewById(R.id.btn_signin);
        //btn_SignOn = (Button) findViewById(R.id.btn_signon);
        et_Username = (EditText)findViewById(R.id.et_username);
        et_Password = (EditText)findViewById(R.id.et_password);

        // Set On ClickListener
        btn_SignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get The User name and Password
                String userName=et_Username.getText().toString();
                String password=et_Password.getText().toString();

                loginDataBaseAdapter.updateEntry("hugo","1234");

                // fetch the Password form database for respective user name
                String storedPassword=loginDataBaseAdapter.getSingleEntry(userName);

                // check if the Stored password matches with  Password entered by user
                if(password.equals(storedPassword)) {
                    Toast.makeText(MainMenu.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainMenu.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }
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
            Intent i = new Intent("pt.fraunhofer.SetSettings");
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
        loginDataBaseAdapter.close();
    }
}

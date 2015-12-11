package feup.resilience;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.nfc.Tag;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignOn extends AppCompatActivity {

    private Button btn_nextRegister;
    private EditText et_Username, et_Password, et_Password2, et_dateOfBirth, et_Email;
    private CustomDrawer drawer;

    DataBaseAdapter connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new CustomDrawer( this, (DrawerLayout)findViewById(R.id.signon_drawerlayout),
                (NavigationView)findViewById(R.id.signon_nav_view), toolbar );

        // create a instance of SQLite Database
        connector = new DataBaseAdapter(this);
        connector.open();

        et_Username = (EditText)findViewById(R.id.et_username);
        et_Password = (EditText)findViewById(R.id.et_password);
        et_Password2 = (EditText)findViewById(R.id.et_conf_pass);
        et_Email = (EditText)findViewById(R.id.et_email);
        et_dateOfBirth = (EditText)findViewById(R.id.et_dateofbirth);
        btn_nextRegister = (Button)findViewById(R.id.btn_register);

        et_Email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String email = et_Email.getText().toString();

                if (!isEmailValid(email))
                    et_Email.setError("Invalid email address.");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
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

        });

        btn_nextRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = et_Username.getText().toString();
                String email = et_Email.getText().toString();
                String password = et_Password.getText().toString();
                String password2 = et_Password2.getText().toString();
                String date = et_dateOfBirth.getText().toString();

                if (username.matches("") || password.matches("") || password2.matches("") ||
                        date.matches("") || email.matches("")) {
                    Toast.makeText(SignOn.this, "Please, fill all fields.", Toast.LENGTH_SHORT).show();
                }

                if (connector.verifyUsernameAndEmail(username, email))
                    et_Username.setError("Username or email already in use, insert another please.");

                // TODO: insert condition to validate date
                if (verifyEqualsPasswords(password,password2) &&
                        !connector.verifyUsernameAndEmail(username, email)) {
                    connector.createUser(username,email,date,password);
                    Toast.makeText(SignOn.this,"Account created successfully!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        /*  TODO:
            implement this method exclusively in this class and verify date
            (it must be less than actual date)
        */
        et_dateOfBirth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                    return true;
                }

                return false;
            }
        });
    }

    private boolean verifyEqualsPasswords(String password, String password2){
        if (!password.matches(password2)){
            et_Password2.setError("Passwords doesn't match.");
            return false;
        } else
            return true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        connector.close();
    }

    //Date Fragment
    @SuppressLint("ValidFragment")
    public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        public DateDialog(View view){
            et_dateOfBirth =(EditText)view;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the dialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //show to the selected date in the text box
            String date=day+"-"+(month+1)+"-"+year;
            et_dateOfBirth .setText(date);
        }

    }

}

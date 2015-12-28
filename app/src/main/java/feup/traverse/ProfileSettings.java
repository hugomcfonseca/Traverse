package feup.traverse;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
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
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hugof on 25/12/2015.
 */
public class ProfileSettings extends AppCompatActivity {

    private CustomDrawer drawer;

    private EditText et_Name, et_birthDate,  et_Email, et_Password, et_Password2;
    private CircleImageView iv_settings_photo;
    private Button btn_settingsUpdate;
    private TextView tv_settingsUsername;
    private Bitmap bmp = null;

    private Session session;//global variable

    private static int RESULT_LOAD_IMAGE = 1;

    private Pattern pattern;
    private Matcher matcher;

    private static final String DATE_PATTERN =
            "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    DataBaseAdapter dataBaseAdapter;
    DbBitmapUtility bitmapUtility;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        session = new Session(this.getBaseContext()); //in oncreate

        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        pattern = Pattern.compile(DATE_PATTERN);

        bitmapUtility = new DbBitmapUtility();

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
        btn_settingsUpdate = (Button)findViewById(R.id.btn_settings_update);
        iv_settings_photo =(CircleImageView)findViewById(R.id.iv_settings_photo);

        Cursor cursor = dataBaseAdapter.getProfileData(session.getusername());

        if(dataBaseAdapter.verifyImage(session.getusername())== null ){
            Bitmap icon = BitmapFactory.decodeResource(getBaseContext().getResources(),
                    R.drawable.userprofile_add_logo);
            iv_settings_photo.setImageBitmap(icon);
        }
        else{
            iv_settings_photo.setImageBitmap(bitmapUtility.getImage(dataBaseAdapter.verifyImage(session.getusername())));
        }
        tv_settingsUsername.setText(session.getusername());
        et_birthDate.setText(cursor.getString(cursor.getColumnIndex("date")));
        et_Name.setText(cursor.getString(cursor.getColumnIndex("name")));
        et_Email.setText(cursor.getString(cursor.getColumnIndex("email")));

        iv_settings_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        btn_settingsUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateUserData()){
                    Toast.makeText(ProfileSettings.this, "User data updated!", Toast.LENGTH_SHORT).show();

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            iv_settings_photo.setImageBitmap(bmp);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
        else if (!et_Password.getText().toString().matches(et_Password2.getText().toString())
                || (et_Password.getText().toString().length() < 1 && et_Password2.getText().toString().length() < 1) ){
            et_Password.setError("Set valid passwords.");
            isGood = false;
        }
        else if (!DateValidate(et_birthDate.getText().toString())) {
            et_birthDate.setError("Please, fill in the date in this format: dd/mm/yyyy.");
            isGood = false;
        }
        else {
            isGood = true;
            if(bmp==null){
                dataBaseAdapter.updateEntry(session.getusername(),et_Name.getText().toString(),
                et_Email.getText().toString(),et_birthDate.getText().toString(),
                et_Password.getText().toString(),null);

            }
            else{
                dataBaseAdapter.updateEntry(session.getusername(),et_Name.getText().toString(),
                        et_Email.getText().toString(),et_birthDate.getText().toString(),
                        et_Password.getText().toString(),bitmapUtility.getBytes(bmp));
            }
        }
        return isGood;
    }

    public boolean DateValidate(final String date){

        matcher = pattern.matcher(date);

        if(matcher.matches()){

            matcher.reset();

            if(matcher.find()){

                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month .equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month .equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if(year % 4==0){
                        if(day.equals("30") || day.equals("31")){
                            return false;
                        }else{
                            return true;
                        }
                    }else{
                        if(day.equals("29")||day.equals("30")||day.equals("31")){
                            return false;
                        }else{
                            return true;
                        }
                    }
                }else{
                    return true;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
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
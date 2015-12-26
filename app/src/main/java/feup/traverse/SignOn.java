package feup.traverse;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SignOn extends AppCompatActivity implements Communicator {

    private int myflag = 0;
    int questionResult[] = new int[4];
    FragmentManager manager;
    private CustomDrawer drawer;

    private Session session;//global variable

    DataBaseAdapter dataBaseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.signon_drawerlayout),
                (NavigationView) findViewById(R.id.signon_nav_view), toolbar);

        manager = getFragmentManager();

        session = new Session(this.getBaseContext()); //in oncreate
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        SignOnFragment f1 = new SignOnFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.sign, f1, "SignOn");
        transaction.addToBackStack(null);
        transaction.commit();

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

    public void closeThisActivity() {
        finish();
    }

    @Override
    public void onBackPressed() {

        if (drawer.layout.isDrawerOpen(GravityCompat.START)) {
            drawer.layout.closeDrawer(GravityCompat.START);
            return;
        } else {
            Intent i = new Intent(this, MainMenu.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            closeThisActivity();
        }

        super.onBackPressed();
    }

    @Override
    public void respond(int i, int val) {
        if (i == 0) {
            QuestionaryFragment f2 = new QuestionaryFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f2, "ReplaceQuestion1");
            transaction.addToBackStack("add1");
            transaction.commit();
        }
        else if (i == 1) {
            QuestionaryFragment f3 = new QuestionaryFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f3, "ReplaceQuestion2");
            transaction.addToBackStack(null);
            transaction.commit();
            questionResult[0]=val;
            myflag++;


        }
        else if (i == 2) {
            QuestionaryFragment f4 = new QuestionaryFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f4, "ReplaceQuestion3");
            transaction.addToBackStack("add_3");
            transaction.commit();
            questionResult[1]=val;
            myflag++;
        }
        else if (i == 3) {
            QuestionaryFragment f5 = new QuestionaryFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f5, "ReplaceQuestion4");
            transaction.addToBackStack("add_4");
            transaction.commit();
            questionResult[2]=val;
            myflag++;
        }else{
            questionResult[3]=val;
            dataBaseAdapter.updatePersona(session.getusername(),findPersona());
            Intent nextStep = new Intent(this, MainMenu.class);
            nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(nextStep);
            closeThisActivity();

        }
    }

    private String findPersona() {
        String type="";
        for(int i=0, count1=0,count2=0,count3=0,count4=0;i<4;i++){
            if (questionResult[i]==1){
                count1++;
                if(count1==2){
                    type="type1";
                    break;
                }
            }
            else if (questionResult[i]==2){
                count2++;
                if(count2==2){
                    type="type2";
                    break;
                }
            }
            else if (questionResult[i]==3){
                count3++;
                if(count3==2){
                    type="type3";
                    break;
                }
            }
            else if (questionResult[i]==4){
                count4++;
                if(count4==2){
                    type="type4";
                    break;
                }
            }

        }
        return type;
    }

    public int getMyFlag() {
        return myflag;
    }

    public void Back(View view){
        myflag--;
        if(myflag==3){
            myflag--;
            QuestionaryFragment f4 = new QuestionaryFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f4, "ReplaceQuestion3");
            transaction.addToBackStack("add_3");
            transaction.commit();
            myflag++;
        }
        else if(myflag==2){
            myflag--;
            QuestionaryFragment f4 = new QuestionaryFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f4, "ReplaceQuestion3");
            transaction.addToBackStack("add_3");
            transaction.commit();
            myflag++;
        }
        else if(myflag==1){
            myflag--;
            QuestionaryFragment f3 = new QuestionaryFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f3, "ReplaceQuestion2");
            transaction.addToBackStack(null);
            transaction.commit();
            myflag++;
        }
        else if(myflag==0){
            QuestionaryFragment f2 = new QuestionaryFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f2, "ReplaceQuestion2");
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else ;


    }

}
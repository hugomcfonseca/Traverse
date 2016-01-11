package feup.traverse;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class SignOn extends AppCompatActivity implements Communicator {

    private int myflag = 0;
    int questionResult[] = new int[4];
    FragmentManager manager;

    private Session session;//global variable

    DataBaseAdapter dataBaseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = getFragmentManager();

        session = new Session(this.getBaseContext()); //in oncreate
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        SignOnForm f1 = new SignOnForm();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.sign, f1, "SignOn");
        transaction.addToBackStack(null);
        transaction.commit();

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

        Intent i = new Intent(this, MainMenu.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        closeThisActivity();

        super.onBackPressed();
    }

    @Override
    public void respond(int i, int val) {
        if (i == 0) {
            SignOnQuestionary f2 = new SignOnQuestionary();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f2, "ReplaceQuestion1");
            transaction.addToBackStack("add1");
            transaction.commit();
        }
        else if (i == 1) {
            SignOnQuestionary f3 = new SignOnQuestionary();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f3, "ReplaceQuestion2");
            transaction.addToBackStack(null);
            transaction.commit();
            questionResult[0]=val;
            myflag++;


        }
        else if (i == 2) {
            SignOnQuestionary f4 = new SignOnQuestionary();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f4, "ReplaceQuestion3");
            transaction.addToBackStack("add_3");
            transaction.commit();
            questionResult[1]=val;
            myflag++;
        }
        else if (i == 3) {
            SignOnQuestionary f5 = new SignOnQuestionary();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f5, "ReplaceQuestion4");
            transaction.addToBackStack("add_4");
            transaction.commit();
            questionResult[2]=val;
            myflag++;
        }else{
            questionResult[3]=val;

            dataBaseAdapter.createUser(session.getusername(), session.getname(), session.getemail(), session.getdate(), findPersona(), 1, 0, session.getpassword());
            Toast.makeText(getApplicationContext(), "Account created successfully! You get a persona of "+findPersona(), Toast.LENGTH_SHORT).show();
            ExternalDB db = new ExternalDB();

            try {
                db.insertuser("traverseusers", "INSERT", session.getusername(), session.getname(), session.getemail(), session.getdate(), findPersona(), 1, 0, session.getpassword());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
                    type="Art";
                    break;
                }
            }
            else if (questionResult[i]==2){
                count2++;
                if(count2==2){
                    type="Tourist";
                    break;
                }
            }
            else if (questionResult[i]==3){
                count3++;
                if(count3==2){
                    type="Nature";
                    break;
                }
            }
            else if (questionResult[i]==4){
                count4++;
                if(count4==2){
                    type="Gastronomy";
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
            SignOnQuestionary f4 = new SignOnQuestionary();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f4, "ReplaceQuestion3");
            transaction.addToBackStack("add_3");
            transaction.commit();
            myflag++;
        }
        else if(myflag==2){
            myflag--;
            SignOnQuestionary f4 = new SignOnQuestionary();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f4, "ReplaceQuestion3");
            transaction.addToBackStack("add_3");
            transaction.commit();
            myflag++;
        }
        else if(myflag==1){
            myflag--;
            SignOnQuestionary f3 = new SignOnQuestionary();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f3, "ReplaceQuestion2");
            transaction.addToBackStack(null);
            transaction.commit();
            myflag++;
        }
        else if(myflag==0){
            SignOnQuestionary f2 = new SignOnQuestionary();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.sign, f2, "ReplaceQuestion2");
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else ;


    }

}
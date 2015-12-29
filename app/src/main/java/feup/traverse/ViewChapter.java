package feup.traverse;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * @author hugof
 * @date 29/12/2015.
 */
public class ViewChapter extends AppCompatActivity {

    DataBaseAdapter dataBaseAdapter;
    private Session session;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        session = new Session(this.getBaseContext()); //in oncreate

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ViewChapterTextPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

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

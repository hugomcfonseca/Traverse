package feup.traverse;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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
    public Session session;
    private CustomDrawer drawer;

    public ViewPager mPager;
    public PagerAdapter mPagerAdapter;

    public int flag = 1;
    public int value;
    public int finished = 0;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.view_chapter_drawerlayout),
                (NavigationView) findViewById(R.id.view_chapter_nav_view), toolbar);

        session = new Session(this.getBaseContext()); //in oncreate

        Bundle extras = getIntent().getExtras();
        value = extras.getInt("chapter_selected");

        flag = 1;
        finished = 0;

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ViewChapterTextPagerAdapter(getSupportFragmentManager(), 1);
        mPager.setAdapter(mPagerAdapter);

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

        int id = item.getItemId();

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
        } else if (flag == 2 || flag == 3){
            flag = 1;
            mPagerAdapter = new ViewChapterTextPagerAdapter(getSupportFragmentManager(),1);
            mPager.setAdapter(mPagerAdapter);
            return;
        } else {
            Intent i = new Intent(this, HomePageChapters.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            closeThisActivity();
        }

        super.onBackPressed();
    }


}

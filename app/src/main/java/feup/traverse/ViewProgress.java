package feup.traverse;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * @author hugof
 * @date 28/12/2015.
 */
public class ViewProgress extends AppCompatActivity {

    private CustomDrawer drawer;

    private Typeface regularF;
    private Typeface boldF;

    private TextView tv_progress_actual_chapter,tv_progress_value,tv_progress_phase_id,tv_progress_local_name,tv_progress_score_phase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        regularF = Typeface.createFromAsset(getAssets(),
                "fonts/qsR.otf");
        boldF = Typeface.createFromAsset(getAssets(),
                "fonts/qsB.otf");
        setContentView(R.layout.activity_view_progress);

        tv_progress_actual_chapter=(TextView)findViewById(R.id.tv_progress_actual_chapter);
        tv_progress_actual_chapter.setTypeface(boldF);
        tv_progress_value=(TextView)findViewById(R.id.tv_progress_value);
        tv_progress_value.setTypeface(regularF);
        tv_progress_phase_id=(TextView)findViewById(R.id.tv_progress_phase_id);
        tv_progress_phase_id.setTypeface(boldF);
        tv_progress_local_name=(TextView)findViewById(R.id.tv_progress_local_name);
        tv_progress_local_name.setTypeface(boldF);
        tv_progress_score_phase=(TextView)findViewById(R.id.tv_progress_score_phase);
        tv_progress_score_phase.setTypeface(boldF);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = new CustomDrawer( this, (DrawerLayout)findViewById(R.id.view_progress_drawerlayout), (NavigationView)findViewById(R.id.view_progress_nav_view), toolbar );

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        ListFragment newFragment = new ViewProgressList();
        transaction.add(R.id.viewpager, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (drawer.layout.isDrawerOpen(GravityCompat.START)) {
            drawer.layout.closeDrawer(GravityCompat.START);
            return;
        }
        Intent i = new Intent(this, HomePageGeneral.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        closeThisActivity();
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
            Intent i = new Intent("pt.fraunhofer.WarningsSettings");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            closeThisActivity();
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void closeThisActivity() {
        finish();
    }



}

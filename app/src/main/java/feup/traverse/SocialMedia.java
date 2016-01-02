package feup.traverse;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hugof on 26/12/2015.
 */
public class SocialMedia extends AppCompatActivity {

    private CustomDrawer drawer;
    private ImageView ev_facebook, ev_twitter, ev_site;
    private WebView tv_about;
    private TextView tv_name;
    private String text= "<html><body><h3 align=\"justify\">"
    + "A message from the future tells you that Humanity is in grave danger and that only by destroying the Artifact, an ancient radiation sensor from another world, will we survive. Meet with Dr. Marco Fogg who will send you back in time, before the Artifact was activated, to stop Phileas Fogg from saving the sensor from certain destruction. Chase him during his journey around the world as you explore Porto, Portugal.\n"
    +"Turn on the app, get transported to the past, explore Porto and save the world."
    +"</h3></body></html>";

    private Typeface regularF;
    private Typeface boldF;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        regularF = Typeface.createFromAsset(getAssets(),
                "fonts/qsR.otf");
        boldF = Typeface.createFromAsset(getAssets(),
                "fonts/qsB.otf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.social_drawerlayout),
                (NavigationView) findViewById(R.id.social_nav_view), toolbar);

        ev_facebook= (ImageView)findViewById(R.id.btn_facebook);
        ev_twitter= (ImageView)findViewById(R.id.btn_twitter);
        ev_site= (ImageView)findViewById(R.id.btn_site);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_name.setTypeface(boldF);
        tv_about=(WebView)findViewById(R.id.webView);
        tv_about.loadData(text, "text/html", "utf-8");
        tv_about.setBackgroundColor(0x7AAABBBB);




        ev_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100001285444664"));
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/100001285444664")));
                }
            }
        });
        ev_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("twitter://user?user_id=4615381997"));
                    startActivity(intent);

                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/FilipeDMorais")));
                }
            }
        });
        ev_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://traverse-rangers.herokuapp.com/")));
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

        int id = item.getItemId();

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
        } else {
            Intent i = new Intent(this, HomePageGeneral.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            closeThisActivity();
        }

        super.onBackPressed();
    }

}


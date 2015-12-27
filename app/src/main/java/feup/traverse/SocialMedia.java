package feup.traverse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by hugof on 26/12/2015.
 */
public class SocialMedia extends AppCompatActivity {

    private CustomDrawer drawer;
    private ImageView ev_facebook, ev_twitter, ev_site;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.social_drawerlayout),
                (NavigationView) findViewById(R.id.social_nav_view), toolbar);

        ev_facebook= (ImageView)findViewById(R.id.btn_facebook);
        ev_twitter= (ImageView)findViewById(R.id.btn_twitter);
        ev_site= (ImageView)findViewById(R.id.btn_site);

        ev_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100001285444664"));
                    startActivity(intent);
                } catch(Exception e) {
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


}

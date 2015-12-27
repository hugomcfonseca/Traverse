package feup.traverse;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * @author Hugo Fonseca
 * @date 28/11/2015
 */
public class CustomDrawer implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    DrawerLayout layout;
    ActionBarDrawerToggle toggle;

    NavigationView navView;
    AppCompatActivity parent;

    private final int POSITION_CHAPTERS = 00;
    private final int POSITION_PROFILE  = 01;
    private final int POSITION_PROGRESS = 02;
    private final int POSITION_MAP = 03;
    private final int POSITION_SOCIAL = 04;

    private int checked_pos;
    CustomDrawer (AppCompatActivity nParent, DrawerLayout nLayout, NavigationView nNavView, Toolbar nToolbar) {
        this.parent = nParent;
        this.layout = nLayout;
        this.toolbar = nToolbar;
        this.navView = nNavView;

        int pos=0;

        toggle = new ActionBarDrawerToggle(
                parent,                  /* host Activity */
                layout,         /* DrawerLayout object */
                toolbar,
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        );

        layout.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        if (parent instanceof HomePageChapters )
            pos = POSITION_CHAPTERS;
        else
        if (parent instanceof ViewProfile )
            pos = POSITION_PROFILE;
        else

        if (parent instanceof SocialMedia)
            pos = POSITION_SOCIAL;
        else

            checked_pos = pos;

        navView.getMenu().getItem( (int)(pos/10) ).getSubMenu().getItem(pos % 10).setChecked(true);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        navView.getMenu().getItem( (int)(checked_pos/10) ).getSubMenu().getItem( checked_pos%10 ).setChecked(false);

        int id = item.getItemId();
        int pos = -1;
        Intent i = null;

        switch (id) {
            case R.id.nav_homepage_chapters:
                if( !(parent instanceof HomePageChapters) )
                    i = new Intent(parent, HomePageChapters.class);
                pos = POSITION_CHAPTERS;
                break;

            case R.id.nav_view_profile:
                if( !(parent instanceof ViewProfile) )
                    i = new Intent(parent, ViewProfile.class);
                pos = POSITION_PROFILE;
                break;

            case R.id.nav_progress:
                pos = POSITION_PROGRESS;
                break;

            case R.id.nav_map:
                pos = POSITION_MAP;
                break;

            case R.id.nav_social:
                if( !(parent instanceof SocialMedia) )
                    i = new Intent(parent, SocialMedia.class);
                pos = POSITION_SOCIAL;
                break;
        }


        if(pos!=-1 && i!=null) {
            navView.getMenu().getItem( (int)(pos/10) ).getSubMenu().getItem( pos%10 ).setChecked(true);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            parent.startActivity(i);
            parent.finish();
        }


        checked_pos = pos;
        navView.getMenu().getItem( (int)(pos/10) ).getSubMenu().getItem( pos%10 ).setChecked(true);

        //layout.closeDrawer(GravityCompat.START);
        return true;
    }
}

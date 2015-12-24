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

    private final int POSITION_HOME = 00;
    private final int POSITION_SIGNON = 01;
    private final int POSITION_STARTNEWPHASE = 10;
    private final int POSITION_SIMPLEVIEW = 11;
    private final int POSITION_DETAILEDVIEW = 12;

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

        if (parent instanceof MainMenu )
            pos = POSITION_HOME;
        else
        if (parent instanceof SignOn )
            pos = POSITION_SIGNON;
        else

            checked_pos = pos;
        navView.getMenu().getItem( (int)(pos/10) ).getSubMenu().getItem( pos%10 ).setChecked(true);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        navView.getMenu().getItem( (int)(checked_pos/10) ).getSubMenu().getItem( checked_pos%10 ).setChecked(false);

        int id = item.getItemId();
        int pos = -1;
        Intent i = null;


        switch (id) {
            case R.id.nav_home:
                if( !(parent instanceof MainMenu) )
                    i = new Intent(parent, MainMenu.class);
                pos = POSITION_HOME;
                break;

            case R.id.nav_signon:
                if( !(parent instanceof SignOn) )
                    i = new Intent(parent, SignOn.class);
                pos = POSITION_SIGNON;
                break;

            case R.id.nav_start_newphase:
                pos = POSITION_STARTNEWPHASE;
                break;

            case R.id.nav_simple_progress_view:
                pos = POSITION_SIMPLEVIEW;
                break;

            case R.id.nav_detailed_progress_view:
                pos = POSITION_DETAILEDVIEW;
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

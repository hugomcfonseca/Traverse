package feup.traverse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

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

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private Session session;

    private final int POSITION_HOMEPAGE = 00;
    private final int POSITION_CHAPTERS = 01;
    private final int POSITION_PROGRESS  = 02;
    private final int POSITION_SOCIAL  = 03;
    private final int POSITION_PROFILE  = 04;
    private final int POSITION_LOGOUT = 05;

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

        if (parent instanceof HomePageGeneral )
            pos = POSITION_HOMEPAGE;
        else
        if (parent instanceof HomePageChapters )
            pos = POSITION_CHAPTERS;
        else
        if (parent instanceof ViewProgress )
            pos = POSITION_PROGRESS;
        else
        if (parent instanceof ViewProfile)
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
        final Intent[] i = {null};

        session = new Session(parent.getBaseContext());

        switch (id) {
            case R.id.nav_homepage:
                if( !(parent instanceof HomePageGeneral) )
                    i[0] = new Intent(parent, HomePageGeneral.class);
                pos = POSITION_HOMEPAGE;
                break;

            case R.id.nav_homepage_chapters:
                if( !(parent instanceof HomePageChapters) )
                    i[0] = new Intent(parent, HomePageChapters.class);
                pos = POSITION_CHAPTERS;
                break;

            case R.id.nav_progress:
                if( !(parent instanceof ViewProgress) )
                    i[0] = new Intent(parent, ViewProgress.class);
                pos = POSITION_PROGRESS;
                break;

            case R.id.nav_social:
                if( !(parent instanceof SocialMedia) )
                    i[0] = new Intent(parent, SocialMedia.class);
                pos = POSITION_SOCIAL;
                break;

            case R.id.nav_view_profile:
                if( !(parent instanceof ViewProfile) )
                    i[0] = new Intent(parent, ViewProfile.class);
                pos = POSITION_PROFILE;
                break;

            case R.id.nav_logout:
                if ( !(parent instanceof MainMenu) ) {
                    View view = parent.getLayoutInflater().inflate(R.layout.logout_form, null);

                    alertDialogBuilder = new AlertDialog.Builder(parent);
                    alertDialogBuilder.setView(view);
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    session.deleteUsername();

                                    i[0] = new Intent(parent, MainMenu.class);
                                    parent.startActivity(i[0]);
                                    parent.finish();

                                }
                            });

                    alertDialogBuilder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                pos = POSITION_LOGOUT;
                break;
        }


        if(pos!=-1 && i[0] !=null) {
            navView.getMenu().getItem( (int)(pos/10) ).getSubMenu().getItem(pos % 10).setChecked(true);

            i[0].setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            parent.startActivity(i[0]);
            parent.finish();
        }


        checked_pos = pos;
        navView.getMenu().getItem( (int)(pos/10) ).getSubMenu().getItem( pos%10 ).setChecked(true);

        return true;
    }
}

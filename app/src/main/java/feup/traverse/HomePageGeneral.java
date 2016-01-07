package feup.traverse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * @author hugof
 * @date 30/12/2015.
 */
public class HomePageGeneral extends AppCompatActivity {

    private LinearLayout[] ll_buttons = new LinearLayout[5];
    private TextView tv_homepageUsername;
    private CustomDrawer drawer;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private Session session;

    Typeface regularF;
    Typeface boldF;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        regularF = Typeface.createFromAsset(getAssets(),
                "fonts/qsR.otf");
        boldF = Typeface.createFromAsset(getAssets(),
                "fonts/qsB.otf");
        setContentView(R.layout.activity_homepage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.homepage_drawerlayout),
                (NavigationView) findViewById(R.id.homepage_nav_view), toolbar);

        session = new Session(this);

        ll_buttons[0] = (LinearLayout)findViewById(R.id.ll_homepage_chapters);
        ll_buttons[1] = (LinearLayout)findViewById(R.id.ll_homepage_progress);
        ll_buttons[2] = (LinearLayout)findViewById(R.id.ll_homepage_social);
        ll_buttons[3] = (LinearLayout)findViewById(R.id.ll_homepage_profilesettings);
        ll_buttons[4] = (LinearLayout)findViewById(R.id.ll_homepage_logout);
        tv_homepageUsername = (TextView)findViewById(R.id.tv_homepage_username);

        tv_homepageUsername.setText(session.getusername());
        tv_homepageUsername.setTypeface(boldF);

        ll_buttons[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ll_buttons[0].setBackground(getResources().getDrawable(R.drawable.rect_round_corner_selected));
                Intent nextStep = new Intent("feup.traverse.HomePageChapters");
                nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextStep);
                finish();
            }
        });

        ll_buttons[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ll_buttons[1].setBackground(getResources().getDrawable(R.drawable.rect_round_corner_selected));
                Intent nextStep = new Intent("feup.traverse.ViewProgress");
                nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextStep);
                finish();
            }
        });

        ll_buttons[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ll_buttons[2].setBackground(getResources().getDrawable(R.drawable.rect_round_corner_selected));
                Intent nextStep = new Intent("feup.traverse.SocialMedia");
                nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextStep);
                finish();
            }
        });

        ll_buttons[3].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ll_buttons[3].setBackground(getResources().getDrawable(R.drawable.rect_round_corner_selected));
                Intent nextStep = new Intent("feup.traverse.ViewProfile");
                nextStep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextStep);
                finish();
            }
        });

        ll_buttons[4].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ll_buttons[4].setBackground(getResources().getDrawable(R.drawable.rect_round_corner_selected));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        View view = getLayoutInflater().inflate(R.layout.logout_form, null);

                        alertDialogBuilder = new AlertDialog.Builder(HomePageGeneral.this);
                        alertDialogBuilder.setView(view);
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        session.deleteUsername();

                                        Intent i = new Intent(getApplicationContext(), MainMenu.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();

                                    }
                                });

                        alertDialogBuilder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ll_buttons[4].setBackground(getResources().getDrawable(R.drawable.rect_round_corner_notselected));
                                        return;
                                    }
                                });

                        alertDialog = alertDialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(true);

                        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                ll_buttons[4].setBackground(getResources().getDrawable(R.drawable.rect_round_corner_notselected));
                            }
                        });

                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                ll_buttons[4].setBackground(getResources().getDrawable(R.drawable.rect_round_corner_notselected));
                            }
                        });
                        alertDialog.show();
                    }
                });

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

    @Override
    public void onBackPressed() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View view = getLayoutInflater().inflate(R.layout.logout_form, null);

                alertDialogBuilder = new AlertDialog.Builder(HomePageGeneral.this);
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.deleteUsername();

                                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();

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
        });

    }

}

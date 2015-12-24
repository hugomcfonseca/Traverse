package feup.traverse;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.facebook.CallbackManager;

/**
 * Created by Filipe on 23/12/2015.
 */
public class Profile extends AppCompatActivity {
    private CustomDrawer drawer;

    private TextView et_Username, et_Name,  et_dateOfBirth, et_Persona, et_Status;
    private Session session;//global variable

    DataBaseAdapter dataBaseAdapter;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new Session(this.getBaseContext()); //in oncreate
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.open();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = new CustomDrawer(this, (DrawerLayout) findViewById(R.id.signon_drawerlayout),
                (NavigationView) findViewById(R.id.signon_nav_view), toolbar);

        //Create Variable
        et_Name= (TextView)this.findViewById(R.id.tv_nameP);
        et_Username= (TextView)this.findViewById(R.id.tv_usernameP);
        et_dateOfBirth= (TextView)this.findViewById(R.id.tv_ageP);
        et_Persona= (TextView)this.findViewById(R.id.tv_personaP);


        //get Username
        et_Name.setText( session.getusename());
        //get BirthDate
        et_dateOfBirth.setText("Birth Date: "+dataBaseAdapter.getBirthay(session.getusename()));
        //Persona
        et_Persona.setText("Type Persona:  " );
        //Progress
        //et_Name.setText("Username:  " );
        //Number of hour in Game
    }


    @Override
    public void onStart(){
        super.onStart();
        //init();


    }

    private void init(){

    }

}



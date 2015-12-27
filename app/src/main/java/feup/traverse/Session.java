package feup.traverse;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Filipe on 24/12/2015.
 */
public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusername(String username) {
        prefs.edit().putString("username", username).apply();
    }

    public void putdata(String username, String name, String email, String date,  String password) {
        prefs.edit().putString("username", username).apply();
        prefs.edit().putString("name", name).apply();
        prefs.edit().putString("email", email).apply();
        prefs.edit().putString("date", date).apply();
        prefs.edit().putString("password", password).apply();

    }

    public String getusername() {
        return prefs.getString("username","");
    }
    public String getname() {
        return prefs.getString("name","");
    }
    public String getdate() {
        return prefs.getString("date","");
    }
    public String getemail() {
        return prefs.getString("email","");
    }
    public String getpassword() {
        return prefs.getString("password","");
    }

}

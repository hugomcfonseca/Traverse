package feup.traverse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author hugof
 * @date 28/11/2015.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "traverse.db";
    public static final String TABLE_NAME_USERDATA = "userdata";
    public static final String TABLE_NAME_PLACES = "placesbypersona";
    private static final int DATABASE_VERSION = 1;
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String LOCAL = "local";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String DATE = "date";
    public static final String PASSWORD = "password";
    public static final String STATUS = "status";
    public static final String PERSONA = "persona";
    public static final String PROGRESS = "progress";
    public static final String IMAGE = "image";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String PHASE = "phase";
    public static final String LOCKED = "locked";

    public DataBaseHelper (Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(scriptUserData());
        db.execSQL(scriptPlaces());

        initDatabaseHardcoded(db,2,2,0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERDATA);
        onCreate(db);
    }

    public String scriptUserData (){
        final StringBuilder userData_table = new StringBuilder();

        userData_table.append("create table if not exists "+ TABLE_NAME_USERDATA +" ( ");
        userData_table.append(ID + " integer primary key autoincrement, ");
        userData_table.append(NAME + " varchar(30) not null unique, ");
        userData_table.append(USERNAME + " varchar(20) not null unique, ");
        userData_table.append(EMAIL + " varchar(40) not null unique, ");
        userData_table.append(DATE + " varchar(12) not null, ");
        userData_table.append(PERSONA + " varchar(15) not null, ");
        userData_table.append(STATUS + " integer not null, ");
        userData_table.append(PROGRESS + " integer not null, ");
        userData_table.append(PASSWORD + " varchar(20) not null, ");
        userData_table.append(IMAGE + " blob");
        userData_table.append(");");

        return userData_table.toString();
    }

    //ToDo: insert places hardcoded
    public String scriptPlaces (){
        final StringBuilder maps_table = new StringBuilder();

        maps_table.append("create table if not exists " + TABLE_NAME_PLACES + " ( ");
        maps_table.append(ID + " integer primary key autoincrement, ");
        maps_table.append(PERSONA + " varchar(15) not null, ");
        maps_table.append(LOCAL + " varchar(30) not null unique, ");
        maps_table.append(LATITUDE + " double not null, ");
        maps_table.append(LONGITUDE + " double not null, ");
        maps_table.append(LOCKED + " integer not null, ");
        maps_table.append(PHASE + " integer not null ");
        maps_table.append(");");

        return maps_table.toString();
    }

    //ToDo: insert each story part by phase
    public String scriptChapters (){
        final StringBuilder maps_table = new StringBuilder();

        maps_table.append("create table if not exists " + TABLE_NAME_PLACES + " ( ");
        maps_table.append(ID + " integer primary key autoincrement, ");
        maps_table.append(PERSONA + " varchar(15) not null, ");
        maps_table.append(LOCAL + " varchar(30) not null unique, ");
        maps_table.append(LATITUDE + " double not null unique, ");
        maps_table.append(LONGITUDE + " double not null unique ");
        maps_table.append(");");

        return maps_table.toString();
    }

    public void initDatabaseHardcoded (SQLiteDatabase db, int n_userData, int n_places, int n_chapters) {
        String[] array = new String[n_userData + n_places + n_chapters];

        if (n_userData > 0) {
            array[0] = "INSERT or replace INTO " + TABLE_NAME_USERDATA + " ( " + ID + ", " + NAME + ", " + USERNAME + ", " + EMAIL + ", " + DATE + ", "
                    + PERSONA + " ," + STATUS + ", " + PROGRESS + ", " + PASSWORD + " )" +
                    " VALUES(1,'Hugo M. Fonseca','hugo','hugofonseca93@hotmail.com','11/04/1993','Music',1,0,'1234')";
            db.execSQL(array[0]);
            array[1] = "INSERT or replace INTO " + TABLE_NAME_USERDATA + " ( " + ID + ", " + NAME + ", " + USERNAME + ", " + EMAIL + ", " + DATE + ", "
                    + PERSONA + " ," + STATUS + ", " + PROGRESS + ", " + PASSWORD + " )" +
                    " VALUES(2,'morais','Filipe D. Morais','filipe_morais@outlook.com','23/07/1993','Arts',1,25,'1234')";
            db.execSQL(array[1]);
        }
        if (n_places > 0) {
            array[2] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                    + LOCKED + " ," + PHASE  + " )" +
                    " VALUES(1,'Music','Majestik',41.1597026,-8.6057382,0,1)";
            db.execSQL(array[2]);
            array[3] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                    + LOCKED + " ," + PHASE  + " )" +
                    " VALUES(2,'Music','Maus Hábitos',41.1597026,-8.6057382,1,2)";
            db.execSQL(array[3]);
        }

    }

}
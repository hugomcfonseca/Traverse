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
    public static final String TABLE_NAME = "userdata";
    private static final int DATABASE_VERSION = 1;
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String DATE = "date";
    public static final String PASSWORD = "password";
    public static final String STATUS = "status";
    public static final String PERSONA = "persona";
    public static final String PROGRESS = "progress";
    public static final String IMAGE = "image";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + ID + " integer primary key autoincrement, "
            + NAME + " varchar(30) not null unique, "
            + USERNAME + " varchar(20) not null unique, "
            + EMAIL + " varchar(40) not null unique, "
            + DATE + " varchar(12) not null, "
            + PERSONA + " varchar(15) not null, "
            + STATUS + " integer not null, "
            + PROGRESS + " integer not null, "
            + PASSWORD + " varchar(20) not null, "
            + IMAGE + " blob"
            +");";

    public DataBaseHelper (Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

        /**String sql = "INSERT or replace INTO " + TABLE_NAME + " ( "+ID+", "+NAME+", "+USERNAME+", "+EMAIL+", "+DATE+", "
         +PERSONA+" ,"+STATUS+", "+PROGRESS+", "+PASSWORD+", "+IMAGE+" )" +
         " VALUES(1,'Hugo M. Fonseca','hugo','hugofonseca93@hotmail.com','11/04/1993','Music Person',1,0,'1234')" ;*/

        String sql = "INSERT or replace INTO " + TABLE_NAME + " ( "+ID+", "+NAME+", "+USERNAME+", "+EMAIL+", "+DATE+", "
                +PERSONA+" ,"+STATUS+", "+PROGRESS+", "+PASSWORD+" )" +
                " VALUES(1,'Hugo M. Fonseca','hugo','hugofonseca93@hotmail.com','11/04/1993','Music Person',1,0,'1234')" ;
        db.execSQL(sql);

        String sql1 = "INSERT or replace INTO " + TABLE_NAME + " ( "+ID+", "+NAME+", "+USERNAME+", "+EMAIL+", "+DATE+", "
                +PERSONA+" ,"+STATUS+", "+PROGRESS+", "+PASSWORD+" )" +
                " VALUES(2,'morais','Filipe D. Morais','filipe_morais@outlook.com','23/07/1993','Arts Person',1,25,'1234')" ;
        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
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
    public static final String TABLE_NAME_USERDATA = "user_data";
    public static final String TABLE_NAME_PLACES = "chapters_places";
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
    public static final String SCORE = "score";

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

        initDatabaseHardcoded(db, 2, 0, 0);
        initPlacesByPersona(db);
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
        userData_table.append(NAME + " varchar(30) not null, ");
        userData_table.append(USERNAME + " varchar(20) not null unique, ");
        userData_table.append(EMAIL + " varchar(40) not null unique, ");
        userData_table.append(DATE + " varchar(12) not null, ");
        userData_table.append(PERSONA + " varchar(20) not null, ");
        userData_table.append(STATUS + " integer not null, ");
        userData_table.append(PROGRESS + " integer not null, ");
        userData_table.append(PASSWORD + " varchar(25) not null, ");
        userData_table.append(IMAGE + " blob");
        userData_table.append(");");

        return userData_table.toString();
    }

    //ToDo: insert places hardcoded
    public String scriptPlaces (){
        final StringBuilder chapters_places_table = new StringBuilder();

        chapters_places_table.append("create table if not exists " + TABLE_NAME_PLACES + " ( ");
        chapters_places_table.append(ID + " integer primary key autoincrement, ");
        chapters_places_table.append(PERSONA + " varchar(20) not null, ");
        chapters_places_table.append(LOCAL + " varchar(40) not null, ");
        chapters_places_table.append(LATITUDE + " double not null, ");
        chapters_places_table.append(LONGITUDE + " double not null, ");
        chapters_places_table.append(LOCKED + " integer not null, ");
        chapters_places_table.append(PHASE + " integer not null, ");
        chapters_places_table.append(SCORE + " integer ");
        chapters_places_table.append(");");

        return chapters_places_table.toString();
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
                    " VALUES(1,'Hugo M. Fonseca','hugo','hugofonseca93@hotmail.com','11/04/1993','Tourist',1,0,'1234')";
            db.execSQL(array[0]);
            array[1] = "INSERT or replace INTO " + TABLE_NAME_USERDATA + " ( " + ID + ", " + NAME + ", " + USERNAME + ", " + EMAIL + ", " + DATE + ", "
                    + PERSONA + " ," + STATUS + ", " + PROGRESS + ", " + PASSWORD + " )" +
                    " VALUES(2,'Filipe D. Morais','morais','filipe_morais@outlook.com','23/07/1993','Art',1,25,'1234')";
            db.execSQL(array[1]);
        }

    }

    public void initPlacesByPersona (SQLiteDatabase db){

        String[] places_art = new String[8];

        places_art[0] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(1,'Art','Serralves Foundation',41.159891,-8.659889,0,1,0)";
        places_art[1] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(2,'Art','World of Discoveries',41.143346,-8.620973,1,2,0)";
        places_art[2] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(3,'Art','National Museum Soares dos Reis',41.1476799,-8.6215614,1,3,0)";
        places_art[3] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(4,'Art','Romantic Museum Quinta da Macieirinha',41.147585,-8.621568,1,4,0)";
        places_art[4] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(5,'Art','Military Museum of Porto',41.145976,-8.595297,1,5,0)";
        places_art[5] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(6,'Art','ArtDistrict - Miguel Bombarda',41.149643,-8.620279,1,6,0)";
        places_art[6] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(7,'Art','Museum of Natural History',41.146891,-8.615648,1,7,0)";
        places_art[7] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(8,'Art','House of Arts',41.156470,-8.643426,1,8,0)";

        String[] places_tourist = new String[8];

        places_tourist[0] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(9,'Tourist','Dom Luís I Bridge',41.140043,-8.609444,0,1,0)";
        places_tourist[1] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(10,'Tourist','Clérigos Church',41.145715,-8.614720,1,2,0)";
        places_tourist[2] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(11,'Tourist','São Bento Railway Station',41.145512,-8.610718,1,3,0)";
        places_tourist[3] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(12,'Tourist','Crystal Palace',41.148248,-8.625494,1,4,0)";
        places_tourist[4] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(13,'Tourist','Stock Exchange Palace',41.141613,-8.614871,1,5,0)";
        places_tourist[5] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(14,'Tourist','Lello Bookstore',41.146796,-8.614896,1,6,0)";
        places_tourist[6] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(15,'Tourist','Porto Cathedral',41.142964,-8.611303,1,7,0)";
        places_tourist[7] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(16,'Tourist','Church of São Francisco',41.140741,-8.616030,1,8,0)";

        String[] places_gastronomy = new String[8];

        places_gastronomy[0] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(17,'Gastronomy','Maus Hábitos',41.146743,-8.605681,0,1,0)";
        places_gastronomy[1] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(18,'Gastronomy','Âncora d''Ouro Coffee (''O Piolho'')',41.147012,-8.616529,1,2,0)";
        places_gastronomy[2] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(19,'Gastronomy','Rota do Chá',41.149420,-8.622305,1,3,0)";
        places_gastronomy[3] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(20,'Gastronomy','Toscano Restaurant',41.157418,-8.624769,1,4,0)";
        places_gastronomy[4] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(21,'Gastronomy','Dama Pé de Cabra',41.145775,-8.603314,1,5,0)";
        places_gastronomy[5] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(22,'Gastronomy','Port Wine Cellars',41.137398,-8.614819,1,6,0)";
        places_gastronomy[6] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(23,'Gastronomy','Majestic Café',41.147132,-8.606695,1,7,0)";
        places_gastronomy[7] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(24,'Gastronomy','A Brasileira',41.146850,-8.609357,1,8,0)";

        String[] places_nature = new String[8];

        places_nature[0] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(25,'Nature','Parque da Cidade (Porto)',41.170476,-8.686492,0,1,0)";
        places_nature[1] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(26,'Nature','River (Dom Luís I Bridge)',41.140043,-8.609444,1,2,0)";
        places_nature[2] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(27,'Nature','Rota do Chá',41.149420,-8.622305,1,3,0)";
        places_nature[3] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(28,'Nature','Toscano Restaurant',41.157418,-8.624769,1,4,0)";
        places_nature[4] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(29,'Nature','Tait House',41.147948,-8.628159,1,5,0)";
        places_nature[5] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(30,'Nature','Porto Botanical Garden',41.154024,-8.642415,1,6,0)";
        places_nature[6] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(31,'Nature','Serralves Foundation',41.159891,-8.659889,1,7,0)";
        places_nature[7] = "INSERT or replace INTO " + TABLE_NAME_PLACES + " ( " + ID + ", " + PERSONA + ", " + LOCAL + ", " + LATITUDE + ", " + LONGITUDE + ", "
                + LOCKED + " ," + PHASE + " ," + SCORE  + " )" +
                " VALUES(32,'Nature','Crystal Palace',41.148248,-8.625494,1,8,0)";

        for (int i = 0; i < 8; i++){
            db.execSQL(places_art[i]);
            db.execSQL(places_tourist[i]);
            db.execSQL(places_gastronomy[i]);
            db.execSQL(places_nature[i]);
        }

    }

}
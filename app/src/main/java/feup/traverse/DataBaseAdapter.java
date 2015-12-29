package feup.traverse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hugof
 * @date 28/11/2015.
 */

public class DataBaseAdapter {
    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private String[] allColumns_userData = { DataBaseHelper.ID, DataBaseHelper.USERNAME, DataBaseHelper.EMAIL,
            DataBaseHelper.DATE,DataBaseHelper.PERSONA,DataBaseHelper.STATUS,
            DataBaseHelper.PROGRESS, DataBaseHelper.PASSWORD};

    int[] nChapter;
    int[] chapterScore;
    String[] locals;

    public DataBaseAdapter(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public  SQLiteDatabase getDatabaseInstance(){
        return database;
    }

    /** The next methods are about user data table */

    public void createUser (String username,String name, String email, String date,String persona ,
                            int status,int progress,String password) {
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.USERNAME, username);
        values.put(DataBaseHelper.NAME, name);
        values.put(DataBaseHelper.EMAIL, email);
        values.put(DataBaseHelper.DATE, date);
        values.put(DataBaseHelper.PERSONA, persona);
        values.put(DataBaseHelper.STATUS, status);
        values.put(DataBaseHelper.PROGRESS, progress);
        values.put(DataBaseHelper.PASSWORD, password);

        database.insert(DataBaseHelper.TABLE_NAME_USERDATA, null, values);
    }

    public String getSingleEntry(String username) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_USERDATA + " WHERE " +
                DataBaseHelper.USERNAME + " = ?", new String[]{username});

        if(cursor.getCount() < 1) { // UserName Not Exist
            cursor.close();
            return "NOT EXIST";
        }

        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PASSWORD));
        cursor.close();

        return password;
    }

    public boolean verifyUsernameAndEmail(String username, String email) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_USERDATA + " WHERE " +
                DataBaseHelper.USERNAME + " = ?", new String[]{username});

        Cursor cursor2 = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_USERDATA + " WHERE " +
                DataBaseHelper.EMAIL + " = ?", new String[]{email});

        if(cursor.getCount() < 1 && cursor2.getCount() < 1) { // UserName Not Exist
            cursor.close();
            return false;
        }

        return true;
    }

    public String verifyEmail(String email){
        Cursor cursor=database.rawQuery("SELECT username FROM "+ DataBaseHelper.TABLE_NAME_USERDATA + " WHERE " +
                DataBaseHelper.EMAIL + " = ?", new String[]{email});
        if (cursor.getCount()<1){
            cursor.close();
            return "false";
        }
        else{
            cursor.moveToFirst();
            String username = cursor.getString(cursor.getColumnIndex(DataBaseHelper.USERNAME));
            cursor.close();
            return username;
        }

    }


    public void updateEntry (String username,String name, String email, String date, String password,byte[] image) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(DataBaseHelper.NAME, name);
        updatedValues.put(DataBaseHelper.EMAIL, email);
        updatedValues.put(DataBaseHelper.DATE, date);
        updatedValues.put(DataBaseHelper.PASSWORD, password);
        if (image!=null)
            updatedValues.put(DataBaseHelper.IMAGE, image);
        else;

        String where = "USERNAME = ?";
        database.update(DataBaseHelper.TABLE_NAME_USERDATA, updatedValues, where, new String[]{username});
    }

    public Cursor getProfileData(String username) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_USERDATA + " WHERE " +
                DataBaseHelper.USERNAME + " = ?", new String[]{username});
        if(cursor.getCount() < 1) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        return cursor;
    }

    /**The next Methods are for imageprofile   */
    public byte[] verifyImage (String username){
        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_USERDATA + " WHERE " +
                DataBaseHelper.USERNAME + " = ?", new String[]{username});
        if(cursor.getCount() < 1) {
            cursor.close();
            return null;
        }
        else {
            cursor.moveToFirst();
            byte[] image =  cursor.getBlob(cursor.getColumnIndex(DataBaseHelper.IMAGE));
            cursor.close();
            return image;
        }

    }

    /** The next methods are about places table */

    public String[] getPlaceByPersona(String persona, int phase_number){
        String [] markup = new String[3];
        String sql_query = "SELECT * FROM "+DataBaseHelper.TABLE_NAME_PLACES+" WHERE "+DataBaseHelper.PERSONA+
                " = "+persona+" AND "+DataBaseHelper.PHASE+" = "+phase_number;
        Cursor cursor = database.rawQuery(sql_query,null);

        if(cursor.getCount() < 1) { // UserName Not Exist
            cursor.close();
            return null;
        } else {
            cursor.moveToFirst();
            markup[0] = cursor.getString(cursor.getColumnIndex(DataBaseHelper.LOCAL));
            markup[1] = Double.toString(cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.LATITUDE)));
            markup[2] = Double.toString(cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.LONGITUDE)));
            cursor.close();
        }

        return markup;
    }

    public Cursor getChaptersInfo (String username) {

        Cursor cursor = getProfileData(username);
        String persona = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA));

        cursor.close();

        String sqlQuery = "SELECT * FROM "+DataBaseHelper.TABLE_NAME_PLACES+" WHERE "+DataBaseHelper.PERSONA+" = '"+persona
                +"' ORDER BY "+DataBaseHelper.PHASE;
        Cursor cursor2 = database.rawQuery(sqlQuery,null);

        cursor2.moveToFirst();

        return cursor2;
    }

    public List<PhaseDoneItem> getAllPhasesDone(String username) {

        Cursor cursor = getProfileData(username);
        String persona = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA));

        cursor.close();

        List<PhaseDoneItem> phasesDoneItem = new ArrayList<PhaseDoneItem>();

        String selectQuery = "SELECT " + DataBaseHelper.LOCAL +", "+ DataBaseHelper.SCORE+", "+ DataBaseHelper.PHASE +
                " FROM "+DataBaseHelper.TABLE_NAME_PLACES+" WHERE "+DataBaseHelper.PERSONA+" = '"+persona+"' AND " +
                DataBaseHelper.LOCKED + " = 0";

        Cursor cursor2 = database.rawQuery(selectQuery, null);
        int i = 0;
        nChapter = new int[cursor2.getCount()];
        chapterScore = new int[cursor2.getCount()];
        locals = new String[cursor2.getCount()];

        if (cursor2.moveToFirst()) {
            do {
                locals[i] = cursor2.getString(cursor2.getColumnIndex(DataBaseHelper.LOCAL));
                nChapter[i] = cursor2.getInt(cursor2.getColumnIndex(DataBaseHelper.PHASE));
                chapterScore[i] = cursor2.getInt(cursor2.getColumnIndex(DataBaseHelper.SCORE));

                PhaseDoneItem items = new PhaseDoneItem(locals[i], nChapter[i],chapterScore[i]);
                phasesDoneItem.add(items);
                i++;
            } while (cursor2.moveToNext()); // até terminar , pode-se usar movetoPrevious posteriormente
        }

        cursor2.close();

        return phasesDoneItem;
    }

    public Cursor getLastLocalName (String username){

        Cursor cursor = getProfileData(username);
        String persona = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA));

        cursor.close();

        String sqlQuery = "SELECT "+DataBaseHelper.LOCAL + "," + DataBaseHelper.PHASE + " FROM "+DataBaseHelper.TABLE_NAME_PLACES+" WHERE "+DataBaseHelper.LOCKED+" = 1 AND "
                + DataBaseHelper.PERSONA + " = '"+persona+"' ORDER BY "+DataBaseHelper.PHASE;
        Cursor cursor2 = database.rawQuery(sqlQuery,null);

        cursor2.moveToFirst();

        return cursor2;
    }

}
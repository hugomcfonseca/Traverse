package feup.traverse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public void updateProgress (String username){

        double progress = 0;
        Cursor cursor = getProfileData(username);

        progress = 12.5 + cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.PROGRESS));

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(DataBaseHelper.PROGRESS, progress);

        String where = "USERNAME = ?";
        database.update(DataBaseHelper.TABLE_NAME_USERDATA, updatedValues, where, new String[]{username});
    }

    /** The next methods are about places table */

    public void convertLockedUserToLockedPlaces (String username){

        String sqlQuery = "SELECT "+ DataBaseHelper.PROGRESS + " , "+ DataBaseHelper.PERSONA +" FROM "+
                DataBaseHelper.TABLE_NAME_USERDATA+" WHERE "+DataBaseHelper.USERNAME+" = '" + username + "'";

        Cursor cursor = database.rawQuery(sqlQuery, null);

        cursor.moveToFirst();

        if (cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.PROGRESS)) == 0){
            int[] chapters_unlocked = new int[]{1, 0, 0, 0, 0, 0, 0, 0};
            updatePlaceStatus(chapters_unlocked, cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA)));
        } else if (cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.PROGRESS)) == 12.5){
            int[] chapters_unlocked = new int[]{1, 1, 0, 0, 0, 0, 0, 0};
            updatePlaceStatus(chapters_unlocked,cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA)));
        } else if (cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.PROGRESS)) == 25){
            int[] chapters_unlocked = new int[]{1, 1, 1, 0, 0, 0, 0, 0};
            updatePlaceStatus(chapters_unlocked,cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA)));
        } else if (cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.PROGRESS)) == 37.5){
            int[] chapters_unlocked = new int[]{1, 1, 1, 1, 0, 0, 0, 0};
            updatePlaceStatus(chapters_unlocked,cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA)));
        } else if (cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.PROGRESS)) == 50){
            int[] chapters_unlocked = new int[]{1, 1, 1, 1, 1, 0, 0, 0};
            updatePlaceStatus(chapters_unlocked,cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA)));
        } else if (cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.PROGRESS)) == 62.5){
            int[] chapters_unlocked = new int[]{1, 1, 1, 1, 1, 1, 0, 0};
            updatePlaceStatus(chapters_unlocked,cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA)));
        } else if (cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.PROGRESS)) == 75){
            int[] chapters_unlocked = new int[]{1, 1, 1, 1, 1, 1, 1, 0};
            updatePlaceStatus(chapters_unlocked,cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA)));
        } else {
            int[] chapters_unlocked = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
            updatePlaceStatus(chapters_unlocked,cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA)));
        }

        cursor.close();
    }

    public void updatePlaceStatus (int[] chapter, String persona) {
        ContentValues updatedValues = new ContentValues();

        updatedValues.put(DataBaseHelper.LOCKED, 0);

        String where = DataBaseHelper.PHASE+ " = ? AND " + DataBaseHelper.PERSONA + " = ?";

        for (int i= 0; i < chapter.length; i++){
            if (chapter[i] == 1)
                database.update(DataBaseHelper.TABLE_NAME_PLACES, updatedValues, where, new String[]{String.valueOf(i+1),persona});
        }
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

    public Cursor getChapterInfo (String username, int phase) {

        Cursor cursor = getProfileData(username);
        String persona = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA));

        cursor.close();

        String sqlQuery = "SELECT * FROM "+DataBaseHelper.TABLE_NAME_PLACES+" WHERE "+DataBaseHelper.PERSONA+" = '"+persona
                +"' AND " + DataBaseHelper.PHASE + " = " + phase;
        Cursor cursor2 = database.rawQuery(sqlQuery,null);

        cursor2.moveToFirst();

        return cursor2;
    }

    public List<PhaseDoneItem> getAllPhasesDone(String username) {

        Cursor cursor = getProfileData(username);
        String persona = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA));

        cursor.close();

        List<PhaseDoneItem> phasesDoneItem = new ArrayList<PhaseDoneItem>();

        String selectQuery = "SELECT " + DataBaseHelper.LOCAL +", "+ DataBaseHelper.PHASE + " FROM "
                +DataBaseHelper.TABLE_NAME_PLACES+" WHERE "+DataBaseHelper.PERSONA+" = '"+persona+"' AND " +
                DataBaseHelper.LOCKED + " = 0";

        Cursor cursor2 = database.rawQuery(selectQuery, null);

        String selectQuery2 = "SELECT " + DataBaseHelper.SCORE + " FROM "
                +DataBaseHelper.TABLE_NAME_CHAPTERS+" WHERE "+DataBaseHelper.USERNAME+" = '"+username
                +"' AND "+DataBaseHelper.CHAPTERNUMBER + "<=" + cursor2.getCount();

        Cursor cursor3 = database.rawQuery(selectQuery2, null);
        Log.d("DEBUG",""+cursor3.getCount());

        int i = 0;
        nChapter = new int[cursor2.getCount()];
        chapterScore = new int[cursor3.getCount()];
        locals = new String[cursor2.getCount()];

        if (cursor2.moveToFirst() && cursor3.moveToFirst()) {
            do {
                locals[i] = cursor2.getString(cursor2.getColumnIndex(DataBaseHelper.LOCAL));
                nChapter[i] = cursor2.getInt(cursor2.getColumnIndex(DataBaseHelper.PHASE));
                chapterScore[i] = cursor3.getInt(cursor3.getColumnIndex(DataBaseHelper.SCORE));
                Log.d("DEBUG/SCORE",""+chapterScore[i]);

                PhaseDoneItem items = new PhaseDoneItem(locals[i], nChapter[i],chapterScore[i]);
                phasesDoneItem.add(items);
                i++;
            } while (cursor2.moveToNext() && cursor3.moveToNext()); // até terminar , pode-se usar movetoPrevious posteriormente
        }

        cursor2.close();
        cursor3.close();

        return phasesDoneItem;
    }

    public Cursor getLastLocalName (String username){

        Cursor cursor = getProfileData(username);
        String persona = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA));

        cursor.close();

        String sqlQuery = "SELECT "+DataBaseHelper.LOCAL + "," + DataBaseHelper.PHASE + " FROM "+DataBaseHelper.TABLE_NAME_PLACES+" WHERE "+DataBaseHelper.LOCKED+" = 0 AND "
                + DataBaseHelper.PERSONA + " = '"+persona+"' ORDER BY "+DataBaseHelper.PHASE + " DESC";
        Cursor cursor2 = database.rawQuery(sqlQuery,null);

        cursor2.moveToFirst();

        return cursor2;
    }

    public Cursor getCoordinatesAndLocal (int phase, String username){
        Cursor cursor = getProfileData(username);
        String persona = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERSONA));

        cursor.close();

        String sqlQuery = "SELECT "+DataBaseHelper.LOCAL + "," + DataBaseHelper.LATITUDE + "," + DataBaseHelper.LONGITUDE
                + " FROM "+DataBaseHelper.TABLE_NAME_PLACES+" WHERE "+DataBaseHelper.PHASE+" = " + phase +" AND "
                + DataBaseHelper.PERSONA + " = '"+persona+"'";
        Cursor cursor2 = database.rawQuery(sqlQuery,null);

        cursor2.moveToFirst();

        return cursor2;
    }

    /** The next methods are about chapters titles and score flags table */

    public Cursor getInfoByChapter (String username, int chapter_number){

        String sqlQuery = "SELECT * FROM "+ DataBaseHelper.TABLE_NAME_CHAPTERS + " WHERE " + DataBaseHelper.USERNAME+ " = '" +username+
                "' AND " + DataBaseHelper.CHAPTERNUMBER + " = " + chapter_number;
        Cursor cursor = database.rawQuery(sqlQuery,null);

        cursor.moveToFirst();

        return cursor;
    }

    public void updateUsernameInfoChapter (String username){
        String countQuery = "SELECT  * FROM " + DataBaseHelper.TABLE_NAME_CHAPTERS;

        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        for (int i = 0; i < cnt; i++){
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(DataBaseHelper.USERNAME, username);
            int chapter_number = i+1;
            database.update(DataBaseHelper.TABLE_NAME_CHAPTERS,updatedValues,DataBaseHelper.CHAPTERNUMBER+" = ?",new String[]{String.valueOf(chapter_number)});
        }
    }

    public Cursor getAllChaptersName (String username){

        String sqlQuery = "SELECT * FROM "+ DataBaseHelper.TABLE_NAME_CHAPTERS + " WHERE " + DataBaseHelper.USERNAME+ " = '" +username+
                "'";
        Cursor cursor = database.rawQuery(sqlQuery,null);

        cursor.moveToFirst();

        return cursor;
    }

    public void updateChallengesByChapter (String username, int chapter_number, int[] flags){

        ContentValues updatedValues = new ContentValues();

        if (flags[0] == 1){
            updatedValues.put(DataBaseHelper.FLAG_SCORE_MUSIC, flags[0]);
        }
        if (flags[1] == 1){
            updatedValues.put(DataBaseHelper.FLAG_SCORE_TEXT1, flags[1]);
        }
        if (flags[2] == 1){
            updatedValues.put(DataBaseHelper.FLAG_SCORE_MAPS, flags[2]);
        }
        if (flags[3] == 1){
            updatedValues.put(DataBaseHelper.FLAG_SCORE_TEXT2, flags[3]);
        }
        if (flags[4] == 1){
            updatedValues.put(DataBaseHelper.FLAG_SCORE_SHARING, flags[4]);
        }

        String where_clause = DataBaseHelper.CHAPTERNUMBER + " = ? AND " + DataBaseHelper.USERNAME + " = ?";

        database.update(DataBaseHelper.TABLE_NAME_CHAPTERS, updatedValues, where_clause, new String[]{String.valueOf(chapter_number), username});
    }

    public void updateScore (int addingValue, String username, int chapter_number){

        String sqlQuery = "SELECT "+ DataBaseHelper.SCORE +" FROM "+ DataBaseHelper.TABLE_NAME_CHAPTERS + " WHERE " + DataBaseHelper.USERNAME+ " = '" +username+
                "' AND " + DataBaseHelper.CHAPTERNUMBER + " = " + chapter_number;
        Cursor cursor = database.rawQuery(sqlQuery,null);

        cursor.moveToFirst();

        ContentValues updatedValues = new ContentValues();
        int score = 0;

        score = addingValue + cursor.getInt(cursor.getColumnIndex(DataBaseHelper.SCORE));

        updatedValues.put(DataBaseHelper.SCORE,score);

        String where_clause = DataBaseHelper.USERNAME + " = ? AND "+ DataBaseHelper.CHAPTERNUMBER + " = ? ";

        database.update(DataBaseHelper.TABLE_NAME_CHAPTERS,updatedValues,where_clause,new String[]{username, String.valueOf(chapter_number)});
    }

    public int[] getScoreFlags (String username, int chapter_number){
        int[] flags = new int[5];

        Cursor cursor = getInfoByChapter(username,chapter_number);

        flags[0] = cursor.getInt(cursor.getColumnIndex("score_audio"));
        flags[1] = cursor.getInt(cursor.getColumnIndex("score_text1"));
        flags[2] = cursor.getInt(cursor.getColumnIndex("score_maps"));
        flags[3] = cursor.getInt(cursor.getColumnIndex("score_text2"));
        flags[4] = cursor.getInt(cursor.getColumnIndex("score_sharing"));

        cursor.close();

        return flags;
    }

}
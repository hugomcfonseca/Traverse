package feup.traverse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author hugof
 * @date 28/11/2015.
 */

public class DataBaseAdapter {
    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private String[] allColumns = { DataBaseHelper.ID, DataBaseHelper.USERNAME, DataBaseHelper.EMAIL,
            DataBaseHelper.DATE, DataBaseHelper.PASSWORD};

    public DataBaseAdapter(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return database;
    }

    public void createUser (String username,String name, String email, String date, String password) {
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.USERNAME, username);
        values.put(DataBaseHelper.NAME, name);
        values.put(DataBaseHelper.EMAIL, email);
        values.put(DataBaseHelper.DATE, date);
        values.put(DataBaseHelper.PASSWORD, password);

        database.insert(DataBaseHelper.TABLE_NAME, null, values);
    }

    public void deleteEntry(String username) {
        database.delete(DataBaseHelper.TABLE_NAME, DataBaseHelper.USERNAME + " = " + username,
                null);
    }

    public String getSingleEntry(String username) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE " +
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
        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE " +
                DataBaseHelper.USERNAME + " = ?", new String[]{username});

        Cursor cursor2 = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE " +
                DataBaseHelper.EMAIL + " = ?", new String[]{email});

        if(cursor.getCount() < 1 && cursor2.getCount() < 1) { // UserName Not Exist
            cursor.close();
            return false;
        }

        return true;
    }

    public void updateEntry (String username,String name, String email, String date, String password)
    {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(DataBaseHelper.NAME, name);
        updatedValues.put(DataBaseHelper.EMAIL, email);
        updatedValues.put(DataBaseHelper.DATE, date);
        updatedValues.put(DataBaseHelper.PASSWORD, password);

        String where="USERNAME = ?";
        database.update(DataBaseHelper.TABLE_NAME, updatedValues, where, new String[]{username});
    }
    public Cursor getProfileData(String username) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE " +
                DataBaseHelper.USERNAME + " = ?", new String[]{username});

        if(cursor.getCount() < 1) {
            cursor.close();
            return null;
        }

        cursor.moveToFirst();

        return cursor;
    }

}
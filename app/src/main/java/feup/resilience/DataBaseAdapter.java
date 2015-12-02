package feup.resilience;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author hugof
 * @date 28/11/2015.
 */

public class DataBaseAdapter {
    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private String[] allColumns = { DataBaseHelper.USERNAME, DataBaseHelper.EMAIL,
            DataBaseHelper.PASSWORD};

    public DataBaseAdapter(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public DataBaseAdapter open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return database;
    }

    public void createUser (String username, String email, String password) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.USERNAME, username);
        values.put(DataBaseHelper.EMAIL, email);
        values.put(DataBaseHelper.PASSWORD,password);

        database.insert(DataBaseHelper.TABLE_NAME, null, values);
    }

    public void deleteEntry(String username) {
        database.delete(DataBaseHelper.TABLE_NAME, DataBaseHelper.USERNAME + " = " + username,
                null);
    }

    public void  updateEntry(String username, String email, String password)
    {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(DataBaseHelper.USERNAME, username);
        updatedValues.put(DataBaseHelper.EMAIL, email);
        updatedValues.put(DataBaseHelper.PASSWORD, password);

        String where="USERNAME = ?";
        database.update(DataBaseHelper.TABLE_NAME, updatedValues, where, new String[]{username});
    }
}

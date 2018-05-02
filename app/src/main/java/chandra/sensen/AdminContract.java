package chandra.sensen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by rd on 24/04/2018.
 */

public class AdminContract {

    public static  final String SQL_CREATE_ACCOUNTS = String.format(
            "CREATE TABLE %s(%s, %s, %s)",
            AdminEntry.TABLE_NAME,
            String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT", AdminEntry._ID),
            String.format("%s VARCHAR(40)", AdminEntry.COLUMN_NAME_USERNAME),
            String.format("%s VARCHAR(40)", AdminEntry.COLUMN_NAME_PASSWORD)
    );
    public static final String SQL_DELETE_ACCOUNTS = String.format("DROP TABLE IF EXISTS %s", AdminEntry.TABLE_NAME);

    private AdminContract(){}

    public static class AdminEntry implements BaseColumns {
        public static final String TABLE_NAME = "admin";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

    public static class AdminDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "sensen_admin.db";

        public AdminDbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ACCOUNTS);
            db.execSQL("INSERT INTO admin VALUES('1', 'admin', 'admin');");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ACCOUNTS);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }


    }
}

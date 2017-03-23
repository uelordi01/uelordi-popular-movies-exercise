package udacity.uelordi.com.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by uelordi on 22/03/17.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasksDb.db";
    private static final int VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID                + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_ID + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_SYNOPSYS + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_USER_RATING +  "REAL NOT NULL," +
                MovieContract.MovieEntry.COLUMN_IMAGE_URL   + " TEXT NOT NULL)";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}

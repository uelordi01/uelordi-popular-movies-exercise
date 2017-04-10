package udacity.uelordi.com.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by uelordi on 22/03/17.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MoviesDb.db";
    private static final int VERSION = 5;
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(MovieContract.MovieEntry.SQL_CREATE_TABLE);
//        db.execSQL(MovieContract.PopularEntry.SQL_CREATE_TABLE);
//        db.execSQL(MovieContract.HighestRatedEntry.SQL_CREATE_TABLE);
//        db.execSQL(MovieContract.FavoritesEntry.SQL_CREATE_TABLE);
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( DROP_TABLE + MovieContract.MovieEntry.TABLE_NAME);
//        db.execSQL( DROP_TABLE + MovieContract.PopularEntry.TABLE_NAME);
//        db.execSQL( DROP_TABLE + MovieContract.HighestRatedEntry.TABLE_NAME);
//        db.execSQL( DROP_TABLE + MovieContract.FavoritesEntry.TABLE_NAME);
        onCreate(db);
    }
}

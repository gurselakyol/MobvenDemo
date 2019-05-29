package grsl.com.mobvenmoviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";
    private static final String TABLE_NAME = "favorites";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "MOVIEID";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,MOVIEID TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,movieId);
        long result = db.insert(TABLE_NAME,null ,contentValues);

        if(result == -1) {

            return false;
        }else {

            return true;
        }
    }

    public boolean getData(String movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_2+" LIKE '%"+ movieId +"%'",null);

        if(res.getCount()==0){

            return false;
        }else {

            return true;
        }
    }

    public Integer deleteData (String movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "MOVIEID = ?",new String[] {movieId});
    }

}

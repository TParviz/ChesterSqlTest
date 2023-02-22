package tj.test.chestersql;

import static tj.test.chestersql.Utils.CHILD_TYPE;
import static tj.test.chestersql.Utils.PARENT_TYPE;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "test_db.sqlite";
    public final static String DATABASE_PATH = "/data/user/0/tj.test.chestersql/databases/";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;

    }

    //Create a empty database on the system
    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }

        boolean dbExist1 = checkDataBase();
        if (!dbExist1) {
            this.getReadableDatabase();
            try {
                this.close();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    //Check database already exist or not
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        } catch (SQLiteException e) {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException {

        InputStream mInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[2024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //delete database
    public void db_delete() {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if (file.exists()) {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    //Open database
    public void openDatabase() throws SQLException {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
//        Log.d("TAG_DATABASe" ,"database: " + );
    }

    public List<Tt> readDatabase(int start, int end, int type, long parentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Tt> ttList = new ArrayList<>();

//        // on below line we are creating a cursor with query to read data from database.
        if (type == PARENT_TYPE) {
            Cursor cursor = db.rawQuery("SELECT * FROM tt WHERE _parent_id = 0 LIMIT  " + start + "," + end, null);
            ttList = cursorDb(cursor);
            return ttList;
        } else if (type == CHILD_TYPE) {
            Cursor cursor = db.rawQuery("SELECT * FROM tt WHERE _parent_id = " + parentId + " LIMIT " + start + "," + end, null);
            ttList = cursorDb(cursor);
            return ttList;
        }
        return ttList;
    }

    public int getCountDB(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tt WHERE _parent_id = " + id, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    private ArrayList<Tt> cursorDb(Cursor cursor) {
        ArrayList<Tt> ttList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ttList.add(new Tt(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getLong(1), getCountDB(cursor.getLong(2))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ttList;
    }

    public synchronized void closeDataBase() throws SQLException {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
        }
    }
}
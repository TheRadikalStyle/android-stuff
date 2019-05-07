import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DBName = "DATABASE_NAME.db";
    private static final int version = 1;

    private static final String TEXT_TYPE = " TEXT ";
    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String REAL_TYPE = " REAL ";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA = ",";

    public DBHelper(Context context) {
        super(context, DBName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CreateTableUnidades);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
      //Call Drop queries
      //onCreate(sqLiteDatabase);
    }

    //region Create Queries
    private static String CreateTableUsers = "CREATE TABLE " + DBSchema.TableUsers.TableName + "(" +
            DBSchema.TableUnidades.COLUMN_NAME_ID + INTEGER_TYPE + NOT_NULL + " PRIMARY KEY AUTOINCREMENT" + COMMA +
            DBSchema.TableUnidades.COLUMN_NAME_NAME + INTEGER_TYPE + NOT_NULL +COMMA +
            DBSchema.TableUnidades.COLUMN_NAME_TYPE + TEXT_TYPE + NOT_NULL +
            ")";
    //endregion

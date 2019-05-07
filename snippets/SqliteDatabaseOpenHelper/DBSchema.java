import android.provider.BaseColumns;

public class DBSchema{
    private DBSchema(){}

    public static class TableUsers implements BaseColumns{
        public static final String TableName = "users";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TYPE = "type";
    }
}

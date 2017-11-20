package cgodin.qc.ca.androidrestaurant.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

import cgodin.qc.ca.androidrestaurant.activities.MainActivity;
import cgodin.qc.ca.androidrestaurant.model.Restaurant;
import cgodin.qc.ca.androidrestaurant.model.User;

/**
 * Created by Ariel S on 2017-10-29.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "MyDatabase.db";
    private static final String TABLE_USER = "User";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_UTYPE = "user_utype";
    private static final String COLUMN_USER_UID = "user_uid";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_FAVORITES = "user_favorites";

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_UTYPE + " TEXT,"
            + COLUMN_USER_UID + " TEXT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_FAVORITES + "TEXT"
            + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public User getCurrentUser(String strId){
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();

        String[] columns ={COLUMN_USER_ID, COLUMN_USER_UTYPE,COLUMN_USER_UID,COLUMN_USER_NAME,COLUMN_USER_EMAIL,COLUMN_USER_PASSWORD,COLUMN_USER_FAVORITES};

        if (MainActivity.strCurrentUserType == 1){
            Cursor findEntry = db.query(TABLE_USER, columns, "user_email=?", new String[] { strId }, null, null, null, null);

            if (findEntry.moveToFirst()){
                //strFavoris = findEntry.getString(findEntry.getColumnIndex("user_favorites"));
                String IdUser = findEntry.getString(findEntry.getColumnIndex("user_id"));
                String type = findEntry.getString(findEntry.getColumnIndex("user_utype"));
                String uId = findEntry.getString(findEntry.getColumnIndex("user_uid"));
                String name = findEntry.getString(findEntry.getColumnIndex("user_name"));
                String email = findEntry.getString(findEntry.getColumnIndex("user_email"));
                String password = findEntry.getString(findEntry.getColumnIndex("user_password"));

                user.setId(Integer.valueOf(IdUser));
                user.setUtype(Integer.valueOf(type));
                user.setUid(uId);
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);

            }

        }
        else {

            Cursor findEntry = db.query(TABLE_USER, columns, "user_uid=?", new String[] { strId }, null, null, null, null);

            if (findEntry.moveToFirst()){
                String IdUser = findEntry.getString(findEntry.getColumnIndex("user_id"));
                String type = findEntry.getString(findEntry.getColumnIndex("user_utype"));
                String uId = findEntry.getString(findEntry.getColumnIndex("user_uid"));
                String name = findEntry.getString(findEntry.getColumnIndex("user_name"));
                String email = findEntry.getString(findEntry.getColumnIndex("user_email"));
                String password = findEntry.getString(findEntry.getColumnIndex("user_password"));

                user.setId(Integer.valueOf(IdUser));
                user.setUtype(Integer.valueOf(type));
                user.setUid(uId);
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
            }

        }


        return user;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    public void addFavorite(Restaurant restaurant,User user){
        SQLiteDatabase db = this.getWritableDatabase();

        String strFavoris = "";

        // Format des favoris formatted_address,name,rating,place_id,photo_reference,img_link,place_url,formatted_phone_number,lat,lng;...

        ContentValues cv = new ContentValues();

        String[] columns ={COLUMN_USER_ID, COLUMN_USER_UTYPE,COLUMN_USER_UID,COLUMN_USER_NAME,COLUMN_USER_EMAIL,COLUMN_USER_PASSWORD,COLUMN_USER_FAVORITES};

        if (user.getUtype() == 1){
            Cursor findEntry = db.query(TABLE_USER, columns, "user_email=?", new String[] { String.valueOf(user.getEmail()) }, null, null, null, null);

            if (findEntry.moveToFirst()){
                strFavoris = findEntry.getString(findEntry.getColumnIndex("user_favorites"));
            }

            strFavoris += restaurant.getFormatted_address() + "," + restaurant.getName() + "," + restaurant.getRating() + "," + restaurant.getPlace_id() + ","
                    + restaurant.getPhoto_reference() + "," + restaurant.getImg_link() + "," + restaurant.getPlace_url() + "," + restaurant.getFormatted_phone_number()
                    + restaurant.getLat() + "," + restaurant.getLng() + ";";

            cv.put(COLUMN_USER_FAVORITES,strFavoris);

            db.update(TABLE_USER, cv, "user_id=" + user.getId(), null);
        }
        else {
            Cursor findEntry = db.query(TABLE_USER, columns, "user_uid=?", new String[] { String.valueOf(user.getUid()) }, null, null, null, null);

            if (findEntry.moveToFirst()){
                strFavoris = findEntry.getString(findEntry.getColumnIndex("user_favorites"));
            }

            strFavoris += restaurant.getFormatted_address() + "," + restaurant.getName() + "," + restaurant.getRating() + "," + restaurant.getPlace_id() + ","
                    + restaurant.getPhoto_reference() + "," + restaurant.getImg_link() + "," + restaurant.getPlace_url() + "," + restaurant.getFormatted_phone_number()
                    + restaurant.getLat() + "," + restaurant.getLng() + ";";

            String where = "id=?";
            String[] whereArgs = new String[] {String.valueOf(user.getUid())};

            cv.put(COLUMN_USER_FAVORITES,strFavoris);

            db.update(TABLE_USER, cv, where, whereArgs);

        }

        db.close();
    }

    public ArrayList<Restaurant> getCurrentUserFavorites(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Restaurant> lstRestaurants = new ArrayList<Restaurant>();

        String strFavoris = "";

        String[] columns ={COLUMN_USER_ID, COLUMN_USER_UTYPE,COLUMN_USER_UID,COLUMN_USER_NAME,COLUMN_USER_EMAIL,COLUMN_USER_PASSWORD,COLUMN_USER_FAVORITES};

        if (user.getUtype() == 1){
            Cursor findEntry = db.query(TABLE_USER, columns, "user_email=?", new String[] { String.valueOf(user.getEmail()) }, null, null, null, null);

            if (findEntry.moveToFirst()){
                strFavoris = findEntry.getString(findEntry.getColumnIndex("user_favorites"));
            }

            String[] restraurantsStringArray = strFavoris.split(";");
            for (int i = 0; i < restraurantsStringArray.length; i++) {

                String[] restraurant = restraurantsStringArray[i].split(",");

                String formatted_address = restraurant[0];
                String name = restraurant[1];
                double rating = Double.valueOf(restraurant[2]);
                String place_id = restraurant[3];
                String photo_reference = restraurant[4];
                String img_link = restraurant[5];
                String place_url = restraurant[6];
                String formatted_phone_number = restraurant[7];
                String lat = restraurant[8];
                String lng = restraurant[9];

                Restaurant restaurant = new Restaurant(formatted_address,name,rating,place_id,photo_reference,img_link,place_url,formatted_phone_number,lat,lng);

                lstRestaurants.add(restaurant);
            }

        }
        else {

            Cursor findEntry = db.query(TABLE_USER, columns, "user_uid=?", new String[] { String.valueOf(user.getUid()) }, null, null, null, null);

            if (findEntry.moveToFirst()){
                strFavoris = findEntry.getString(findEntry.getColumnIndex("user_favorites"));
            }

            String[] restraurantsStringArray = strFavoris.split(";");
            for (int i = 0; i < restraurantsStringArray.length; i++) {

                String[] restraurant = restraurantsStringArray[i].split(",");

                String formatted_address = restraurant[0];
                String name = restraurant[1];
                double rating = Double.valueOf(restraurant[2]);
                String place_id = restraurant[3];
                String photo_reference = restraurant[4];
                String img_link = restraurant[5];
                String place_url = restraurant[6];
                String formatted_phone_number = restraurant[7];
                String lat = restraurant[8];
                String lng = restraurant[9];

                Restaurant restaurant = new Restaurant(formatted_address,name,rating,place_id,photo_reference,img_link,place_url,formatted_phone_number,lat,lng);

                lstRestaurants.add(restaurant);
            }

        }

        return lstRestaurants;
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Values to add
        values.put(COLUMN_USER_UTYPE, user.getUtype());
        values.put(COLUMN_USER_UID, user.getUid());
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        //Insert data in table and close
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /* Returns true if UID already exists in database */
    public boolean checkUid(String uid){
        boolean blnVerdict = false;
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_UID + " = ?";
        String[] selectionArgs = { uid };

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount > 0){
            blnVerdict = true;
        }

        return blnVerdict;
    }

    /* Returns true if email already exists in database */
    public boolean checkUser(String email){
        boolean blnVerdict = false;
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount > 0){
            blnVerdict = true;
        }

        return blnVerdict;
    }

    /* Returns true if email already exists in database */
    public boolean checkUser(String email, String password){
        boolean blnVerdict = false;
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount > 0){
            blnVerdict = true;
        }

        return blnVerdict;
    }
}

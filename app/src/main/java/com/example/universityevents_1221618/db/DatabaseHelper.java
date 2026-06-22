package com.example.universityevents_1221618.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;
import com.example.universityevents_1221618.models.Event;
import com.example.universityevents_1221618.models.Reservation;
import com.example.universityevents_1221618.models.User;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UniversityEvents.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_EVENTS = "events";
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_RESERVATIONS = "reservations";

    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EVENT_ID = "event_id";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITY = "city";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PROFILE_PIC_URI = "profile_pic_uri";
    private static final String KEY_ROLE = "role";

    private static final String KEY_TITLE = "title";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DATE = "date";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_TIME = "time";
    private static final String KEY_SEATS = "seats";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IS_FEATURED = "is_featured";

    private static final String KEY_RESERVATION_DATE = "reservation_date";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_EMAIL + " TEXT UNIQUE NOT NULL," + KEY_FIRST_NAME + " TEXT NOT NULL," + KEY_LAST_NAME + " TEXT NOT NULL," + KEY_PASSWORD + " TEXT NOT NULL," + KEY_GENDER + " TEXT," + KEY_COUNTRY + " TEXT," + KEY_CITY + " TEXT," + KEY_PHONE + " TEXT," + KEY_PROFILE_PIC_URI + " TEXT," + KEY_ROLE + " TEXT NOT NULL" + ")";
    private static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_TITLE + " TEXT," +
                    KEY_DESCRIPTION + " TEXT," +
                    KEY_CATEGORY + " TEXT," +
                    KEY_DATE + " TEXT," +
                    KEY_TIME + " TEXT," +
                    KEY_LOCATION + " TEXT," +
                    KEY_SEATS + " INTEGER," +
                    KEY_IMAGE_URL + " TEXT," +
                    KEY_IS_FEATURED + " INTEGER DEFAULT 0" +
                    ")";
    private static final String CREATE_TABLE_FAVORITES = "CREATE TABLE " + TABLE_FAVORITES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_ID + " INTEGER," + KEY_EVENT_ID + " INTEGER, FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + "), FOREIGN KEY(" + KEY_EVENT_ID + ") REFERENCES " + TABLE_EVENTS + "(" + KEY_ID + ")" + ")";
    private static final String CREATE_TABLE_RESERVATIONS = "CREATE TABLE " + TABLE_RESERVATIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_ID + " INTEGER," + KEY_EVENT_ID + " INTEGER," + KEY_RESERVATION_DATE + " TEXT, FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + "), FOREIGN KEY(" + KEY_EVENT_ID + ") REFERENCES " + TABLE_EVENTS + "(" + KEY_ID + ")" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_EVENTS);
        db.execSQL(CREATE_TABLE_FAVORITES);
        db.execSQL(CREATE_TABLE_RESERVATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_FIRST_NAME, user.getFirstName());
        values.put(KEY_LAST_NAME, user.getLastName());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_GENDER, user.getGender());
        values.put(KEY_COUNTRY, user.getCountry());
        values.put(KEY_CITY, user.getCity());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_ROLE, user.getRole());
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public User checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, KEY_EMAIL + "=? AND " + KEY_PASSWORD + "=?", new String[]{email, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRST_NAME)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_NAME)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_GENDER)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_COUNTRY)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_CITY)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROFILE_PIC_URI)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE)));
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID}, KEY_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, user.getFirstName());
        values.put(KEY_LAST_NAME, user.getLastName());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_PROFILE_PIC_URI, user.getProfilePicUri());
        int rows = db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
        return rows > 0;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, KEY_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRST_NAME)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_NAME)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_GENDER)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_COUNTRY)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_CITY)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROFILE_PIC_URI)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE)));
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public void addEventsFromJSON(JSONArray eventsArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            db.delete(TABLE_EVENTS, null, null);

            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject event = eventsArray.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(KEY_ID, event.getInt("id"));
                values.put(KEY_TITLE, event.getString("title"));
                values.put(KEY_DESCRIPTION, event.getString("description"));
                values.put(KEY_CATEGORY, event.getString("category"));
                values.put(KEY_DATE, event.getString("date"));
                values.put(KEY_TIME, event.getString("time"));
                values.put(KEY_LOCATION, event.getString("location"));
                values.put(KEY_SEATS, event.getInt("seats"));
                values.put(KEY_IMAGE_URL, event.getString("image"));

                values.put(KEY_IS_FEATURED, (i < 3) ? 1 : 0);

                db.insert(TABLE_EVENTS, null, values);
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<Event> getEvents(String categoryFilter, String locationFilter, int minSeats, boolean featuredOnly) {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = featuredOnly ? KEY_IS_FEATURED + " = 1" : null;
        List<String> selectionArgs = new ArrayList<>();

        if (categoryFilter != null && !categoryFilter.isEmpty() && !categoryFilter.equals("All")) {
            if (selection == null) selection = ""; else selection += " AND ";
            selection += KEY_CATEGORY + " = ?";
            selectionArgs.add(categoryFilter);
        }

        if (locationFilter != null && !locationFilter.isEmpty()) {
            if (selection == null) selection = ""; else selection += " AND ";
            selection += KEY_LOCATION + " LIKE ?";
            selectionArgs.add("%" + locationFilter + "%");
        }

        if (minSeats > 0) {
            if (selection == null) selection = ""; else selection += " AND ";
            selection += KEY_SEATS + " >= ?";
            selectionArgs.add(String.valueOf(minSeats));
        }

        Cursor cursor = db.query(TABLE_EVENTS, null, selection,
                selectionArgs.toArray(new String[0]), null, null, KEY_DATE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SEATS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL))
                );

                eventList.add(event);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventList;
    }

    public void addFavorite(int userId, int EventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_EVENT_ID, EventId);
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    public void removeFavorite(int userId, int EventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, KEY_USER_ID + " = ? AND " + KEY_EVENT_ID + " = ?", new String[]{String.valueOf(userId), String.valueOf(EventId)});
        db.close();
    }

    public boolean isFavorite(int userId, int EventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, null, KEY_USER_ID + " = ? AND " + KEY_EVENT_ID + " = ?", new String[]{String.valueOf(userId), String.valueOf(EventId)}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public List<Event> getFavoriteEvents(int userId) {
        List<Event> EventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p.* FROM " + TABLE_EVENTS + " p INNER JOIN " + TABLE_FAVORITES + " f ON p." + KEY_ID + " = f." + KEY_EVENT_ID + " WHERE f." + KEY_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SEATS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL))
                );
                EventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return EventList;
    }

    public boolean addReservation(int userId, int EventId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_EVENT_ID, EventId);
        values.put(KEY_RESERVATION_DATE, date);
        long result = db.insert(TABLE_RESERVATIONS, null, values);
        db.close();
        return result != -1;
    }

    public List<Reservation> getReservations(int userId) {
        List<Reservation> reservationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT r." + KEY_ID + " as reservation_id, r." + KEY_RESERVATION_DATE + ", p.* FROM " + TABLE_RESERVATIONS + " r INNER JOIN " + TABLE_EVENTS + " p ON r." + KEY_EVENT_ID + " = p." + KEY_ID + " WHERE r." + KEY_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SEATS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL))
                );
                Reservation reservation = new Reservation(cursor.getInt(cursor.getColumnIndexOrThrow("reservation_id")), userId, event, cursor.getString(cursor.getColumnIndexOrThrow(KEY_RESERVATION_DATE)));
                reservationList.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reservationList;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, KEY_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.delete(TABLE_RESERVATIONS, KEY_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        int result = db.delete(TABLE_USERS, KEY_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
        return result > 0;
    }

    public List<User> getAllCustomers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, KEY_ROLE + " = ?", new String[]{"USER"}, null, null, KEY_FIRST_NAME);
        if (cursor.moveToFirst()) {
            do {
                userList.add(new User(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRST_NAME)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_NAME)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_GENDER)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_COUNTRY)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_CITY)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROFILE_PIC_URI)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public int getNumberOfUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) db.compileStatement("SELECT COUNT(*) FROM " + TABLE_USERS).simpleQueryForLong();
    }

    public int getNumberOfReservations() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) db.compileStatement("SELECT COUNT(*) FROM " + TABLE_RESERVATIONS).simpleQueryForLong();
    }

    public Map<String, Integer> getCountryReservationCounts() {
        Map<String, Integer> counts = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u." + KEY_COUNTRY + ", COUNT(r." + KEY_ID + ") as count FROM " + TABLE_USERS + " u JOIN " + TABLE_RESERVATIONS + " r ON u." + KEY_ID + " = r." + KEY_USER_ID + " GROUP BY u." + KEY_COUNTRY;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                counts.put(cursor.getString(0), cursor.getInt(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return counts;
    }

    public Pair<Float, Float> getGenderPercentage() {
        SQLiteDatabase db = this.getReadableDatabase();
        float maleCount = db.compileStatement("SELECT COUNT(*) FROM " + TABLE_USERS + " WHERE " + KEY_GENDER + " = 'Male'").simpleQueryForLong();
        float femaleCount = db.compileStatement("SELECT COUNT(*) FROM " + TABLE_USERS + " WHERE " + KEY_GENDER + " = 'Female'").simpleQueryForLong();
        float total = maleCount + femaleCount;
        if (total == 0) return new Pair<>(0f, 0f);
        return new Pair<>((maleCount / total) * 100, (femaleCount / total) * 100);
    }

    public void setFeaturedEvent(int EventId, boolean isFeatured) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_FEATURED, isFeatured ? 1 : 0);
        db.update(TABLE_EVENTS, values, KEY_ID + " = ?", new String[]{String.valueOf(EventId)});
        db.close();
    }

    public boolean isEventFeatured(int EventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, new String[]{KEY_IS_FEATURED}, KEY_ID + " = ?", new String[]{String.valueOf(EventId)}, null, null, null);
        if (cursor.moveToFirst()) {
            boolean featured = cursor.getInt(0) == 1;
            cursor.close();
            return featured;
        }
        cursor.close();
        return false;
    }

    public List<Pair<Reservation, User>> getAllReservationsWithUsers() {
        List<Pair<Reservation, User>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                "u." + KEY_ID + " as user_id_col, u.*, " +
                "r." + KEY_ID + " as reservation_id_col, r.*, " +
                "p." + KEY_ID + " as Event_id_col, p.* " +
                "FROM " + TABLE_USERS + " u " +
                "JOIN " + TABLE_RESERVATIONS + " r ON u." + KEY_ID + " = r." + KEY_USER_ID + " " +
                "JOIN " + TABLE_EVENTS + " p ON r." + KEY_EVENT_ID + " = p." + KEY_ID + " " +
                "ORDER BY r." + KEY_RESERVATION_DATE + " DESC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id_col")),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_GENDER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_COUNTRY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_CITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROFILE_PIC_URI)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE))
                );
                Event event = new Event(
                        cursor.getInt(cursor.getColumnIndexOrThrow("Event_id_col")),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SEATS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL))
                );
                Reservation reservation = new Reservation(
                        cursor.getInt(cursor.getColumnIndexOrThrow("reservation_id_col")),
                        user.getId(),
                        event,
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_RESERVATION_DATE))
                );
                list.add(new Pair<>(reservation, user));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}

package com.reminder.menu;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import java.sql.Blob;
import java.sql.SQLTransactionRollbackException;
import java.util.ArrayList;

/**
 * Created by admin on 19-01-2017.
 */

public class SQLClass extends SQLiteOpenHelper {

    SQLiteDatabase db;
    public static final String DATABASE_NAME = "Remindme.db";
    public static final String TABLE_NAME = "info_table";
    public static final String CAT_NAME = "cat_table";
    public static final String EXT_TABLE = "extra_table";
    public static final String PAY_HISTORY_TABLE = "history_table";
    public static final String UPDATE_CAT_NAME = "Category_table";
    public static final String up_cat_col2 = "CATEGORY_NAME";
    public static final String CAT_COL1 = "CATEGORY_NAME";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "HOLDER_NAMES";
    public static final String COL4 = "INVESTMENT_PLAN";
    public static final String COL5 = "POLICY_NO";
    public static final String COL6 = "SUM_ASSURED";
    public static final String COL7 = "START_DATE";
    public static final String COL8 = "MATURITY_DATE";
    public static final String COL9 = "PREMIUM_INTERVAL";
    public static final String COL10 = "INTERVAL_AMOUNT";
    public static final String COL11 = "PREMIUM_DATE";
    public static final String COL12 = "No_of_installment";
    public static final String COL13 = "TOTAL_PREMIUM_AMOUNT";
    public static final String COL14 = "BEFORE_NOTIFY";
    public static final String COL15 = "LAST_PREMIUM_DATE";
    public static final String COL16 = "MAIN_HOLDER_NAME";
    public static final String EXT_COLID = "ID";
    public static final String EXT_COL1 = "COL1", EXT_COL2 = "COL2", EXT_COL3 = "COL3", EXT_COL4 = "COL4", EXT_COL5 = "COL5", EXT_COL6 = "COL6";
    public static final String EXT_COL7 = "COL7", EXT_COL8 = "COL8", EXT_COL9 = "COL9", EXT_COL10 = "COL10";
    public static final String HIS_COL1 = "ID";
    public static final String HIS_COL2 = "NAME";
    public static final String HIS_COL3 = "DATE";
    public static final String SNOOZE_DATE_TABLE = "SNOOZE_DATE_TABLE";
    public static final String SNOOZE_COL1 = "ID", SNOOZE_COL2 = "DAY", SNOOZE_COL3 = "MONTH", SNOOZE_COL4 = "YEAR", SNOOZE_COL5 = "HOUR", SNOOZE_COL6 = "MINUTE", SNOOZE_COL7 = "TYPE";
    public static final String PENDING_INSTALL_DATE_TABLE = "PENDING_INSTALL_DATE_TABLE";
    public static final String PENDING_COL1 = "ID", PENDING_COL2 = "DAY", PENDING_COL3 = "MONTH", PENDING_COL4 = "YEAR", PENDING_COL5 = "MINUTE", PENDING_COL6 = "HOUR";
    public static final String DATE_TABLE = "STORE_DATE";
    public static final String DATE_COL1 = "ID", DATE_COL2 = "DAY", DATE_COL3 = "MONTH", DATE_COL4 = "YEAR", DATE_COL5 = "HOUR", DATE_COL6 = "MINUTE";
    public static final String HOLDER_TABLE = "HOLDER";
    public static final String HOLDER_COL1 = "ID", HOLDER_COL2 = "NAME", HOLDER_COL3 = "RELATION", HOLDER_COL4 = "AGE";
    ArrayList<String> arr = new ArrayList<>();
    ArrayList<String> list_cat = new ArrayList<>();
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_ID = "MAIN_ID";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASS = "PASSWORD";
    public static final String COLUMN_MOB = "MOBILE";
    public static final String COLUMN_USER = "NAME";
    public static final String COLUMN_MODE = "MODE";
    public static final String COLUMN_IMAGE = "PROFILE";
    public static final String COLUMN_GOOGLE_IMAGE_STRING = "PROFILESTR";
    public static final String COLUMN_FB_IMAGE_STRING = "PROFILESTR1";
    public static final String COLUMN_FB_ID = "FBID";
    public static final String PENDING_PREMIUM_AMOUNT = "PENDING_PREMIUM_TABLE", PENDING_PREMIUM_COL1 = "ID", PENDING_PREMIUM_COL2 = "AMOUNT",PENDING_PREMIUM_COL3 = "PAID";
    public static final String QUICK_COL1 = "ID", QUICK_COL2 = "TITLE", QUICK_COL3 = "DAY", QUICK_COL4 = "MONTH", QUICK_COL5 = "YEAR", QUICK_COL6 = "HOUR", QUICK_COL7 = "MINUTE", QUICK_COL8 = "DESCRIPTION";
    public static final String QUICK_NAME = "QUICK_REMINDER_TABLE";
    public static final String COMPLETED_NAME = "COMPLETED_TABLE", COMPLETED_COL1 = "ID", COMPLETED_COL2 = "NAME", COMPLETED_COL3 = "POLICY_NO", COMPLETED_COL4 = "INVESTMENT_PLAN";
    public static final String INSTALL_MONTH_SAVE = "INSTALL_TABLE", INSTALL_COL1 = "ID", INSTALL_COL2 = "COUNT", INSTALL_COL3 = "TOTAL";
    int chk = 0;
    static int Main_id;
    /*public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_MOB + " TEXT,"
            + COLUMN_PASS + " TEXT);";*/

    public static void setId(int id) {
        Main_id = id;
    }

    public SQLClass(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        chk = settings.getInt("silentMode", 0);
        if (chk == 0) {
            settings = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("silentMode", 1);
            editor.commit();
            db = this.getWritableDatabase();
            list_cat.add("Mediclaim");
            list_cat.add("LIC");
            list_cat.add("PF");
            list_cat.add("Other");
            for (int i = 0; i < list_cat.size(); i++) {
                insertdefault(list_cat.get(i));
            }
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CAT_NAME + "(CATEGORY_NAME TEXT)");
        db.execSQL("create table " + UPDATE_CAT_NAME + "(MAIN_ID INTEGER,CATEGORY_NAME TEXT)");
        db.execSQL("create table " + TABLE_NAME + "(MAIN_ID INTEGER,ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,HOLDER_NAMES TEXT,MAIN_HOLDER_NAME TEXT,INVESTMENT_PLAN TEXT,POLICY_NO TEXT,SUM_ASSURED TEXT,START_DATE TEXT,MATURITY_DATE TEXT,PREMIUM_INTERVAL TEXT,INTERVAL_AMOUNT INTEGER,PREMIUM_DATE TEXT,No_of_installment INTEGER,TOTAL_PREMIUM_AMOUNT INTEGER,BEFORE_NOTIFY TEXT,LAST_PREMIUM_DATE TEXT)");
        db.execSQL("create table " + EXT_TABLE + "(MAIN_ID INTEGER,ID INTEGER,COL1 TEXT,COL2 TEXT,COL3 TEXT,COL4 TEXT,COL5 TEXT,COL6 TEXT,COL7 TEXT,COL8 TEXT,COL9 TEXT,COL10 TEXT)");
        db.execSQL("create table " + PAY_HISTORY_TABLE + "(MAIN_ID INTEGER,ID INTEGER,NAME TEXT,DATE TEXT)");
        db.execSQL("create table " + PENDING_INSTALL_DATE_TABLE + "(MAIN_ID INTEGER,ID INTEGER,DAY INTEGER,MONTH INTEGER,YEAR INTEGER,HOUR INTEGER,MINUTE INTEGER)");
        db.execSQL("create table " + DATE_TABLE + "(MAIN_ID INTEGER,ID INTEGER,DAY INTEGER,MONTH INTEGER,YEAR INTEGER,HOUR INTEGER,MINUTE INTEGER)");
        db.execSQL("create table " + SNOOZE_DATE_TABLE + "(MAIN_ID INTEGER,ID INTEGER,DAY INTEGER,MONTH INTEGER,YEAR INTEGER,HOUR INTEGER,MINUTE INTEGER,TYPE TEXT)");
        db.execSQL("create table " + HOLDER_TABLE + "(MAIN_ID INTEGER,ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,RELATION TEXT,AGE INTEGER)");
        db.execSQL("create table " + USER_TABLE + "(MAIN_ID INTEGER PRIMARY KEY AUTOINCREMENT,EMAIL TEXT,PASSWORD TEXT,MOBILE TEXT,NAME TEXT,MODE TEXT,PROFILE BLOB,PROFILESTR TEXT,PROFILESTR1 TEXT,FBID TEXT)");
        db.execSQL("create table " + QUICK_NAME + "(MAIN_ID INTEGER,ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,DAY INTEGER,MONTH INTEGER,YEAR INTEGER,HOUR INTEGER,MINUTE INTEGER,DESCRIPTION TEXT)");
        db.execSQL("create table " + COMPLETED_NAME + "(MAIN_ID INTEGER,ID INTEGER,NAME TEXT,POLICY_NO TEXT,INVESTMENT_PLAN TEXT)");
        db.execSQL("create table " + INSTALL_MONTH_SAVE + "(MAIN_ID INTEGER,ID INTEGER,COUNT INTEGER,TOTAL INTEGER)");
        db.execSQL("create table " + PENDING_PREMIUM_AMOUNT + "(MAIN_ID INTEGER,ID INTEGER,AMOUNT INTEGER,PAID INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        db.execSQL("DROP TABLE " + CAT_NAME);
        db.execSQL("DROP TABLE " + EXT_TABLE);
        db.execSQL("DROP TABLE " + PAY_HISTORY_TABLE);
        db.execSQL("DROP TABLE " + PENDING_INSTALL_DATE_TABLE);
        db.execSQL("DROP TABLE " + DATE_TABLE);
        db.execSQL("DROP TABLE " + SNOOZE_DATE_TABLE);
        db.execSQL("DROP TABLE " + HOLDER_TABLE);
        db.execSQL("DROP TABLE " + USER_TABLE);
        db.execSQL("DROP TABLE " + QUICK_NAME);
        db.execSQL("DROP TABLE " + COMPLETED_NAME);
        db.execSQL("DROP TABLE " + INSTALL_MONTH_SAVE);
        db.execSQL("DROP TABLE " + PENDING_PREMIUM_AMOUNT);
        onCreate(db);
    }

    public boolean insertHolder(int main_id, String name, String Relation, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, main_id);
        contentValues.put(HOLDER_COL2, name);
        contentValues.put(HOLDER_COL3, Relation);
        contentValues.put(HOLDER_COL4, age);
        long result = db.insert(HOLDER_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int DeleteHolder(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(HOLDER_TABLE, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
    }

    public Cursor viewAllHolder(String main_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, HOLDER_COL1, HOLDER_COL2, HOLDER_COL3, HOLDER_COL4};
        Cursor res = db.query(HOLDER_TABLE, projections, "MAIN_ID = ?", new String[]{main_id}, null, null, null);
        return res;
    }

    public boolean insertSnoozeDate(int main_id, int id, int day, int month, int year, int hour, int minute, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, main_id);
        contentValues.put(SNOOZE_COL1, id);
        contentValues.put(SNOOZE_COL2, day);
        contentValues.put(SNOOZE_COL3, month);
        contentValues.put(SNOOZE_COL4, year);
        contentValues.put(SNOOZE_COL5, hour);
        contentValues.put(SNOOZE_COL6, minute);
        contentValues.put(SNOOZE_COL7, type);
        long result = db.insert(SNOOZE_DATE_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateSnoozeDate(String main_id, String id, int day, int month, int year, int hour, int minute, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SNOOZE_COL2, day);
        contentValues.put(SNOOZE_COL3, month);
        contentValues.put(SNOOZE_COL4, year);
        contentValues.put(SNOOZE_COL5, hour);
        contentValues.put(SNOOZE_COL6, minute);
        contentValues.put(SNOOZE_COL7, type);
        long result = db.update(SNOOZE_DATE_TABLE, contentValues, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteSnoozeDate(String main_id, String id, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        int x = db.delete(SNOOZE_DATE_TABLE, "ID = ? and MAIN_ID = ? and TYPE = ?", new String[]{id, main_id, type});
        if (x > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor ViewSnoozeDate(String main_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, SNOOZE_COL1, SNOOZE_COL2, SNOOZE_COL3, SNOOZE_COL4, SNOOZE_COL5, SNOOZE_COL6, SNOOZE_COL7};
        Cursor res = db.query(SNOOZE_DATE_TABLE, projections, "MAIN_ID = ?", new String[]{main_id}, null, null, null);
        return res;
    }

    public Cursor ViewSingleSnoozeDate(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, SNOOZE_COL1, SNOOZE_COL2, SNOOZE_COL3, SNOOZE_COL4, SNOOZE_COL5, SNOOZE_COL6, SNOOZE_COL7};
        Cursor res = db.query(SNOOZE_DATE_TABLE, projections, "ID = ? and MAIN_ID = ?", new String[]{id, main_id}, null, null, null);
        return res;
    }

    public boolean insertDate(int main_id, int id, int day, int month, int year, int hour, int minute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, main_id);
        contentValues.put(DATE_COL1, id);
        contentValues.put(DATE_COL2, day);
        contentValues.put(DATE_COL3, month);
        contentValues.put(DATE_COL4, year);
        contentValues.put(DATE_COL5, hour);
        contentValues.put(DATE_COL6, minute);
        long result = db.insert(DATE_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateDate(String main_id, String id, int day, int month, int year, int hour, int minute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE_COL2, day);
        contentValues.put(DATE_COL3, month);
        contentValues.put(DATE_COL4, year);
        contentValues.put(DATE_COL5, hour);
        contentValues.put(DATE_COL6, minute);
        long result = db.update(DATE_TABLE, contentValues, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int deleteDate(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DATE_TABLE, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
    }

    /*public Cursor ViewDate() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {DATE_COL1, DATE_COL2, DATE_COL3, DATE_COL4, DATE_COL5, DATE_COL6};
        Cursor res = db.query(DATE_TABLE, projections, null,null, null, null, null);
        return res;
    }*/
    public Cursor ViewDate(String main_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, DATE_COL1, DATE_COL2, DATE_COL3, DATE_COL4, DATE_COL5, DATE_COL6};
        Cursor res = db.query(DATE_TABLE, projections, "MAIN_ID = ?", new String[]{main_id}, null, null, null);
        return res;
    }

    public Cursor ViewSingleDate(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, DATE_COL1, DATE_COL2, DATE_COL3, DATE_COL4, DATE_COL5, DATE_COL6};
        Cursor res = db.query(DATE_TABLE, projections, "ID = ? and MAIN_ID = ?", new String[]{id, main_id}, null, null, null);
        return res;
    }

    public boolean insertPendingDate(int main_id, int id, int day, int month, int year, int hour, int minute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, main_id);
        contentValues.put(PENDING_COL1, id);
        contentValues.put(PENDING_COL2, day);
        contentValues.put(PENDING_COL3, month);
        contentValues.put(PENDING_COL4, year);
        contentValues.put(PENDING_COL5, hour);
        contentValues.put(PENDING_COL6, minute);
        long result = db.insert(PENDING_INSTALL_DATE_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updatePendingDate(String main_id, String id, int day, int month, int year, int hour, int minute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PENDING_COL2, day);
        contentValues.put(PENDING_COL3, month);
        contentValues.put(PENDING_COL4, year);
        contentValues.put(PENDING_COL5, hour);
        contentValues.put(PENDING_COL6, minute);
        long result = db.update(PENDING_INSTALL_DATE_TABLE, contentValues, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int deletePendingDate(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PENDING_INSTALL_DATE_TABLE, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
    }

    public Cursor viewPendingDate(String main_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, PENDING_COL1, PENDING_COL2, PENDING_COL3, PENDING_COL4, PENDING_COL5, PENDING_COL6};
        Cursor res = db.query(PENDING_INSTALL_DATE_TABLE, projections, "MAIN_ID = ?", new String[]{main_id}, null, null, null);
        return res;
    }

    public Cursor viewSinglePendingDate(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {PENDING_COL1, PENDING_COL2, PENDING_COL3, PENDING_COL4, PENDING_COL5, PENDING_COL6};
        Cursor res = db.query(PENDING_INSTALL_DATE_TABLE, projections, "ID = ? and MAIN_ID = ?", new String[]{id, main_id}, null, null, null);
        return res;
    }

    public boolean insertHistory(int main_id, int id, String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, main_id);
        contentValues.put(HIS_COL1, id);
        contentValues.put(HIS_COL2, name);
        contentValues.put(HIS_COL3, date);
        long result = db.insert(PAY_HISTORY_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor viewHistory(String main_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, HIS_COL1, HIS_COL2, HIS_COL3};
        Cursor res = db.query(PAY_HISTORY_TABLE, projections, "MAIN_ID = ?", new String[]{main_id}, null, null, null);
        return res;
    }

    public Cursor viewSingleHistory(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {HIS_COL1, HIS_COL2, HIS_COL3};
        Cursor res = db.query(PAY_HISTORY_TABLE, projections, "ID = ? and MAIN_ID = ?", new String[]{id, main_id}, null, null, null);
        return res;
    }

    public void deleteHistory(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PAY_HISTORY_TABLE, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
    }

    public void insertdefault(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_COL1, item);
        db.insert(CAT_NAME, null, contentValues);
    }

    public boolean insertcategory(int main_id, String cat_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, main_id);
        contentValues.put(up_cat_col2, cat_name);
        long result = db.insert(UPDATE_CAT_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updatecategory(String main_id, String cat_name, String cat_new_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(up_cat_col2, cat_new_name);
        db.update(UPDATE_CAT_NAME, contentValues, "CATEGORY_NAME = ? and MAIN_ID = ?", new String[]{cat_name, main_id});
        return true;
    }

    public Integer DeleteCategoty(String main_id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(UPDATE_CAT_NAME, "CATEGORY_NAME = ? and MAIN_ID = ?", new String[]{name, main_id});
    }

    public Cursor viewAllcategory(String main_id) {
        /*SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID,CAT_COL1,CAT_COL2};
        Cursor res = db.query(CAT_NAME, projections,null,null, null, null, null);*/
        //viewDefaultCategory();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, up_cat_col2};
        Cursor res = db.query(UPDATE_CAT_NAME, projections, "MAIN_ID = ?", new String[]{main_id}, null, null, null);
        return res;
    }

    public Cursor viewDefaultCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {CAT_COL1};
        Cursor res = db.query(CAT_NAME, projections, null, null, null, null, null);
        return res;
    }

    public boolean insertdata(int main_id, String name, String holder,String main_holder, String investment_plan, String policy, String sum, String start_Date, String maturity_date, String premium_interval, int amount, String premium_date, int no_installment, int tt_premium_amoount, String before_notify, String last_premium_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, main_id);
        contentValues.put(COL2, name);
        contentValues.put(COL3, holder);
        contentValues.put(COL16, main_holder);
        contentValues.put(COL4, investment_plan);
        contentValues.put(COL5, policy);
        contentValues.put(COL6, sum);
        contentValues.put(COL7, start_Date);
        contentValues.put(COL8, maturity_date);
        contentValues.put(COL9, premium_interval);
        contentValues.put(COL10, amount);
        contentValues.put(COL11, premium_date);
        contentValues.put(COL12, no_installment);
        contentValues.put(COL13, tt_premium_amoount);
        contentValues.put(COL14, before_notify);
        contentValues.put(COL15, last_premium_date);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertextra(ArrayList<String> tmp, int id) {
        arr.add(EXT_COL1);
        arr.add(EXT_COL2);
        arr.add(EXT_COL3);
        arr.add(EXT_COL4);
        arr.add(EXT_COL5);
        arr.add(EXT_COL6);
        arr.add(EXT_COL7);
        arr.add(EXT_COL8);
        arr.add(EXT_COL9);
        arr.add(EXT_COL10);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXT_COLID, id);
        int i;
        for (i = 0; i < tmp.size(); i++) {
            contentValues.put(arr.get(i), tmp.get(i));
        }
        if (tmp.size() < 10) {
            while (i < 10) {
                contentValues.put(arr.get(i), "null");
                i++;
            }
        }
        long result = db.insert(EXT_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateextra(ArrayList<String> tmp, int id) {
        arr.add(EXT_COL1);
        arr.add(EXT_COL2);
        arr.add(EXT_COL3);
        arr.add(EXT_COL4);
        arr.add(EXT_COL5);
        arr.add(EXT_COL6);
        arr.add(EXT_COL7);
        arr.add(EXT_COL8);
        arr.add(EXT_COL9);
        arr.add(EXT_COL10);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXT_COLID, id);
        int i;
        for (i = 0; i < tmp.size(); i++) {
            contentValues.put(arr.get(i), tmp.get(i));
        }
        if (tmp.size() < 10) {
            while (i < 10) {
                contentValues.put(arr.get(i), "null");
                i++;
            }
        }
        long result = db.update(EXT_TABLE, contentValues, "ID = ?", new String[]{String.valueOf(id)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int deleteextra(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(EXT_TABLE, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
    }

    public Cursor viewSingleExtra(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, EXT_COLID, EXT_COL1, EXT_COL2, EXT_COL3, EXT_COL4, EXT_COL5, EXT_COL6, EXT_COL7, EXT_COL8, EXT_COL9, EXT_COL10};
        Cursor res = db.query(EXT_TABLE, projections, "ID = ? and MAIN_ID = ?", new String[]{id, main_id}, null, null, null);
        return res;
    }

    public Cursor viewSingleData(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, COL1, COL2, COL3, COL16,COL4, COL5, COL6, COL7, COL8, COL9, COL10, COL11, COL12, COL13, COL14, COL15};
        Cursor res = db.query(TABLE_NAME, projections, "ID = ? and MAIN_ID = ?", new String[]{id, main_id}, null, null, null);
        return res;
    }

    public Cursor viewAllExtra(String main_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, EXT_COLID, EXT_COL1, EXT_COL2, EXT_COL3, EXT_COL4, EXT_COL5, EXT_COL6, EXT_COL7, EXT_COL8, EXT_COL9, EXT_COL10};
        Cursor res = db.query(EXT_TABLE, projections, "MAIN_ID = ?", new String[]{main_id}, null, null, null);
        return res;
    }

    public boolean updatedata(String main_id, String id, String name, String holder,String main_holder,String policy, String before_notify) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, holder);
        contentValues.put(COL16, main_holder);
        contentValues.put(COL5, policy);
        contentValues.put(COL14, before_notify);

        db.update(TABLE_NAME, contentValues, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
        return true;
    }

    public Cursor viewAllData(String main_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, COL1, COL2, COL3,COL16 ,COL4, COL5, COL6, COL7, COL8, COL9, COL10, COL11, COL12, COL13, COL14, COL15};
        Cursor res = db.query(TABLE_NAME, projections, "MAIN_ID = ?", new String[]{main_id}, null, null, null);
        return res;
    }

    public void DeleteData(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
        Cursor cc;
        cc = viewSingleExtra(main_id, id);
        if (cc.getCount() > 0) {
            deleteextra(main_id, id);
        }
        cc = ViewSingleDate(main_id, id);
        if (cc.getCount() > 0) {
            deleteDate(main_id, id);
        }
        cc = ViewSingleSnoozeDate(main_id, id);
        if (cc.getCount() > 0) {
            deleteSnoozeDate(main_id, id, "P");
        }
        cc = viewSinglePendingDate(main_id, id);
        if (cc.getCount() > 0) {
            deletePendingDate(main_id, id);
        }
        cc = viewSingleHistory(main_id, id);
        if (cc.getCount() > 0) {
            deleteHistory(main_id, id);
        }

        cc = ViewSingleCompletedPlan(String.valueOf(main_id), String.valueOf(id));
        if (cc.getCount() > 0) {
            deleteCompletedPlan(String.valueOf(main_id), String.valueOf(id));
        }
        cc = ViewSingleInstallMonth(String.valueOf(main_id), String.valueOf(id));
        if (cc.getCount() > 0) {
            deleteInstallMonth(String.valueOf(main_id), String.valueOf(id));
        }
        /*
        db.execSQL("DROP TABLE " + COMPLETED_NAME);
        db.execSQL("DROP TABLE " + INSTALL_MONTH_SAVE);*/

    }

    public void addUser(String email, String password, String mobile, String user_name, String mode, byte[] image, String imgstr, String fbstr, String fbid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASS, password);
        values.put(COLUMN_MOB, mobile);
        values.put(COLUMN_USER, user_name);
        values.put(COLUMN_MODE, mode);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_GOOGLE_IMAGE_STRING, imgstr);
        values.put(COLUMN_FB_IMAGE_STRING, fbstr);
        values.put(COLUMN_FB_ID, fbid);

        long id = db.insert(USER_TABLE, null, values);
        db.close();


    }

    public boolean getUser(String email, String pass) {
        //HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "select * from  " + USER_TABLE + " where " +
                COLUMN_EMAIL + " = " + "'" + email + "'" + " and " + COLUMN_PASS + " = " + "'" + pass + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return true;
        }
        cursor.close();
        db.close();

        return false;
    }

    public boolean validateUser(String email) {
        //HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "select * from  " + USER_TABLE + " where " +
                COLUMN_EMAIL + " = " + "'" + email + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return true;
        }
        cursor.close();
        db.close();

        return false;
    }

    public Cursor getUserData(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID};
        Cursor res = db.query(USER_TABLE, projections, "EMAIL = ?", new String[]{email}, null, null, null);
        return res;
    }


    public Cursor ViewAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID};
        Cursor res = db.query(USER_TABLE, projections, null, null, null, null, null);
        return res;
    }

    public Cursor ViewSingleUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, COLUMN_EMAIL, COLUMN_MOB, COLUMN_PASS, COLUMN_USER, COLUMN_MODE, COLUMN_IMAGE, COLUMN_GOOGLE_IMAGE_STRING, COLUMN_FB_IMAGE_STRING, COLUMN_FB_ID};
        Cursor res = db.query(USER_TABLE, projections, "MAIN_ID = ?", new String[]{id}, null, null, null);
        return res;
    }

    public boolean updatepic(String main_id, byte[] pic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IMAGE, pic);

        db.update(USER_TABLE, contentValues, "MAIN_ID = ?", new String[]{main_id});
        return true;
    }


    public boolean updatepicgoogle(String main_id, String pic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GOOGLE_IMAGE_STRING, pic);

        db.update(USER_TABLE, contentValues, "MAIN_ID = ?", new String[]{main_id});
        return true;
    }

    public boolean updatepicfb(String main_id, String pic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FB_IMAGE_STRING, pic);

        db.update(USER_TABLE, contentValues, "MAIN_ID = ?", new String[]{main_id});
        return true;
    }

    public boolean updatepass(String main_id, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PASS, password);

        db.update(USER_TABLE, contentValues, "MAIN_ID = ?", new String[]{main_id});
        return true;
    }

    public boolean updateusername(String main_id, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER, username);

        db.update(USER_TABLE, contentValues, "MAIN_ID = ?", new String[]{main_id});
        return true;
    }

    public boolean updatefbid(String main_id, String fid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FB_ID, fid);

        db.update(USER_TABLE, contentValues, "MAIN_ID = ?", new String[]{main_id});
        return true;
    }

    public boolean updatemode(String main_id, String mode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MODE, mode);

        db.update(USER_TABLE, contentValues, "MAIN_ID = ?", new String[]{main_id});
        return true;
    }

    public Cursor getusermainid(String fbid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID};
        Cursor res = db.query(USER_TABLE, projections, "FBID = ?", new String[]{fbid}, null, null, null);
        return res;
    }

    public boolean insertQuickReminder(int main_id, String title, int day, int month, int year, int hour, int minute, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, main_id);
        values.put(QUICK_COL2, title);
        values.put(QUICK_COL3, day);
        values.put(QUICK_COL4, month);
        values.put(QUICK_COL5, year);
        values.put(QUICK_COL6, hour);
        values.put(QUICK_COL7, minute);
        values.put(QUICK_COL8, desc);
        long result = db.insert(QUICK_NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateQuickReminder(String main_id, String id, String title, int day, int month, int year, int hour, int minute, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QUICK_COL2, title);
        values.put(QUICK_COL3, day);
        values.put(QUICK_COL4, month);
        values.put(QUICK_COL5, year);
        values.put(QUICK_COL6, hour);
        values.put(QUICK_COL7, minute);
        values.put(QUICK_COL8, desc);
        long result = db.update(QUICK_NAME, values, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int deleteQuickReminder(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(QUICK_NAME, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
        return result;
    }

    public Cursor viewSingleQuickReminer(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, QUICK_COL1, QUICK_COL2, QUICK_COL3, QUICK_COL4, QUICK_COL5, QUICK_COL6, QUICK_COL7, QUICK_COL8};
        Cursor res = db.query(QUICK_NAME, projections, "MAIN_ID = ? and ID = ?", new String[]{main_id, id}, null, null, null);
        return res;
    }

    public Cursor viewAllQuickReminder(String main_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, QUICK_COL1, QUICK_COL2, QUICK_COL3, QUICK_COL4, QUICK_COL5, QUICK_COL6, QUICK_COL7, QUICK_COL8};
        Cursor res = db.query(QUICK_NAME, projections, "MAIN_ID = ?", new String[]{main_id}, null, null, null);
        return res;
    }

    public boolean insertCompletedPlan(int main_id, int id, String name, String policy, String investment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, main_id);
        values.put(COMPLETED_COL1, id);
        values.put(COMPLETED_COL2, name);
        values.put(COMPLETED_COL3, policy);
        values.put(COMPLETED_COL4, investment);
        long result = db.insert(COMPLETED_NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int deleteCompletedPlan(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(COMPLETED_NAME, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
        return result;
    }

    public Cursor ViewCompletedPlan(String main_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, COMPLETED_COL1, COMPLETED_COL2, COMPLETED_COL3, COMPLETED_COL4};
        Cursor res = db.query(COMPLETED_NAME, projections, "MAIN_ID = ?", new String[]{main_id}, null, null, null);
        return res;
    }

    public Cursor ViewSingleCompletedPlan(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, COMPLETED_COL1, COMPLETED_COL2, COMPLETED_COL3, COMPLETED_COL4};
        Cursor res = db.query(COMPLETED_NAME, projections, "MAIN_ID = ? and ID = ?", new String[]{main_id, id}, null, null, null);
        return res;
    }

    public boolean insertInstallMonth(int main_id, int id, int count, int total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, main_id);
        values.put(INSTALL_COL1, id);
        values.put(INSTALL_COL2, count);
        values.put(INSTALL_COL3, total);
        long result = db.insert(INSTALL_MONTH_SAVE, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateInstallMonth(String main_id, String id, int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INSTALL_COL2, count);
        long result = db.update(INSTALL_MONTH_SAVE, values, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int deleteInstallMonth(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(INSTALL_MONTH_SAVE, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
        return result;
    }

    public Cursor ViewSingleInstallMonth(String main_id, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, INSTALL_COL1, INSTALL_COL2, INSTALL_COL3};
        Cursor res = db.query(INSTALL_MONTH_SAVE, projections, "MAIN_ID = ? and ID = ?", new String[]{main_id, id}, null, null, null);
        return res;
    }

    public boolean insertPendingPremium(int main_id, int id, int amount,int paid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, main_id);
        values.put(PENDING_PREMIUM_COL1, id);
        values.put(PENDING_PREMIUM_COL2, amount);
        values.put(PENDING_PREMIUM_COL3, paid);
        long result = db.insert(PENDING_PREMIUM_AMOUNT, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor viewSinglePremiumAmount(String main_id,String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COLUMN_ID, PENDING_PREMIUM_COL1, PENDING_PREMIUM_COL2,PENDING_PREMIUM_COL3};
        Cursor res = db.query(PENDING_PREMIUM_AMOUNT, projections, "MAIN_ID = ? and ID = ?", new String[]{main_id, id}, null, null, null);
        return res;
    }
    public int deletePremiumAmount(String main_id,String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(PENDING_PREMIUM_AMOUNT, "ID = ? and MAIN_ID = ?", new String[]{id, main_id});
        return result;
    }
}

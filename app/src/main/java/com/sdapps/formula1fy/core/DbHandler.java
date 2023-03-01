package com.sdapps.formula1fy.core;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbHandler extends SQLiteOpenHelper {

    private final Context context;
    private final String DB_NAME;
    private SQLiteDatabase db;
    private final String DB_UPGRADE = "Upgrade DB: ";
    private final String DB_PATH;

    public DbHandler(final Context ctx, final String dbName){
        super(ctx,dbName, null,1);
        this.context = ctx;
        this.DB_NAME = dbName;
        this.DB_PATH = context.getDatabasePath(DB_NAME).getPath();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        startUpgradingDB(newVersion);
    }

    public void insertSQL(String tableName, String columns, String content){
        final String sql = "insert into " + tableName + " (" + columns + ") values ('" + content + "')";
        Log.d("SQL", "Sql -<> " + sql);
        db.execSQL(sql);
    }

    public void startUpgradingDB(int version){
        boolean delete = context.deleteDatabase(DB_NAME);
        if(delete)
            createDB();
    }

    public void createDB(){
        final boolean isDBexist = dbCheck();
        if(isDBexist){
            getWritableDatabase();
        }
        else{
            getWritableDatabase();
            try{
                copyDB();
                DBHelper.INSTANCE.createTables(this);
                Log.w("DB","DBcreated.. ");
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }

    public void copyDB() throws IOException {

        final InputStream input = context.getAssets().open(DB_NAME);
        final OutputStream output = new FileOutputStream(DB_PATH);

        final byte[] buffer = new byte[1024];
        int length;
        while((length = input.read(buffer)) > 0 ){
            output.write(buffer,0,length);
        }

        output.flush();
        output.close();
        input.close();

    }
    public boolean dbCheck(){
        return context.getDatabasePath(DB_NAME).exists();
    }
    public void openDB() throws SQLException{
        try{
            db = SQLiteDatabase.openDatabase(DB_PATH,null,
                    SQLiteDatabase.OPEN_READWRITE);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void closeDB(){
        this.db.close();
    }

    public void exe(final String sql){
        db.execSQL(sql);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.disableWriteAheadLogging();
    }
}

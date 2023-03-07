package com.sdapps.formula1fy.core;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        final String sql = "insert into " + tableName + " (" + columns + ") values (" + content + ")";
        Log.d("SQL", "Sql -<> " + sql);
        db.execSQL(sql);
    }

    public void startUpgradingDB(int version){
        exportDB(version);
        boolean delete = context.deleteDatabase(DB_NAME);
        if(delete)
            createDB();
    }

    public void createDB(){
        final boolean isDBexist = dbCheck();
        getWritableDatabase();
        if(!isDBexist){
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
        final OutputStream output;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            output = Files.newOutputStream(Paths.get(DB_PATH));
        }else{
            output = new FileOutputStream(DB_PATH);
        }

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

    private void exportDB(int newVersion){
        String loc = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/formula1fy/";

        File file;
        file = new File(loc);
        if(!file.exists()){
            file.mkdir();
        }

        File path = new File(loc);
        if(!path.exists()){
            path.mkdir();
        }

        try{
            File currentDB = new File(DB_PATH);
            InputStream input = new FileInputStream(currentDB);
            byte[] data = new byte[input.available()];
            input.read(data);

            OutputStream out = new FileOutputStream(loc
                    + "/"
                    + "formula1fy_old"
                    + newVersion);
            out.write(data);
            out.flush();
            input.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public Cursor selectSql(final String sql){
        return db.rawQuery(sql,null);
    }
}

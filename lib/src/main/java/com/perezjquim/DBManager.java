package com.perezjquim;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DBManager
{
    private static final String TAG = "DBManager";

    private SQLiteDatabase db;

    public DBManager(String db_name)
    {
    	try
        {
              File dbFolder = new File(Environment.getExternalStorageDirectory(), "/" + db_name);
              if(!dbFolder.exists())
              {
                  if (!dbFolder.mkdir()) Log.e(TAG,"Could not create database folder");
              }

              File dbFile = new File(dbFolder, db_name);
              if(!dbFile.exists())
              {
                  if(!dbFile.createNewFile()) Log.e(TAG,"Make sure 'WRITE_EXTERNAL_STORAGE' permission is in the Manifest and it is granted");
              }

              db = SQLiteDatabase.openDatabase(
                      Environment.getExternalStorageDirectory() + "/" + db_name + "/" + db_name,
                      null,
                      SQLiteDatabase.CREATE_IF_NECESSARY);
      }
      catch(Exception e)
      { e.printStackTrace(); }
    }

    public Cursor querySelect(String s)
    {
        Log.e("-- Executing query --",s);
        return db.rawQuery(s,null);
    }

    public synchronized void query(String s)
    {
        Log.e("-- Executing query --",s);
        db.execSQL(s);
    }

    public void queryInTransaction(String ... queries)
    {
        db.beginTransaction();
        try
        {
            for(String q : queries)
            {
                query(q);
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.endTransaction();
        }
    }

    public Cursor select(String[] tables, HashMap<String,String> data)
    {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");

        sql.append(StringUtils.join(tables, ","));

        sql.append(" WHERE ");

        String column, value;

        for(Map.Entry<String, String> entry : data.entrySet())
        {
            column = entry.getKey();
            value = entry.getValue();

            sql.append(column).append("='").append(value).append("' AND ");
        }

        sql = new StringBuilder(sql.substring(0, sql.length() - 6));

        return querySelect(sql.toString());
    }

    public void insert(String table, HashMap<String,String> data)
    {
        StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");

        String[] columns = (String[]) data.keySet().toArray();
        sql.append(StringUtils.join(columns, ","));

        sql.append(") VALUES (");

        String[] values = (String[]) data.values().toArray();
        for(String v : values)
        {
            sql.append("'").append(v).append("',");
        }

        sql = new StringBuilder(sql.substring(0, sql.length() - 2));

        sql.append(")");

        query(sql.toString());
    }

    public void update(String table, HashMap<String,String> data)
    {
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");

        String column, value;

        for(Map.Entry<String, String> entry : data.entrySet())
        {
            column = entry.getKey();
            value = entry.getValue();

            sql.append(column).append("='").append(value).append("',");
        }

        sql = new StringBuilder(sql.substring(0, sql.length() - 2));

        query(sql.toString());
    }

    public void delete(String table, HashMap<String,String> data)
    {
        StringBuilder sql = new StringBuilder("DELETE FROM " + table + " WHERE ");

        String column, value;

        for(Map.Entry<String, String> entry : data.entrySet())
        {
            column = entry.getKey();
            value = entry.getValue();

            sql.append(column).append("='").append(value).append("' AND ");
        }

        sql = new StringBuilder(sql.substring(0, sql.length() - 6));

        query(sql.toString());
    }

    public void createTable(String table, Column ... columns)
    {
        String sql = "CREATE TABLE IF NOT EXISTS " + table
                + " ("
                + StringUtils.join(columns, ",")
                + " )";

        query(sql);
    }

    public void clearTable(String table)
    {
        String sql = "DELETE FROM " + table;

        query(sql);
    }
}

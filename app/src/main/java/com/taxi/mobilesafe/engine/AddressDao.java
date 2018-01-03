package com.taxi.mobilesafe.engine;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddressDao {
    private static final String tag = "AddressDao";
    public static String address = "未知号码";

    public static String getAddress(Context context,String phone){
        address = "未知号码";
        String regularExpression = "^1[3-8]\\d{9}";
        SQLiteDatabase db =
                SQLiteDatabase.openDatabase(context.getFilesDir()+"/address.db", null, SQLiteDatabase.OPEN_READONLY);
        if(phone.matches(regularExpression)){
            phone = phone.substring(0, 7);
            Cursor cursor =
                    db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);
            if(cursor.moveToNext()){
                String outkey = cursor.getString(0);
                Cursor indexCursor = db.query(
                        "data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);
                if(indexCursor.moveToNext()){
                    String location = indexCursor.getString(0);
                    Log.i(tag, "location = "+location);
                    address = location;
                }else{
                    address = "未知号码";
                }

                indexCursor.close();
            }
            cursor.close();
        }else{
            switch (phone.length()) {
                case 3:
                    address = "报警电话";
                case 4:
                    address = "模拟器";
                    break;
                case 5:
                    address = "银行号码";
                    break;
                case 7:
                    address = "本地电话";
                case 8:
                    address = "本地电话";
                    break;
                case 11:
                    String areaCode = phone.substring(1,3);
                    Cursor cursor = db.query(
                            "data2", new String[]{"location"}, "area = ?", new String[]{areaCode}, null, null, null);
                    if(cursor.moveToNext()){
                        address = cursor.getString(0);
                    }else{
                        address = "未知号码";
                    }
                    break;
                case 12:
                    String areaCode1 = phone.substring(1,4);
                    Cursor cursor1 = db.query(
                            "data2", new String[]{"location"}, "area = ?", new String[]{areaCode1}, null, null, null);
                    if(cursor1.moveToNext()){
                        address = cursor1.getString(0);
                    }else{
                        address = "未知号码";
                    }
                    break;
            }
        }

        return address;
    }
}

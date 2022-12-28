package com.example.toyfirstmobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        //create SQLllte database
        super(context, "ToyDatabase.db", null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {
            //create admin table primary key is adminID, with columns adminID, adminName, adminPassword, account type (admin or user)
//            sqLiteDatabase.execSQL("Create Table IF NOT EXISTS Admin(adminID INTEGER PRIMARY KEY AUTOINCREMENT, adminName TEXT(20) NOT NULL, adminPassword TEXT(10) NOT NULL, isAdmin INTEGER DEFAULT 1)");

            //create user table primary key is userID, with columns userID, userName, userPassword, account type (admin or user), Name, Address and Phone Number
            sqLiteDatabase.execSQL("Create Table IF NOT EXISTS User(userID INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT(40) UNIQUE, userName TEXT(20) NOT NULL UNIQUE, userPassword TEXT(10) NOT NULL, isAdmin INTEGER DEFAULT 0, name TEXT(20) NOT NULL, address TEXT(20) NOT NULL, phone TEXT(15) NOT NULL)");

            //create toy category table primary key is categoryID, with columns categoryID, categoryName
            sqLiteDatabase.execSQL("Create Table IF NOT EXISTS ToyCategory(categoryID INTEGER PRIMARY KEY AUTOINCREMENT, categoryName TEXT(20) NOT NULL UNIQUE)");

            //create toy table primary key is toyID, with columns toyID, toyName, toyPrice, toyQuantity, toyCategory as foreign key from category table, toyImage
            sqLiteDatabase.execSQL("Create Table IF NOT EXISTS Toy(toyID INTEGER PRIMARY KEY AUTOINCREMENT, toyName TEXT(20) NOT NULL, toyPrice REAL(6) NOT NULL, toyQuantity INTEGER(4) NOT NULL, toyCategory INTEGER(4) NOT NULL, toyImage BLOB, FOREIGN KEY (toyCategory) REFERENCES ToyCategory(categoryID) ON DELETE CASCADE ON UPDATE CASCADE ) ");

            //create order table  with columns orderID auto increment for different username, username, toyId as foreign key from toy table, orderStatus, orderQuantity, orderDate
            sqLiteDatabase.execSQL("Create Table IF NOT EXISTS Orders(orderID INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT(20) NOT NULL, toyID INTEGER(4) NOT NULL, orderStatus TEXT(20) NOT NULL, orderQuantity INTEGER(4) NOT NULL, orderDate TEXT(20) NOT NULL, FOREIGN KEY (toyID) REFERENCES Toy(toyID) ON DELETE CASCADE ON UPDATE CASCADE ) ");

        }

        catch(Exception e){
            e.printStackTrace();
        }
        }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    //insert data into user table
    public boolean insertUserData(String email, String userName, String userPassword, boolean isAdmin, String Name, String Address, String Phone){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int isAdm = (isAdmin==true)?1:0;
        contentValues.put("email",email);
        contentValues.put("userName",userName);
        contentValues.put("userPassword",userPassword);
        contentValues.put("isAdmin",isAdm);
        contentValues.put("Name",Name);
        contentValues.put("address",Address);
        contentValues.put("phone",Phone);
        long result =DB.insert("User",null,contentValues);
        return result != 1;
    }

    //insert data into toy category table
    public boolean insertToyCategoryData(int categoryID, String categoryName){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("categoryID",categoryID);
        contentValues.put("categoryName",categoryName);
        long result =DB.insert("ToyCategory",null,contentValues);
        return result != 1;
    }

    //insert data into toy category table
    public boolean insertToyCategoryData(String categoryName){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("categoryName",categoryName);
        long result =DB.insert("ToyCategory",null,contentValues);
        return result != 1;
    }



    //insert data into toy table
    public boolean insertToyData( String toyName, float toyPrice, int toyQuantity, int toyCategory, byte[] toyImage){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("toyName",toyName);
        contentValues.put("toyPrice",toyPrice);
        contentValues.put("toyQuantity",toyQuantity);
        contentValues.put("toyCategory",toyCategory);
        contentValues.put("toyImage",toyImage);
        long result =DB.insert("Toy",null,contentValues);
        return result != 1;
    }

    //update category table
    public boolean updateToyCategoryData(int categoryID, String categoryName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put("categoryID", categoryID);
        contentValues.put("categoryName", categoryName);
        Cursor cursor = DB.rawQuery("Select * from ToyCategory where categoryID = ?", new String[]{String.valueOf(categoryID)});
        if (cursor.getCount() > 0) {
            long result = DB.update("ToyCategory", contentValues, "categoryID = ?", new String[]{String.valueOf(categoryID)});
            return result != 1;
        } else {
            return false;
        }
    }

    //delete category
    public boolean deleteToyCategoryData(int categoryID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from ToyCategory where categoryID = ?", new String[]{String.valueOf(categoryID)});
        if (cursor.getCount() > 0) {
            long result = DB.delete("ToyCategory", "categoryID = ?", new String[]{String.valueOf(categoryID)});
            if (result==1)
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    //retrieve data from category table
    public Cursor getToyCategoryData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from ToyCategory",null);
        return cursor;
    }

    //return category name when input category id
    public String categoryName(int id){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from ToyCategory where categoryID = ?",new String[]{String.valueOf(id)});
        if (cursor.moveToFirst())
            return cursor.getString(1);
        else
            return null;
    }
    //return category id when input category name
    public int categoryId(String name){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from ToyCategory where categoryName = ?",new String[]{name});
        if (cursor.moveToFirst())
            return cursor.getInt(0);
        else
            return -1;
    }

    //update toy table
    public boolean updateToyData(int toyID, String toyName, float toyPrice, int toyQuantity, int toyCategory, byte[] toyImage) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("toyID", toyID);
        contentValues.put("toyName", toyName);
        contentValues.put("toyPrice", toyPrice);
        contentValues.put("toyQuantity", toyQuantity);
        contentValues.put("toyCategory", toyCategory);
        contentValues.put("toyImage", toyImage);
        Cursor cursor = DB.rawQuery("Select * from Toy where toyID = ?", new String[]{String.valueOf(toyID)});
        if (cursor.getCount() > 0) {
            long result = DB.update("Toy", contentValues, "toyID = ?", new String[]{String.valueOf(toyID)});
            return result != 1;
        } else {
            return false;
        }
    }

    //delete toy
    public boolean deleteToyData(int toyID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Toy where toyID = ?", new String[]{String.valueOf(toyID)});
        if (cursor.getCount() > 0) {
            long result = DB.delete("Toy", "toyID = ?", new String[]{String.valueOf(toyID)});
            return result != 1;
        } else {
            return false;
        }
    }

    //retrieve data from toy table
    public Cursor getToyData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Toy",null);
        return cursor;
    }

    //retrieve user data by email
    public Cursor getUserDataByEmail(String email){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from User where email = ?", new String[]{email});
        return cursor;
    }

    public Cursor getToyDataOnCategory(int category){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor;
        if(category == -1){
            cursor = DB.rawQuery("Select * from Toy",null);
        }else {
            cursor = DB.rawQuery("Select * from Toy where toyCategory = ?",new String[]{String.valueOf(category)});
        }

        return cursor;
    }

    //add order
    public boolean addOrder(String userName, int toyID, String orderStatus, int orderQuantity, String orderDate){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put("orderID",orderID);
        contentValues.put("userName",userName);
        contentValues.put("toyID",toyID);
        contentValues.put("orderStatus",orderStatus);
        contentValues.put("orderQuantity",orderQuantity);
        contentValues.put("orderDate",orderDate);
        long result =DB.insert("Orders",null,contentValues);
        return result != 1;
    }

}







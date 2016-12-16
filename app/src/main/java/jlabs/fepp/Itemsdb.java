package jlabs.fepp;

//Created by pradeep kumar (Jussconnect)

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Itemsdb extends SQLiteOpenHelper {

	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Itemsdb";
    Context context;

	public Itemsdb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }

	@Override
	public void onCreate(SQLiteDatabase db) {
 		String CREATE_LocalED_TABLE = "CREATE TABLE Items ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"companyId TEXT, "+
				"Shelf_life INTEGER, "+
				"Item_code TEXT,"+
                "Barcode TEXT,"+
                "Item_name TEXT,"+
                "Price INTEGER,"+
                "Weight TEXT,"+
                "Image TEXT,"+
				"Desc TEXT);";

 		db.execSQL(CREATE_LocalED_TABLE);

        String CREATE_Sync_Fe_list_TABLE = "CREATE TABLE Fe_list ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "fe_id TEXT, "+
                "name TEXT, "+
                "contact TEXT, "+
                "email TEXT, "+
                "add_by TEXT, "+
                "company_id TEXT ); ";
        db.execSQL( CREATE_Sync_Fe_list_TABLE);

    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Items");
        db.execSQL("DROP TABLE IF EXISTS Fe_list");

        this.onCreate(db);
	}
	//---------------------------------------------------------------------

	/**
     * CRUD operations (create "add", read "get", update, delete)
     */

    private static final String TABLE_Items = "Items";

    private static final String KEY_ID = "id";
    private static final String KEY_companyId = "companyId";
    private static final String KEY_Shelf_life = "Shelf_life";
    private static final String KEY_Item_code = "Item_code";
    private static final String KEY_Barcode = "Barcode";
    private static final String KEY_Item_name = "Item_name";
    private static final String KEY_Price = "Price";
    private static final String KEY_Weight = "Weight";
    private static final String KEY_Desc = "Desc";
    private static final String KEY_Image ="Image";


    private static final String TABLE_FEList = "Fe_list";

    private static final String KEY_Name = "name";
    private static final String KEY_Fe_id = "fe_id";
    private static final String KEY_Contact = "contact";
    private static final String KEY_Email = "email";
    private static final String KEY_Add_by = "add_by";
    private static final String KEY_Company_id = "company_id";


    public void addToSyncItems(JSONObject tp){

        try {
            JSONArray ja=tp.getJSONArray("img");

            String a = null;

            for (int i =0; i< ja.length(); i++ ){

                a =ja.getString(i);
            }

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_companyId, tp.getString("company_id"));
            values.put(KEY_Shelf_life, tp.getInt("shelf_life"));
            values.put(KEY_Item_code, tp.getString("item_id"));

            values.put(KEY_Barcode, tp.getString("barcode"));
            values.put(KEY_Item_name, tp.getString("product_name"));
            values.put(KEY_Price, tp.getInt("price"));
            values.put(KEY_Weight, tp.getString("weight"));
            values.put(KEY_Desc, tp.getString("detail"));

            values.put(KEY_Image,a);
            db.insert(TABLE_Items, null, values);
            values.clear();

            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void addListOfFe(JSONObject obj){


        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_Fe_id, obj.getString("fe_id"));
            values.put(KEY_Name, obj.getString("name"));
            values.put(KEY_Contact, obj.getString("contact"));
            values.put(KEY_Email, obj.getString("email_id"));
            values.put(KEY_Add_by, obj.getString("add_by"));
            values.put(KEY_Company_id, obj.getString("company_id"));

            db.insert(TABLE_FEList, null, values);
            values.clear();

            db.close();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Class_Fe> getAllFE_List(){


        ArrayList<Class_Fe> fe_list = new ArrayList<>();
        String query = " SELECT * FROM " + TABLE_FEList + " ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();

        Class_Fe tp = null ;
        if (cursor.moveToFirst()) {
            do {
                tp = new Class_Fe();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.FE_id = (cursor.getString(1));
                tp.Name = cursor.getString(2);
                tp.Contact = cursor.getString(3) ;
                tp.Email =cursor.getString(4);
                tp.Add_by=cursor.getString(5);
                tp.Company_id= cursor.getString(6);

                fe_list.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return fe_list;
    }



    public ArrayList<Class_Items> getAllItems() {
    	ArrayList<Class_Items> Items = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Items + " ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {
        	
        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Items tp = null;
        if (cursor.moveToFirst()) {
            do {
            	tp = new Class_Items();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.companyId= (cursor.getString(1));
                tp.Shelf_life=(Integer.parseInt(cursor.getString(2)));
                tp.Item_code=cursor.getString(3);

                tp.Barcode=cursor.getString(4);
                tp.Item_name=cursor.getString(5);
                tp.price=(Integer.parseInt(cursor.getString(6)));
                tp.Weight=cursor.getString(7);
                tp.Image=cursor.getString(8);
                tp.Desc=cursor.getString(9);

                Items.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return Items;
    }

    public ArrayList<Class_Items> getAllItemsByBarcode(String barcode) {
        ArrayList<Class_Items> Items = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Items + " where "+KEY_Barcode+" like '"+barcode + "' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Items tp = null;
        if (cursor.moveToFirst()) {
            do {
                tp = new Class_Items();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.companyId=(cursor.getString(1));
                tp.Shelf_life=(Integer.parseInt(cursor.getString(2)));
                tp.Item_code=cursor.getString(3);

                tp.Barcode=cursor.getString(4);
                tp.Item_name=cursor.getString(5);
                tp.price=(Integer.parseInt(cursor.getString(6)));
                tp.Weight=cursor.getString(7);
                tp.Image = cursor.getString(cursor.getColumnIndex("Image"));
                tp.Desc=cursor.getString(9);

                Log.i("TAGMAN :"," hope "+ tp.Image);
                Items.add(tp);
                Log.i("TAGMAN :"," heyyy"+ Items);
            } while (cursor.moveToNext());
        }
        db.close();
        return Items;
    }

    public int getItemShelfLife(String itemcode)
    {
        int x=0;
        String query = "SELECT  "+KEY_Shelf_life+" FROM " + TABLE_Items + " where "+KEY_Item_code+" like '"+itemcode + "' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            x=Integer.parseInt(cursor.getString(0));
        }
        db.close();
        return x;
    }


    public String getItemImage(String itemcode)
    {
       String x= "";
        String query = "SELECT  "+KEY_Image+" FROM " + TABLE_Items + " where "+KEY_Item_code+" like '"+itemcode + "' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            x=(cursor.getString(0));
        }
        db.close();
        return x;
    }

    public void deleteItems()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Items, null, null);
        db.close();
    }

    public void delete_FE_list(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FEList, null, null);
        db.close();
    }

    public void delete_fe_ById(int idd){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FEList + " WHERE "+ KEY_Fe_id +"='"+idd+"'");
        db.close();
    }
}

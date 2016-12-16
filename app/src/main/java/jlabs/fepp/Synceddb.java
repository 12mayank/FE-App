package jlabs.fepp;

//Created by pradeep kumar (Jussconnect)

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Synceddb extends SQLiteOpenHelper {

	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Synceddb";
    Context context;

	public Synceddb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }

	@Override
	public void onCreate(SQLiteDatabase db) {
 		String CREATE_SyncED_TABLE = "CREATE TABLE Synced_offer ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "offer_id TEXT, " +
                "item_code TEXT, "+
                "dom TEXT,"+
                "deliverable INTEGER, "+
                "points INTEGER, "+
                "approved INTEGER, "+
                 "name TEXT, "+
                 "remaining_codes INTEGER, "+
                 "vendor_id TEXT);";

 		db.execSQL(CREATE_SyncED_TABLE);

        String CREATE_Sync_Stores_TABLE = "CREATE TABLE Synced_stores ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Store_id TEXT, " +
                "title TEXT, "+
                "Address TEXT ); " ;


        db.execSQL(CREATE_Sync_Stores_TABLE);

        String CREATE_Sync_Store_Offer_TABLE = "CREATE TABLE Synced_offer_store ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "offer_id TEXT, " +
                "Store_id TEXT);";

        db.execSQL(CREATE_Sync_Store_Offer_TABLE);



    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Synced_offer");
        db.execSQL("DROP TABLE IF EXISTS Synced_stores");
        db.execSQL("DROP TABLE IF EXISTS Synced_offer_store");


        this.onCreate(db);
	}
	//---------------------------------------------------------------------

	/**
     * CRUD operations (create "add", read "get", update, delete)
     */

    private static final String TABLE_Sync_Offer = "Synced_offer";
    private static final String TABLE_Sync_Store = "Synced_stores";
    private static final String TABLE_Sync_Offer_Store = "Synced_offer_store";

    private static final String KEY_ID = "id";
    private static final String KEY_OFFER_ID = "offer_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ITEM_CODE = "item_code";
    private static final String KEY_DOM = "dom";
    private static final String KEY_POINT = "points";
    private static final String KEY_DELIV = "deliverable";
    private static final String KEY_STORE_ID = "Store_id";
    private static final String KEY_STORE_ADDR = "Address";
    private static final String KEY_MYSTORE = "MyStore";
    private static final String KEY_Approved = "approved";
    private static final String KEY_NAME ="name";
    private static final String KEY_REMAINING ="remaining_codes";
    private static final String KEY_Vendor_id ="vendor_id";

    public void addToSyncOffer(JSONObject tp){

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_OFFER_ID, tp.getString("offer_id"));
            values.put(KEY_ITEM_CODE, tp.getString("item_id"));
            values.put(KEY_DOM, tp.getString("dom"));
            values.put(KEY_POINT, tp.getInt("cashback"));
            values.put(KEY_DELIV,0);

            values.put(KEY_Approved,tp.getBoolean("approved")?1:0);
            values.put(KEY_NAME,tp.getString("retailer_name"));
            values.put(KEY_REMAINING,tp.getInt("remaining_codes"));
            values.put(KEY_Vendor_id,tp.getString("retailer_id"));

            db.insert(TABLE_Sync_Offer, null, values);
            values.clear();

            db.close();
        //  addToSyncOfferStore(tp.getString("offer_id"),tp.getJSONArray("retailer_id"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




    public ArrayList<Class_Offers> getStoreNameSync(){


        ArrayList<Class_Offers> SyncedOffer = new ArrayList<>();

        String query = " SELECT * FROM "+ TABLE_Sync_Offer + " GROUP BY " + KEY_NAME;


        Log.i("mine","check querrry :"+ query);

        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }

        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
       Class_Offers tp = null;


        if (cursor.moveToFirst()) {
            do {
                tp = new Class_Offers();

                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.offer_id=(cursor.getString(1));
                tp.item_code=cursor.getString(2);
                tp.dom=cursor.getString(3);
                tp.deliverable=(Integer.parseInt(cursor.getString(4)));
                tp.point=(Integer.parseInt(cursor.getString(5)));
                tp.approved=(cursor.getString(6).equals("1"))? true:false;
                tp.name=cursor.getString(7);
                tp.remaining_codes =(Integer.parseInt(cursor.getString(8)));
                Log.i("Myapp","heloo "+tp.approved+" offer id "+tp.offer_id);
                SyncedOffer.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return SyncedOffer;
    }


    public ArrayList<Class_Offers> getStoreWiseSyncOffers(String pp){


        ArrayList<Class_Offers> SyncedOffer = new ArrayList<>();

        String query = " SELECT * FROM "+ TABLE_Sync_Offer +" WHERE "+ KEY_NAME + " like '%"+pp+ "%'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }

        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Offers tp = null;


        if (cursor.moveToFirst()) {
            do {
                tp = new Class_Offers();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.offer_id=(cursor.getString(1));
                tp.item_code=cursor.getString(2);
                tp.dom=cursor.getString(3);
                tp.deliverable=(Integer.parseInt(cursor.getString(4)));
                tp.point=(Integer.parseInt(cursor.getString(5)));
                tp.approved=(cursor.getString(6).equals("1"))? true:false;
                tp.name=cursor.getString(7);
                tp.remaining_codes =(Integer.parseInt(cursor.getString(8)));
                Log.i("Myapp","heloo "+tp.approved+" offer id "+tp.offer_id);
                SyncedOffer.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return SyncedOffer;

    }

    public ArrayList<Class_Offers> getAllSyncOffer() {
    	ArrayList<Class_Offers> SyncedOffer = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Sync_Offer + " ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {
        	
        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Offers tp = null;
        if (cursor.moveToFirst()) {
            do {
            	tp = new Class_Offers();
                tp.id=(Integer.parseInt(cursor.getString(0)));
            	tp.offer_id=(cursor.getString(1));
                tp.item_code=cursor.getString(2);
                tp.dom=cursor.getString(3);
                tp.deliverable=(Integer.parseInt(cursor.getString(4)));
                tp.point=(Integer.parseInt(cursor.getString(5)));
                tp.approved=(cursor.getString(6).equals("1"))? true:false;
                tp.name=cursor.getString(7);
                tp.remaining_codes =(Integer.parseInt(cursor.getString(8)));
                tp.vendor_id = (cursor.getString(9));

                Log.i("Myapp","heloo "+tp.approved+" offer id "+tp.offer_id);
                SyncedOffer.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return SyncedOffer;
    }


    public void addToSyncStore(JSONObject tp){

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_STORE_ID, tp.getString("retailer_id"));
            values.put(KEY_TITLE, tp.getString("name"));
           // values.put(KEY_STORE_ADDR, tp.getJSONObject("address").getString("address"));
            values.put(KEY_STORE_ADDR, tp.getString("address"));
           // values.put(KEY_MYSTORE,  mystore?1:0);
            db.insert(TABLE_Sync_Store, null, values);
            values.clear();

            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Class_Store> getAllSyncStore() {
        ArrayList<Class_Store> SyncedStore = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Sync_Store + " ORDER BY id ";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Store tp = null;
        if (cursor.moveToFirst()) {
            do {
                tp = new Class_Store();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.store_id=(cursor.getString(1));
                tp.title=cursor.getString(2);
                tp.address=cursor.getString(3);
               // tp.mystore=(Integer.parseInt(cursor.getString(4)))>0?true:false;
              //  Log.i("Myapp","Mystore "+cursor.getString(4));
                SyncedStore.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return SyncedStore;
    }

    public void addToSyncOfferStore(String offer_id,JSONArray tp){
        String k;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            for(int i=0;i<tp.length();i++)
            {
                k=tp.getString(i);
                values.put(KEY_OFFER_ID, offer_id);
                values.put(KEY_STORE_ID, k);
                db.insert(TABLE_Sync_Offer_Store, null, values);
                values.clear();
            }
            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Class_Store> getAllSyncStoreFromOffer(String offer) {
        ArrayList<Class_Store> stores = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Sync_Offer_Store + " where "+KEY_OFFER_ID+" like '"+offer+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Store tp = null;
        if (cursor.moveToFirst()) {
            do {
                Log.i("Myapp","a "+ cursor.getString(2));
                tp=getStoreInfo (cursor.getString(2));
                if(tp!=null)
                stores.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return stores;
    }

    public Class_Store getStoreInfo(String store) {
        String query = "SELECT  * FROM " + TABLE_Sync_Store + " where "+KEY_STORE_ID+" like '"+store+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
               Class_Store tp = null;
        if (cursor.moveToFirst()) {
                tp = new Class_Store();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.store_id= cursor.getString(1);
                tp.title=cursor.getString(2);
                tp.address=cursor.getString(3);
               // tp.mystore=(Integer.parseInt(cursor.getString(4)))>0?true:false;
            Log.i("Myapp","b "+cursor.getString(2));
        }
        db.close();
        return tp;
    }

      // Last
//    public ArrayList<Class_Offers> getAllSyncOfferFromStore(String store) {
//        Log.i("Myapp","d "+store);
//        ArrayList<Class_Offers> offers = new ArrayList<>();
//        String query = "SELECT  * FROM " + TABLE_Sync_Offer_Store + " where "+KEY_STORE_ID+" like '"+store+"' ORDER BY id";
//        SQLiteDatabase db = this.getWritableDatabase();
//        while(db.inTransaction())
//        {
//
//        }
//        db.beginTransaction();
//        Cursor cursor = db.rawQuery(query, null);
//        db.endTransaction();
//        Class_Offers tp = null;
//        if (cursor.moveToFirst()) {
//            do {
//                Log.i("Myapp","a "+Integer.parseInt(cursor.getString(1)));
//                tp=getOfferInfo(Integer.parseInt(cursor.getString(1)));
//                if(tp!=null)
//                    offers.add(tp);
//            } while (cursor.moveToNext());
//        }
//        db.close();
//        return offers;
//    }


    public Class_Offers getOfferInfo(String offer) {
        String query = "SELECT  * FROM " + TABLE_Sync_Offer + " where "+KEY_OFFER_ID+" like '"+offer+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Offers tp = null;
        if (cursor.moveToFirst()) {
            tp = new Class_Offers();
            tp.id=(Integer.parseInt(cursor.getString(0)));
            tp.offer_id= cursor.getString(1);
            tp.item_code= cursor.getString(2);
            tp.dom= cursor.getString(3);
            tp.deliverable=(Integer.parseInt(cursor.getString(4)));
            tp.point=(Integer.parseInt(cursor.getString(5)));
            tp.approved= (cursor.getString(6)=="1")? true:false;
            tp.name =  cursor.getString(7);
            tp.remaining_codes= (Integer.parseInt(cursor.getString(8)));
            tp.vendor_id = cursor.getString(9);

        }
        db.close();
        return tp;
    }


    public Boolean CheckForStoreFromOfferCombination(String offer,String storeid) {
        Boolean found=false;
        String query = "SELECT  * FROM " + TABLE_Sync_Offer_Store + " where "+KEY_OFFER_ID+" like '"+offer+"' AND "+KEY_STORE_ID+" like '"+storeid+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Store tp = null;
        if (cursor.moveToFirst()) {
                found=true;
        }
        db.close();
        return found;
    }

    public String CheckFor_ItemCode_MFG_Points(String ItemCode,String MFG,String vendor)
    {
       // int id=-1;
        String id = null ;

        String query = "SELECT  * FROM " + TABLE_Sync_Offer + " where "+KEY_ITEM_CODE+" like '"+ItemCode+"' AND "+KEY_DOM+" like '"+MFG+"' AND "+KEY_Vendor_id+" like '"+vendor+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Store tp = null;
        if (cursor.moveToFirst()) {

            id = cursor.getString(1);

        }
        db.close();
        return id;
    }


    public  String CheckFor_ItemCode_storeId(String item_code, String store_id){

        String id = null ;

        String query = "SELECT  * FROM " + TABLE_Sync_Offer + " where "+KEY_ITEM_CODE+" like '"+item_code+"' AND "+KEY_Vendor_id+" like '"+store_id+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Store tp = null;
        if (cursor.moveToFirst()) {
            //   id =(Integer.parseInt(cursor.getString(1)));
            id = cursor.getString(1);

        }
        db.close();
        return id;

    }



    public ArrayList<Class_Offers> CheckFor_ItemCode_MFG(String ItemCode,String MFG,String store_id)
    {
        Log.i("TAG","detail"+ItemCode + MFG + store_id);

        ArrayList<Class_Offers> Offers = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Sync_Offer + " where "+KEY_ITEM_CODE+" like '"+ItemCode+"' AND "+KEY_DOM+" like '"+MFG+"' AND "+KEY_Vendor_id+" like '"+store_id+"' ORDER BY id";
        Log.i("TAG","query"+ query);
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Offers tp = null;
        if (cursor.moveToFirst()) {
            tp=new Class_Offers();
            tp.id=(Integer.parseInt(cursor.getString(0)));
            tp.offer_id=cursor.getString(1);
            tp.item_code=cursor.getString(2);
            tp.dom=cursor.getString(3);
             tp.deliverable=(Integer.parseInt(cursor.getString(4)));
            tp.point=(Integer.parseInt(cursor.getString(5)));
            tp.approved= (cursor.getString(6)=="1")?true:false;
            tp.name = cursor.getString(7);
            tp.remaining_codes= (Integer.parseInt(cursor.getString(8)));
            Offers.add(tp);
            Log.i("TAG","checkSameOffer"+Offers);
        }
        db.close();
        return Offers;
    }


    public void addApproved_In_Offer(String offer_id){

        int approved = 1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_Approved, approved);

        db.update(TABLE_Sync_Offer, values, KEY_OFFER_ID+" like '"+offer_id+"'", null);
    }

    public void deleteOffer()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Sync_Offer, null, null);
        db.delete(TABLE_Sync_Offer_Store, null, null);
        db.close();
    }

    public void deleteStore()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Sync_Store, null, null);
        db.close();
    }

    //delete offer from viewfragment
    public void deleteRow(String Offer)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Sync_Offer+ " WHERE "+KEY_OFFER_ID+" like '"+Offer+"'");
       // db.execSQL("DELETE FROM " + TABLE_Sync_Offer+ " WHERE "+KEY_OFFER_ID +"='"+Offer+"' AND "+KEY_STORE_ID+"="+storeID);
        db.close();
    }



}

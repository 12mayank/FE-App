package jlabs.fepp;

//Created by pradeep kumar (Jussconnect)

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class localdb extends SQLiteOpenHelper {

	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Localdb";
    Context context;

	public localdb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }

	@Override
	public void onCreate(SQLiteDatabase db) {
 		String CREATE_LocalED_TABLE = "CREATE TABLE Local_offer ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"item_code TEXT, "+
				"dom TEXT,"+
                "points INTEGER," +
                 "approved INTEGER, "+
                 "name TEXT, "+
                  "remaining_codes INTEGER, "+
                  "vendor_id TEXT);";

 		db.execSQL(CREATE_LocalED_TABLE);

        String CREATE_Local_Store_Offer_TABLE = "CREATE TABLE Local_offer_store ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "offer_id TEXT, " +
                "Store_id TEXT);";

        db.execSQL(CREATE_Local_Store_Offer_TABLE);

        String CREATE_Local_Local_Store_Offer_TABLE = "CREATE TABLE Local_Local_offer_store ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "offer_id TEXT, " +
                "Store_id TEXT);";

        db.execSQL(CREATE_Local_Local_Store_Offer_TABLE);

        String CREATE_Local_Store_Offer_forBarcode_TABLE = "CREATE TABLE Local_offer_store_forBarcode ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "offer_id TEXT, " +
                "Store_id TEXT, "+
                "Barcode TEXT);";

        db.execSQL(CREATE_Local_Store_Offer_forBarcode_TABLE);

        String CREATE_Local_Local_Store_Offer_forBarcode_TABLE = "CREATE TABLE Local_Local_offer_store_forBarcode ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "offer_id TEXT, " +
                "Store_id TEXT, "+
                "Barcode TEXT);";

        db.execSQL(CREATE_Local_Local_Store_Offer_forBarcode_TABLE);


        String CREATE_Local_Store_forDom_TABLE = "CREATE TABLE Local_offer_store_forDom ( " +
                                  "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                   "offer_id TEXT,  " +
                                    "Store_id TEXT, "+
                                   "item_code TEXT, "+
                                   "dom TEXT," +
                                   "points INTEGER);";

        db.execSQL(CREATE_Local_Store_forDom_TABLE);

    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Local_offer");
        db.execSQL("DROP TABLE IF EXISTS Local_stores");
        db.execSQL("DROP TABLE IF EXISTS Local_offer_store");
        db.execSQL("DROP TABLE IF EXISTS Local_Local_offer_store");
        db.execSQL("DROP TABLE IF EXISTS Local_offer_store_forBarcode");
        db.execSQL("DROP TABLE IF EXISTS Local_Local_offer_store_forBarcode");
        db.execSQL("DROP TABLE IF EXISTS Local_offer_store_forDom");

        this.onCreate(db);
	}
	//---------------------------------------------------------------------

	/**
     * CRUD operations (create "add", read "get", update, delete)
     */

    private static final String TABLE_Local_Offer = "Local_offer";
    private static final String TABLE__Local_offer_store_forDom = "Local_offer_store_forDom";
    private static final String TABLE_Local_Store = "Local_stores";
    private static final String TABLE_Local_Offer_Store = "Local_offer_store";
    private static final String TABLE_Local_Offer_Store_LocalOffer = "Local_Local_offer_store";
    private static final String TABLE_Local_Offer_Store_forBarcode = "Local_offer_store_forBarcode";
    private static final String TABLE_Local_Offer_Store_LocalOffer_forBarcode = "Local_Local_offer_store_forBarcode";

    private static final String KEY_ID = "id";
    private static final String KEY_ITEM_CODE = "item_code";
    private static final String KEY_DOM = "dom";
    private static final String KEY_POINT = "points";
    private static final String KEY_OFFER_ID = "offer_id";
    private static final String KEY_STORE_ID = "Store_id";
    private static final String KEY_DELIV = "deliverable";
    private static final String KEY_BARCODE = "Barcode";
    private static final String KEY_STORE_ADDR = "Address";
    private static final String KEY_Approved = "approved";
    private static final String KEY_NAME ="name";
   private static final String KEY_REMAINING ="remaining_codes";
    private static final String KEY_Vendor_id ="vendor_id";

    public String addToLocal(String store_id,Class_Offers tp){
            //long offer_id=-1;
          String offer_id = null ;

            Log.d("TAG","hi this is mine id :"+store_id);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_CODE, tp.item_code);
            values.put(KEY_DOM, tp.dom);

            values.put(KEY_POINT, tp.point);
            values.put(KEY_Approved, tp.approved);
            values.put(KEY_NAME,tp.name);
            values.put(KEY_REMAINING,tp.remaining_codes);
           // values.put(KEY_Vendor_id,tp.vendor_id);
            values.put(KEY_Vendor_id,store_id);
            offer_id= String.valueOf(db.insert(TABLE_Local_Offer, null, values));
            Log.i("Myapp","Pradeep checks "+offer_id);
            Log.d("TAG","hi this is name..."+tp.name);
            Log.d("TAG","hi this is vendor..."+tp.vendor_id);
            values.clear();
            if(offer_id!= null)
                addToLocalOfferStore_OfferIsNew(offer_id,store_id);
            db.close();
            return offer_id;
    }
    public ArrayList<Class_Offers> getAllLocalOffer() {
    	ArrayList<Class_Offers> LocalOffer = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_Local_Offer + " ORDER BY id";
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
                tp.item_code=cursor.getString(1);
                tp.dom=cursor.getString(2);
                tp.point=(Integer.parseInt(cursor.getString(3)));
                tp.IsOfferLocal=true;
                tp.approved= false;
                tp.name= cursor.getString(5);

                ArrayList<String> x=getAllLocalStoreFromLocalOffer(tp.id);
                Log.i("dsakjfhkjdsahfj"," asdf "+tp.id+"    "+x.get(0));
                tp.remaining_codes=getNoAllBarcodeForLocal(tp.id, x.get(0));


//                tp.remaining_codes = (Integer.parseInt(cursor.getString(6)));
                Log.i("dsakjfhkjdsahfj"," asdf remain "+tp.remaining_codes);
                tp.vendor_id= cursor.getString(7);
                LocalOffer.add(0,tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return LocalOffer;
    }





    public ArrayList<Class_Offers> getStoreWiseLocalOffers(String pp) {
        ArrayList<Class_Offers> LocalOffer = new ArrayList<>();
        String query = " SELECT * FROM "+ TABLE_Local_Offer +" WHERE "+ KEY_NAME + " like '%"+pp+ "%'" ;
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
                tp.item_code=cursor.getString(1);
                tp.dom=cursor.getString(2);
                tp.point=(Integer.parseInt(cursor.getString(3)));
                tp.IsOfferLocal=true;
                tp.approved= true;
                tp.name= cursor.getString(5);

                ArrayList<String> x=getAllLocalStoreFromLocalOffer(tp.id);
                tp.remaining_codes=getNoAllBarcodeForLocal(tp.id, x.get(0));
              //  tp.remaining_codes = (Integer.parseInt(cursor.getString(7)));
                LocalOffer.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return LocalOffer;
    }





    //use this if offer is synced but add new store
    public void addToLocalOfferStore(String offer_id,String store_id){
        Log.i("Myapp", "addToLocalOfferStore " + offer_id + " " + store_id);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_OFFER_ID, offer_id);
            values.put(KEY_STORE_ID, store_id);
            db.insert(TABLE_Local_Offer_Store, null, values);
            values.clear();
            db.close();
    }

    //use this if offer is locally created
    public void addToLocalOfferStore_OfferIsNew(String offer_id,String store_id ){
        Log.i("Myapp","addToLocalOfferStore_OfferIsNew "+offer_id+" "+store_id);


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OFFER_ID, offer_id);
        values.put(KEY_STORE_ID, store_id);
        db.insert(TABLE_Local_Offer_Store_LocalOffer, null, values);
        values.clear();
        db.close();
    }

    // this function for add store in Local Local Offer store
    public void  addStoreToOffer( String store_id, int id){

        String offer_id = String.valueOf(id);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OFFER_ID, offer_id);
        values.put(KEY_STORE_ID, store_id);
        db.insert(TABLE_Local_Offer_Store_LocalOffer, null, values);
        values.clear();
        db.close();

    }



    public ArrayList<String> getAllDistinctLocalOffers() {
        ArrayList<String> offers = new ArrayList<>();
        String query = "SELECT  DISTINCT "+KEY_OFFER_ID+" FROM " + TABLE_Local_Offer_Store_forBarcode+" ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            do {
                offers.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        db.close();
        return offers;
    }
    public ArrayList<String> getAllLocalStoreFromOffer(String offer) {
        ArrayList<String> stores = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_forBarcode + " where "+KEY_OFFER_ID+" like '"+offer+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            do {
                Log.i("Myapp", "a " + cursor.getString(2));
                stores.add(cursor.getString(2));

            } while (cursor.moveToNext());
        }
        db.close();
        return stores;
    }
    public ArrayList<Class_Store> getAllLocalStoreFromOffer(String offer,Synceddb synceddb) {
        ArrayList<Class_Store> stores = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store + " where "+KEY_OFFER_ID+" like '"+offer+"' ORDER BY id";
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
                tp=synceddb.getStoreInfo (cursor.getString(2));
                tp.storeaddedLocally=true;
                if(tp!=null)
                stores.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return stores;
    }


    public ArrayList<String> getAllLocalStoreFromLocalOffer(int  id) {

        String offer_id = String.valueOf(id) ;
        ArrayList<String> stores = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_LocalOffer + " where "+KEY_OFFER_ID+"  like '"+offer_id+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            do {
                Log.i("Myapp", "a " + cursor.getString(2));
                stores.add(cursor.getString(2));

            } while (cursor.moveToNext());
        }
        db.close();
        return stores;
    }



    public ArrayList<String> getAllLocalStoreFromLocalOfferFor_Json(int  id) {
        ArrayList<String> stores = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_LocalOffer + " where "+KEY_ID+" = "+id+" ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            do {
                Log.i("Myapp", "a " + cursor.getString(2));
                stores.add(cursor.getString(2));

            } while (cursor.moveToNext());
        }
        db.close();
        return stores;
    }


    public ArrayList<Class_Store> getAllLocalStoreFromLocalOffer(int id,Synceddb synceddb) {
        Log.i("TAG","hey this is  id in localdb "+ id);
        String offer = String.valueOf(id);

        ArrayList<Class_Store> stores = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_LocalOffer + " where "+KEY_OFFER_ID+" like '"+offer+"' ORDER BY id";
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
                tp=synceddb.getStoreInfo(cursor.getString(2));
                tp.storeaddedLocally=true;
                if(tp!=null)
                    stores.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return stores;
    }
    //checking for view Stores....??


    // Last
//    public ArrayList<Class_Offers> getAllLocalOfferFromStore(int store) {
//        Log.i("Myapp","d "+store);
//        ArrayList<Class_Offers> offers = new ArrayList<>();
//        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store + " where "+KEY_STORE_ID+"="+store+" ORDER BY id";
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


               // Last
//    public ArrayList<Class_Offers> getAllLocal_LocalOfferFromStore(int store) {
//        Log.i("Myapp","d "+store);
//        ArrayList<Class_Offers> offers = new ArrayList<>();
//        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_LocalOffer + " where "+KEY_STORE_ID+"="+store+" ORDER BY id";
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


    public Class_Offers getOfferInfo(Integer id) {
        String query = "SELECT  * FROM " + TABLE_Local_Offer + " where "+KEY_ID+"="+id+" ORDER BY id";
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
            tp.item_code=cursor.getString(1);
            tp.dom=cursor.getString(2);
          //  tp.deliverable=(Integer.parseInt(cursor.getString(3)));
            tp.point=(Integer.parseInt(cursor.getString(3)));
            tp.IsOfferLocal=true;
            tp.approved= true;
            tp.name = cursor.getString(5);
           tp.remaining_codes = (Integer.parseInt(cursor.getString(6)));
            tp.vendor_id= cursor.getString(7);
            Log.i("Myapp","b "+cursor.getString(1));
        }
        db.close();
        return tp;
    }






    public Boolean CheckForStoreFromOfferCombination(String offer,String storeid) {
        Boolean found=false;
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store + " where "+KEY_OFFER_ID+" like '"+offer+"' AND "+KEY_STORE_ID+" like '"+storeid+"' ORDER BY id";
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

    public Boolean CheckForStoreFromOfferCombinationLocal(int id,String storeid) {
        Boolean found=false;

        String offer = String.valueOf(id);
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_LocalOffer + " where "+KEY_OFFER_ID+" like '"+offer+"' AND "+KEY_STORE_ID+" like '"+storeid+"' ORDER BY id";
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

    public Boolean CheckForBarcode(String barcode) {
        Boolean found=false;
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_forBarcode + " where "+KEY_BARCODE+" like '"+barcode+"' ORDER BY id";
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

    public Boolean CheckForBarcodeForLocal(String barcode) {
        Boolean found=false;
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_LocalOffer_forBarcode + " where "+KEY_BARCODE+" like '"+barcode+"' ORDER BY id";
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

    //use this if offer is synced but add new store
    public void addBarcode(String offer_id,String store_id,String barcode){
        Log.i("Myapp", "TABLE_Local_Offer_Store_forBarcode " + offer_id + " " + store_id);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_OFFER_ID, offer_id);
            values.put(KEY_STORE_ID, store_id);
            values.put(KEY_BARCODE, barcode);
            db.insert(TABLE_Local_Offer_Store_forBarcode, null, values);
            values.clear();
            db.close();

    }

    //use this if offer is locally created
    public void addBarcode_OfferIsNew(String offer_id,String store_id,String barcode){
        Log.i("Myapp", "TABLE_Local_Offer_Store_LocalOffer_forBarcode " + offer_id + " " + store_id);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_OFFER_ID, offer_id);
            values.put(KEY_STORE_ID, store_id);
            values.put(KEY_BARCODE, barcode);
            db.insert(TABLE_Local_Offer_Store_LocalOffer_forBarcode, null, values);
            values.clear();
            db.close();

    }

    public ArrayList<Barcodes> getAllBarcode(String offer, String store) {
        ArrayList<Barcodes> offers = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_forBarcode + " where "+KEY_OFFER_ID+" like '"+offer+"' AND "+KEY_STORE_ID+" like '"+store+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();

        Barcodes tp = null;
        if (cursor.moveToFirst()) {
            do {
                tp = new Barcodes();
                Log.i("Myapp","a "+cursor.getString(3));
                tp.id = (Integer.parseInt(cursor.getString(0)));
                tp.QR_code = cursor.getString(3);
                //offers.add(cursor.getString(3));
                offers.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return offers;
    }


// change id to offer_id as temp.
    public ArrayList<Barcodes> getAllBarcodeFor_Json(String id, String store) {
        ArrayList<Barcodes> offers = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_forBarcode + " where "+KEY_OFFER_ID+" like '"+id+"' AND "+KEY_STORE_ID+" like '"+store+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();

        Barcodes tp = null;
        if (cursor.moveToFirst()) {
            do {
                tp = new Barcodes();
                Log.i("Myapp","a "+cursor.getString(3));
                tp.id = (Integer.parseInt(cursor.getString(0)));
                tp.QR_code = cursor.getString(3);
                //offers.add(cursor.getString(3));
                offers.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return offers;
    }




    public Integer getNoAllBarcodeForLocal(int  id,String store) {
        String offer_id = String.valueOf(id);
        Integer x=0;
        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_LocalOffer_forBarcode + " where "+KEY_OFFER_ID+" like '"+offer_id+"' AND "+KEY_STORE_ID+" like '"+store+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            do {
                Log.i("Myapp","a "+cursor.getString(3));
                x++;
            } while (cursor.moveToNext());
        }
        db.close();
        return x;
    }

    public ArrayList<Barcodes> getAllBarcodeForLocal(int id, String store) {
        ArrayList<Barcodes> offers = new ArrayList<>();
        String offer_id = String.valueOf(id);

        String query = "SELECT  * FROM " + TABLE_Local_Offer_Store_LocalOffer_forBarcode + " where "+KEY_OFFER_ID+" like '"+offer_id+"' AND "+KEY_STORE_ID+" like '"+store+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Barcodes tp = null;
        if (cursor.moveToFirst()) {
            do {
                tp = new Barcodes();

                Log.i("Myapp","a "+cursor.getString(3));
                tp.id = (Integer.parseInt(cursor.getString(0)));
                tp.QR_code = cursor.getString(3);
               // offers.add(cursor.getString(3));
                offers.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return offers;
    }

    public String CheckFor_ItemCode_MFG_Points(String ItemCode,String MFG,String vendor)
    {
        //int id=-1;
        String id = null;
        String query = "SELECT  * FROM " + TABLE_Local_Offer + " where "+KEY_ITEM_CODE+" like '"+ItemCode+"' AND "+KEY_DOM+" like '"+MFG+"' AND "+KEY_Vendor_id+" like '"+vendor+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Store tp = null;
        if (cursor.moveToFirst()) {
            //id=(Integer.parseInt(cursor.getString(0)));
            id = cursor.getString(0);
        }
        db.close();
        return id;
    }
    public ArrayList<Class_Offers> CheckFor_ItemCode_MFG(String ItemCode,String MFG,String store_id)
    {
        ArrayList<Class_Offers> Offers = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Local_Offer + " where "+KEY_ITEM_CODE+" like '"+ItemCode+"' AND "+KEY_DOM +" like '"+MFG+"' AND "+KEY_Vendor_id+" like '"+store_id+"' ORDER BY id";
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
         //   tp.offer_id=(Integer.parseInt(cursor.getString(1)));
            tp.item_code=cursor.getString(1);
            tp.dom=cursor.getString(2);
          //  tp.deliverable=(Integer.parseInt(cursor.getString(3)));
            tp.point=(Integer.parseInt(cursor.getString(3)));
            tp.IsOfferLocal=true;
            tp.approved=true;
            tp.name = cursor.getString(5);
            tp.remaining_codes = (Integer.parseInt(cursor.getString(6)));
            Offers.add(tp);

            Log.i("TAG","checkSameOfferfromLocal"+Offers);
        }
        db.close();
        return Offers;
    }


    public String CheckFor_ItemCode__storeId(String ItemCode, String vendor)
    {
       // int id=-1;
        String id = null ;
        String query = "SELECT  * FROM " + TABLE_Local_Offer + " where "+KEY_ITEM_CODE+" like '"+ItemCode+"' AND "+KEY_Vendor_id+" like '"+vendor+"' ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Store tp = null;
        if (cursor.moveToFirst()) {
            id= cursor.getString(0);
        }
        db.close();
        return id;
    }
    public void   upDate_Point(int id, int point ){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POINT, point);
      //  db.rawQuery("UPDATE "+ TABLE_Local_Offer + " SET "+ KEY_POINT + " = '"+point +"' WHERE "+ KEY_ID + " = "+id, null );
        db.update(TABLE_Local_Offer, values, KEY_ID+"="+id, null);
    }


    public void upDate_DOM(int id, String dom ,int point){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DOM, dom);
        values.put(KEY_POINT, point);
        //  db.rawQuery("UPDATE "+ TABLE_Local_Offer + " SET "+ KEY_POINT + " = '"+point +"' WHERE "+ KEY_ID + " = "+id, null );
        db.update(TABLE_Local_Offer, values, KEY_ID+"="+id, null);

    }


    public void insert_update_value_for_sync_offers(String item_code, String offer_id , String dom , int point , String store_id ){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OFFER_ID, offer_id );
        values.put(KEY_STORE_ID, store_id );
        values.put(KEY_ITEM_CODE, item_code );
        values.put(KEY_DOM, dom);
        values.put(KEY_POINT, point);
        db.insert(TABLE__Local_offer_store_forDom, null, values);
        values.clear();

        db.close();
    }


      ArrayList<Class_Offers> modified_old_data(){

          ArrayList<Class_Offers> old_data = new ArrayList<>();

          String query = "SELECT  * FROM " + TABLE__Local_offer_store_forDom + " ORDER BY id";
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
                  tp.offer_id = cursor.getString(1);
                  tp.vendor_id = cursor.getString(2);
                  tp.item_code = cursor.getString(3);
                  tp.dom = cursor.getString(4);
                  tp.point = (Integer.parseInt(cursor.getString(5)));

                  old_data.add(tp);
              } while (cursor.moveToNext());
          }
          db.close();
          return old_data;

      }

    public void deleteNewOffer(int offer_id) {
        Log.i("Myapp", "Total Inside "+offer_id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Local_Offer+ " WHERE "+KEY_ID+"='"+offer_id+"'");
        db.execSQL("DELETE FROM " + TABLE_Local_Offer_Store_LocalOffer_forBarcode+ " WHERE "+KEY_ID +"='"+offer_id+"'" );
        db.execSQL("DELETE FROM " + TABLE_Local_Offer_Store_LocalOffer+ " WHERE "+KEY_ID +"='"+offer_id+"'"  );
        db.close();
        //Delete Qr code for offer via TABLE_Local_Offer_Store_LocalOffer_forBarcode
    }
    public void deleteOldOffer(String offer_id) {
        Log.i("Myapp", "Total Inside1 "+offer_id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_Local_Offer_Store+" WHERE "+KEY_OFFER_ID+" like '" +offer_id+"'");
        db.execSQL("DELETE FROM "+TABLE_Local_Offer_Store_forBarcode+" WHERE "+KEY_OFFER_ID+" like '" +offer_id+"'");
        db.execSQL("DELETE FROM "+TABLE__Local_offer_store_forDom+ " WHERE "+KEY_OFFER_ID+" like '" +offer_id+"'");

        db.close();
        //Delete Qr code for offer via TABLE_Local_Offer_Store_LocalOffer_forBarcode
    }


    public void delete_local_offer_store(){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_Local_Offer_Store, null, null);
        db.close();
    }

    public void delete_Local_Row(int id, String storeID){

        Log.i("Tag","checking for delete :"+id + storeID);
         String offer_id = String.valueOf(id);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Local_Offer+ " WHERE "+KEY_ID+"='"+id+"'");
        db.execSQL("DELETE FROM " + TABLE_Local_Offer_Store_LocalOffer_forBarcode+ " WHERE "+KEY_OFFER_ID +" like '"+offer_id+"' AND "+KEY_STORE_ID+" like '"+storeID+"'" );
        db.execSQL("DELETE FROM " + TABLE_Local_Offer_Store_LocalOffer+ " WHERE "+KEY_OFFER_ID +" like'"+offer_id+"' AND "+KEY_STORE_ID+" like '"+storeID+"'" );
        db.close();
    }

    public void delete_Local_Qrcodes(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Local_Offer_Store_LocalOffer_forBarcode+ " WHERE "+KEY_ID+"='"+id+"'");
        db.execSQL("DELETE FROM " + TABLE_Local_Offer_Store_forBarcode+ " WHERE "+KEY_ID+"='"+id+"'");
        db.close();
    }

    public void deleteSameOfferIdfromLocal_Local_offer_store(String Store_id,int id){

        String offer_id = String.valueOf(id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Local_Offer_Store_LocalOffer+ " WHERE "+KEY_STORE_ID +" like '"+Store_id+"' AND "+KEY_OFFER_ID+" like'"+offer_id+"'");
        db.close();

    }

    public void deleteOffer_idFromLocal_offer_store(String store_id,String off_id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Local_Offer_Store+ " WHERE "+KEY_STORE_ID +" like '"+store_id+"' AND "+KEY_OFFER_ID+" like'"+off_id+"'");
        db.close();
    }

    //delete barcode and store added  in  synced offer

    public void delete_Barcode_and_storeFrom_syncedOffer(String OfferID, String storeId ){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Local_Offer_Store_forBarcode+ " WHERE "+KEY_OFFER_ID +" like '"+OfferID+"' AND "+KEY_STORE_ID+" like '"+storeId+"'");
        db.execSQL("DELETE FROM " + TABLE_Local_Offer_Store+ " WHERE "+KEY_OFFER_ID +" like '"+OfferID+"' AND "+KEY_STORE_ID+" like '"+storeId+"'" );
        db.close();
    }

}

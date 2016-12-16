package jlabs.fepp;

//Created by pradeep kumar (Jussconnect)

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

public class Maindb{

    Synceddb synceddb;
    localdb localdb;
    Itemsdb itemsdb;

    Maindb(Context context)
    {
        synceddb=new Synceddb(context);
        localdb=new localdb(context);
        itemsdb=new Itemsdb(context);
    }

    public void create_sync_offer(JSONObject jsonObject)
    {
        synceddb.addToSyncOffer(jsonObject);
    }

//    public Class_Offer_Store create_local_offer(int store_id,Class_Offers offers)
//    {
//        Class_Offer_Store getOffer_id =new Class_Offer_Store();
//        getOffer_id.store_id=0;
//
////        int ret=0;
//
//        int offer_id=synceddb.CheckFor_ItemCode_MFG_Points(offers.item_code,offers.dom,offers.point);
//        if(offer_id!=-1)
//        {
//
//            getOffer_id.store_id=1;
//            getOffer_id.offer_id=offer_id;
////            ret=1;
//            //already found in synced offers
//            localdb.addToLocalOfferStore(offer_id,store_id);
//
//        }
//        else
//        {
//
//            offer_id=localdb.CheckFor_ItemCode_MFG_Points(offers.item_code,offers.dom,offers.point);
//            if(offer_id!=-1)
//            {
//                getOffer_id.store_id=2;
//                getOffer_id.offer_id=offer_id;
////                ret=2;
//                //already found in local offers
//                localdb.addToLocalOfferStore_OfferIsNew(offer_id,store_id);
//            }
//            else
//            {
//                //not found hence created
//                offer_id=localdb.addToLocal(store_id,offers);
//                if(offer_id>=0)
//                    getOffer_id.offer_id=offer_id;
//                else
//                    getOffer_id.store_id=3;
//            }
//        }
//        return getOffer_id;
//    }
    public Class_Offer_Store create_local_offer(String store_id,Class_Offers offers)
    {

        Class_Offer_Store getOffer_id = new Class_Offer_Store();
        getOffer_id.store_id= 0;


        String offer_id=synceddb.CheckFor_ItemCode_MFG_Points(offers.item_code,offers.dom,store_id);
        if(offer_id!= null)
        {
            getOffer_id.store_id= 1;
            getOffer_id.offer_id=offer_id;

        }
        else
        {

            offer_id=localdb.CheckFor_ItemCode_MFG_Points(offers.item_code,offers.dom,store_id);
            if(offer_id!= null)
            {
                getOffer_id.store_id= 2;
                getOffer_id.offer_id=offer_id;
            }
            else
            {
                //not found hence created
                    offer_id=localdb.addToLocal(store_id,offers);
                    //if(offer_id>=0)
                  if(offer_id !=null)  {
                        getOffer_id.offer_id = offer_id;
                        getOffer_id.store_id = 3;
                    }
            }
        }
        return getOffer_id;
    }





    public Class_Offer_Store modify_local_offer(String store_id,Class_Offers offers,String off, int id)
    {

        Log.i("fgfg", "hfg"+offers.vendor_id + "ghgh"+ store_id);
        Class_Offer_Store getOffer_id = new Class_Offer_Store();
        getOffer_id.store_id=0;

         Log.i("hh","store_id "+ store_id  + "ven "+ offers.vendor_id);
        String offer_id=synceddb.CheckFor_ItemCode_MFG_Points(offers.item_code,offers.dom,store_id);
        if(offer_id!= null)
        {
            Log.d("LCM: ","this is checking Method scan 1");
            localdb.insert_update_value_for_sync_offers(offers.item_code,offers.offer_id,offers.dom,offers.point,offers.vendor_id);
            getOffer_id.store_id=1;
            getOffer_id.offer_id=offer_id;

        }
        else
        {

            offer_id=localdb.CheckFor_ItemCode_MFG_Points(offers.item_code,offers.dom,store_id);
            if(offer_id!= null)
            {
                Log.d("HCM: ","this is checking Method  scan 2");
               // localdb.upDate_Point(offers.id, offers.point);

                if(offers.vendor_id.equals(store_id)){
                    localdb.upDate_Point(offers.id, offers.point);
                }else{
                    localdb.deleteSameOfferIdfromLocal_Local_offer_store(store_id,id);
                    localdb.deleteOffer_idFromLocal_offer_store(store_id,off);

                }

                getOffer_id.store_id=2;
                getOffer_id.offer_id=offer_id;
            }
            else
            {
               // check synce db with 2 params
                offer_id = synceddb.CheckFor_ItemCode_storeId(offers.item_code, store_id);
                Log.i("sync TTTT", "offer_id" + offer_id  + " : " + store_id);
                if (offer_id != null){
                    Log.i("PPP: ","this is checking Sync with 2 param");
                    Log.i("YYY", "ven"+ offers.vendor_id + " add " + store_id);
                    if(offers.vendor_id.equals(store_id)){

                        // update DOM and Point
                        Log.i("sync", "scan method sync with 2 params"+offers.vendor_id + " add store" + store_id);

                        localdb.insert_update_value_for_sync_offers(offers.item_code,offers.offer_id,offers.dom,offers.point,offers.vendor_id);
                    }else
                    {
                        localdb.deleteSameOfferIdfromLocal_Local_offer_store(store_id,id);
                        localdb.deleteOffer_idFromLocal_offer_store(store_id,off);
                    }
                    getOffer_id.store_id=3;
                    getOffer_id.offer_id=offer_id;

                }else

                {
                         // check local db with 2 params
                    offer_id = localdb.CheckFor_ItemCode__storeId(offers.item_code,store_id);
                    if(offer_id!= null){

                        Log.i("PPP: ","this is checking local with 2 param");

                        if(offers.vendor_id.equals(store_id)){
                            localdb.upDate_DOM(offers.id,offers.dom,offers.point);

                        }else{
                            localdb.deleteSameOfferIdfromLocal_Local_offer_store(store_id,id);
                            localdb.deleteOffer_idFromLocal_offer_store(store_id,off);
                        }

                        getOffer_id.store_id=4;
                        getOffer_id.offer_id=offer_id;


                    }else{

                          Log.i("TTT", "new offer is created");
                        // delete same offer_id from Table Local_Local_offer_store(when Add new store )
                        localdb.deleteSameOfferIdfromLocal_Local_offer_store(store_id,id);
                        localdb.deleteOffer_idFromLocal_offer_store(store_id,off);
                        offer_id=localdb.addToLocal(store_id,offers);
                        if(offer_id !=null) {
                            getOffer_id.offer_id = offer_id;
                            getOffer_id.store_id = 5;
                        }

                    }



                }

            }
        }
        return getOffer_id;
    }


    public ArrayList<Class_Offers> get_offer_for_itemcodeAndDOM(String item_id,String DOM,String  store_id) {
        ArrayList<Class_Offers> offers = new ArrayList<>();
        offers.addAll(synceddb.CheckFor_ItemCode_MFG(item_id,DOM, store_id));
        offers.addAll(localdb.CheckFor_ItemCode_MFG(item_id, DOM, store_id));
        return offers;
    }



    public void create_sync_Items(JSONObject jsonObject)
    {
        itemsdb.addToSyncItems(jsonObject);
    }

    public void create_Fe_list(JSONObject jsonObject){


        itemsdb.addListOfFe(jsonObject);
    }



    public ArrayList<Class_Offers> getallOffers()
    {
        ArrayList<Class_Offers> offers=new ArrayList<>();

        offers.addAll(localdb.getAllLocalOffer());
        offers.addAll(synceddb.getAllSyncOffer());
        return offers;
    }

    public ArrayList<Class_Offers> getOnlySyncOffers(){

        ArrayList<Class_Offers> offers=new ArrayList<>();
        offers.addAll(synceddb.getAllSyncOffer());
        return offers ;
    }

    public ArrayList<Class_Fe> getAllFE(){

        ArrayList<Class_Fe> fes = new ArrayList<>();
        fes.addAll(itemsdb.getAllFE_List());
        return fes ;
    }


   public ArrayList<Class_Offers> getUniqueStoreName(){
       ArrayList<Class_Offers> storeName=new ArrayList<>();
       storeName.addAll(synceddb.getStoreNameSync());
      // offers.addAll(localdb.getStoreNameLocal());
       return storeName;
   }


    public ArrayList<Class_Offers> getStoreWiseOffers(String pp){

       ArrayList<Class_Offers> offers=new ArrayList<>();
       offers.addAll(synceddb.getStoreWiseSyncOffers(pp));
       offers.addAll(localdb.getStoreWiseLocalOffers(pp));

     return offers;
  }



    public int NumgetallOffers()
    {
        int num=0;
        num=num+synceddb.getAllSyncOffer().size();
        num=num+localdb.getAllLocalOffer().size();
        return num;
    }

    public void create_sync_store(JSONObject jsonObject)
    {
        synceddb.addToSyncStore(jsonObject);
    }

    public ArrayList<Class_Store> getAllStore() {
        return synceddb.getAllSyncStore();
    }

    public int NumgetallStores()
    {
        return synceddb.getAllSyncStore().size();
    }

    public ArrayList<Class_Items> getAllItems() {
        return itemsdb.getAllItems();
    }

    public int NumgetallItems()
    {
        return itemsdb.getAllItems().size();
    }

    public ArrayList<Class_Items> getItemByBarcode(String Barcode) {
        return itemsdb.getAllItemsByBarcode(Barcode);
    }

    public ArrayList<Class_Store> getAllSyncStoreFromOffer(String offer,Boolean IsOfferLocal, int id) {
        ArrayList<Class_Store> stores =new ArrayList<>();
        if(!IsOfferLocal) {
            stores.addAll(synceddb.getAllSyncStoreFromOffer(offer));
            stores.addAll(localdb.getAllLocalStoreFromOffer(offer, synceddb));
        }
        else
            stores.addAll(localdb.getAllLocalStoreFromLocalOffer(id,synceddb));
        return stores;
    }


    //   Last
//    public ArrayList<Class_Offers> getAllSyncOfferFromStore(String store) {
//        ArrayList<Class_Offers> offers = new ArrayList<>();
//        offers.addAll(synceddb.getAllSyncOfferFromStore(store));
//        offers.addAll(localdb.getAllLocalOfferFromStore(store));
//        offers.addAll(localdb.getAllLocal_LocalOfferFromStore(store));
//        return offers;
//    }

    public Boolean AddNewStoreToOffer(String offer_id,String store_id,Boolean OfferIsLocal, int id )
    {
        Boolean added=false;
        if(OfferIsLocal) {
            if (!localdb.CheckForStoreFromOfferCombinationLocal(id, store_id )) {
               // localdb.addToLocalOfferStore_OfferIsNew(offer_id, store_id ,id);
                localdb.addStoreToOffer(store_id ,id);
                added=true;
            }
        }
        else {
            if (!(synceddb.CheckForStoreFromOfferCombination(offer_id, store_id))&&!(localdb.CheckForStoreFromOfferCombination(offer_id,store_id))) {
                localdb.addToLocalOfferStore(offer_id, store_id);
                added=true;
            }
        }
        return added;
    }

    public boolean AddNewBarcode(String offer_id,String store_id,Boolean OfferIsLocal,String barcode)
    {
        Boolean barcodeexists=false;
        Log.i("Myapp ","Add Offer id "+offer_id +" store id "+store_id+" offerislocal "+OfferIsLocal);

        if((!localdb.CheckForBarcodeForLocal(barcode))&&(!localdb.CheckForBarcode(barcode)))
        {
            if(OfferIsLocal)
                localdb.addBarcode_OfferIsNew(offer_id, store_id, barcode);
            else
                localdb.addBarcode(offer_id, store_id,barcode);
        }
        else
            barcodeexists=true;
     return barcodeexists;
    }

    public ArrayList<Barcodes> getAllBarcode(String offer_id, String store_id, Boolean OfferIsLocal, int id) {
        Log.i("Myapp ", "View Offer id " + offer_id + " store id " + store_id + " offerislocal " + OfferIsLocal);
        ArrayList<Barcodes> barcodes = new ArrayList<>();
        if(OfferIsLocal)
            barcodes.addAll(localdb.getAllBarcodeForLocal(id, store_id));
        else
            barcodes.addAll(localdb.getAllBarcode(offer_id, store_id));
        return barcodes;
    }

    public int getAllNoQrCodes(int offer_id,int store_id) {
        ArrayList<Barcodes> barcodes = new ArrayList<>();
          //  barcodes.addAll(localdb.getAllBarcodeForLocal(offer_id, store_id));
          // barcodes.addAll(localdb.getAllBarcode(offer_id, store_id));
        return barcodes.size();
    }

    public String getItemImage(String itemcode){

        return itemsdb.getItemImage(itemcode);
    }

    public Class_Offers getOfferInfo(Boolean offer_is_local,String offer ,int id){
        if(offer_is_local)
            return localdb.getOfferInfo(id);
        else
            return synceddb.getOfferInfo(offer);
    }

    public  Class_Store getStoreInfo(String store){

        return synceddb.getStoreInfo(store);
    }

  /*  public int getOfferInfo(String ItemCode,String MFG,int Points){

        Class_Offer_Store getOffer_id =new Class_Offer_Store();
        getOffer_id.store_id=0;

        int offer_id = synceddb.CheckFor_ItemCode_MFG_Points(ItemCode,MFG,Points);

        if(offer_id==-1){

            getOffer_id.store_id=1;
            getOffer_id.offer_id=offer_id;

           localdb.CheckFor_ItemCode_MFG_Points(ItemCode,MFG,Points);

            if(offer_id==-1){

                //// TODO: 19/05/2016 create new offer

                offer_id= 10;
            }
        }

        return offer_id;
    }*/


    public int getItemShelfLife(String itemcode)
    {
        return itemsdb.getItemShelfLife(itemcode);
    }

    public void deleteNewOffer(int offer_id)
    {
        localdb.deleteNewOffer(offer_id);
    }

    public void delete_added_store(){

        localdb.delete_local_offer_store();
    }
    public void deleteOldOffer(String offer_id)
    {
        localdb.deleteOldOffer(offer_id);
    }

    public void deleteItems()
    {
        itemsdb.deleteItems();
    }

    public void deleteOffers()
    {
        synceddb.deleteOffer();
    }

    public void delete_Fe_List(){

        itemsdb.delete_FE_list();
    }

    public void deleteStores()
    {
        synceddb.deleteStore();
    }

    //delete offers  on click button
    public void deleteRow(String OfferID, int id, Boolean IsOfferLocal,String storeID){

        if(IsOfferLocal){

            localdb.delete_Local_Row(id,storeID);
        }else {
            synceddb.deleteRow(OfferID);
            localdb.delete_Barcode_and_storeFrom_syncedOffer(OfferID,storeID);
        }
    }

    //delete QR codes from Local database
    public void delete_QRCodes(int  id){

        localdb. delete_Local_Qrcodes(id);
    }

    public void delete_fe(int idd){

        itemsdb.delete_fe_ById(idd);
    }


    public void insertApproved(String offer_id){

        synceddb.addApproved_In_Offer(offer_id);
    }

}

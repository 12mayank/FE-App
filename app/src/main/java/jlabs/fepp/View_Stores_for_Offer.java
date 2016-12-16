package jlabs.fepp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class View_Stores_for_Offer extends AppCompatActivity implements View.OnClickListener,ListView.OnItemClickListener{

    Maindb synceddb;
    int idd;
    String offer_id;
    ListView lv;
    Boolean offer_is_local;
    ArrayList<Class_Store> stores;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers);
        idd=getIntent().getIntExtra("id",0);
        offer_id = getIntent().getStringExtra("offer_id");
        offer_is_local=getIntent().getBooleanExtra("offer_type", false);
        synceddb = new Maindb(this);
        Log.d("TM", "offer type "+ offer_is_local);
        lv = (ListView) findViewById(R.id.offers);
        findViewById(R.id.add_store).setVisibility(View.VISIBLE);
        findViewById(R.id.add_store).setOnClickListener(this);
        lv.setOnItemClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this,Add_Store_to_Offer.class);
        i.putExtra("offer_id",offer_id);
        i.putExtra("offer_type",offer_is_local);
        i.putExtra("id",idd);
         Log.i("TATAATA"," offerid add :"+offer_id);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        stores = synceddb.getAllSyncStoreFromOffer(offer_id,offer_is_local,idd);
        Adapter_Stores stores_adap = new Adapter_Stores(this,stores);
        lv.setAdapter(stores_adap);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this,View_QrCode.class);
        i.putExtra("offer_id",offer_id);
        i.putExtra("offer_type",offer_is_local);
        i.putExtra("id",idd);
        i.putExtra("store_id",stores.get(position).store_id);
        Log.i("TATA"," offerid viewQrCode :"+offer_id);
        Log.i("TATA"," storeid  viewQrcode :"+stores.get(position).store_id);
        //startActivity(i);
        int requestCode = 2;
        startActivityForResult(i, requestCode);
    }
}

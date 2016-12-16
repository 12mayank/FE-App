package jlabs.fepp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;

public class View_QrCode extends AppCompatActivity implements View.OnClickListener {

    Maindb synceddb;
  //  ArrayList<String> barcodes;
     ArrayList<Barcodes> barcodes;
    int id;
    String offer_id ,store_id ;
    boolean offer_is_local;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcodes);
        synceddb = new Maindb(this);
        if(getIntent().hasExtra("store_id"))
        {
            store_id=getIntent().getStringExtra("store_id");
            id=getIntent().getIntExtra("id", 0);
            offer_id=getIntent().getStringExtra("offer_id");
            offer_is_local=getIntent().getBooleanExtra("offer_type", false);

            Log.i("TA"," offerid add :"+offer_id+ store_id + offer_is_local);
        }
        findViewById(R.id.add_barcode).setOnClickListener(this);
        lv = (ListView) findViewById(R.id.list_view);
    }

    @Override
    public void onClick(View v) {
        Intent i= new Intent(this,ContinuousScan.class);
        i.putExtra("offer",synceddb.getOfferInfo(offer_is_local,offer_id, id));
        i.putExtra("store_id",store_id);
        i.putExtra("id",id);
        //for show qrcodes in summury
        i.putExtra("offer_id",offer_id);
        i.putExtra("offer_type",offer_is_local);

        Log.i("TA"," offerid added :"+offer_id+store_id+ offer_is_local);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodes=synceddb.getAllBarcode(offer_id,store_id,offer_is_local,id);
      //  ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, android.R.id.text1, barcodes);
        View_QrCode_Adapter adapter = new View_QrCode_Adapter(this,barcodes);
        lv.setAdapter(adapter);
    }

   @Override
   public void onBackPressed(){
       int resultCode = 3;
       setResult(resultCode);
       finish();
   }

}

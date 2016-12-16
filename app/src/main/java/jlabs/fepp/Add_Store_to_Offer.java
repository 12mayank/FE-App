package jlabs.fepp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

public class Add_Store_to_Offer extends AppCompatActivity implements ListView.OnItemClickListener{

    Maindb maindb;
    ArrayList<Class_Store> stores;
    String offer_id;
    Boolean offer_is_local;
    int idd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers);
        offer_id=getIntent().getStringExtra("offer_id");
        Log.i("TP","UU  : "+offer_id);
        offer_is_local=getIntent().getBooleanExtra("offer_type",false);
        idd = getIntent().getIntExtra("id",0);
        maindb = new Maindb(this);
        ListView lv = (ListView) findViewById(R.id.offers);
       // ((TextView)findViewById(R.id.title)).setText("All Stores");
        stores = maindb.getAllStore();
        Adapter_Stores stores_adap = new Adapter_Stores(this,stores);
        lv.setAdapter(stores_adap);
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Log.i("TAG GH :" ,"offer id :"+offer_id  +idd);

        Boolean added=maindb.AddNewStoreToOffer(offer_id, stores.get(position).store_id, offer_is_local , idd);
        if(added)
            Toast.makeText(this,"New Store Added Successfully",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"Looks Like Store Already Exist",Toast.LENGTH_SHORT).show();

        finish();
    }


}

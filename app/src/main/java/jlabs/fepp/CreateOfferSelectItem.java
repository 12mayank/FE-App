package jlabs.fepp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class CreateOfferSelectItem extends AppCompatActivity implements ListView.OnItemClickListener{

    Maindb maindb;
    ArrayList<Class_Items> items;
    ArrayList<Class_Offers> offer;
    String barcode;
    ListView lv;
    String store_id;
    String name;
    Button bt;
    Activity con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers);
        maindb = new Maindb(this);
        con=this;
        store_id=getIntent().getStringExtra("store_id");
        name = getIntent().getStringExtra("name");
        barcode=getIntent().getStringExtra("barcode");
        lv = (ListView) findViewById(R.id.offers);
        bt = (Button) findViewById(android.R.id.empty);
      //  ((TextView)findViewById(R.id.title)).setText("Select Item");

        lv.setOnItemClickListener(this);
        lv.setEmptyView(findViewById(R.id.layoutone).findViewById(android.R.id.empty));
        new IntentIntegrator(this).setCaptureActivity(ScannerActivity.class).initiateScan();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new IntentIntegrator(con).setCaptureActivity(ScannerActivity.class).initiateScan();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Toast.makeText(this, "Item code :" + items.get(position).Item_code, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,Create_Offers.class);
        i.putExtra("item_id",items.get(position).Item_code);
        i.putExtra("store_id",store_id);
        i.putExtra("name",name);
        i.putExtra("pic",items.get(position).Image);
        i.putExtra("item_name",items.get(position).Item_name);
        i.putExtra("weight",items.get(position).Weight);
        i.putExtra("price",items.get(position).price);
        startActivity(i);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                items = maindb.getItemByBarcode(result.getContents());
                if(items!=null&&items.size()>0) {
                    Adapter_Items stores_adap = new Adapter_Items(this, items);
                    lv.setAdapter(stores_adap);
                }
                else
                {
                    Toast.makeText(this, "No Items Exist for that barcode ", Toast.LENGTH_LONG).show();
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}

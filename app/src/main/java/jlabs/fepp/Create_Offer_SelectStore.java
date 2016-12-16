package jlabs.fepp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class  Create_Offer_SelectStore extends Fragment implements current_reload_data, ListView.OnItemClickListener{

    Maindb maindb;
    ArrayList<Class_Store> stores;
    ListView lv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v= inflater.inflate(R.layout.offers, container, false);
       //return inflater.inflate(R.layout.offers, container, false);
        maindb = new Maindb(getActivity());
        lv = (ListView) v.findViewById(R.id.offers);
        stores = maindb.getAllStore();
        Adapter_Stores stores_adap = new Adapter_Stores(getActivity(),stores);
        lv.setAdapter(stores_adap);
        //Log.v(TAG, "Sounds initialized.");
        lv.setOnItemClickListener(this);
        return v;
    }



    public Create_Offer_SelectStore() {
        // Required empty public constructor
    }


    @Override

    public void current_offer_reload_data(){

        try {
            stores = maindb.getAllStore();
            Adapter_Stores stores_adap = new Adapter_Stores(getActivity(),stores);
            lv.setAdapter(stores_adap);

            lv.setOnItemClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public current_reload_data return_current_interface(){
        return this;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent i = new Intent(getActivity(),CreateOfferSelectItem.class);
        i.putExtra("store_id", stores.get(position).store_id);
        i.putExtra("name",stores.get(position).title);
        Log.i("NEW DATA :","stote _ID" +stores.get(position).store_id + stores.get(position).title);
        startActivity(i);
       // finish();
    }
}

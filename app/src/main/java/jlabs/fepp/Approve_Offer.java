package jlabs.fepp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jlabs-Win on 22/09/2016.
 */
public class Approve_Offer extends AppCompatActivity {

    Maindb synceddb;
    ArrayList<Class_Offers> view_fragment;
    ListView lv;
    Button bt ;
    Boolean Success = false;
    Adapter_Offers offers_adap ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_fragment);
        synceddb = new Maindb(this);
        lv = (ListView) findViewById(R.id.offer);
        bt = (Button) findViewById(android.R.id.empty);
        bt.setVisibility(View.GONE);
        view_fragment = synceddb.getOnlySyncOffers();
        ArrayList<Class_Offers> notapprovedList = new ArrayList<Class_Offers>();


        for (int i = 0; i < view_fragment.size(); i++) {

            Class_Offers obj = view_fragment.get(i);

            Boolean approv = obj.approved;

            if(!approv){

                notapprovedList.add(obj);
            }

        }
        offers_adap = new Adapter_Offers(Approve_Offer.this,notapprovedList);
        lv.setAdapter(offers_adap);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                view.findViewById(R.id.approve_button).setVisibility(View.VISIBLE);

                return false;
            }
        });

    }
}





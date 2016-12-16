package jlabs.fepp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jlabs-Win on 05/05/2016.
 */
public class View_Fragment extends Fragment implements reload_data,ListView.OnItemClickListener {

    Maindb synceddb;
    ArrayList<Class_Offers> view_fragment;
    ListView lv;
    Button bt;
    Boolean Success = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_fragment, container, false);
        synceddb = new Maindb(getContext());
        bt = (Button) view.findViewById(android.R.id.empty);
        bt.setVisibility(View.GONE);
        view_fragment = synceddb.getallOffers();
        lv = (ListView) view.findViewById(R.id.offer);
        Adapter_Offers offers_adap = new Adapter_Offers(getContext(),view_fragment);
        lv.setAdapter(offers_adap);
        lv.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void now_reload_data()
    {
        try {
            view_fragment = synceddb.getallOffers();
            Adapter_Offers offers_adap = new Adapter_Offers(getContext(),view_fragment);
            lv.setAdapter(offers_adap);
            lv.setOnItemClickListener(this);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public reload_data return_reload_interface()
    {
        return this;
    }

    @Override
    public void sold_out_filter(){

       view_fragment = synceddb.getallOffers();
       ArrayList<Class_Offers> soldList = new ArrayList<Class_Offers>();

       for (int i = 0; i < view_fragment.size(); i++){
           Class_Offers obj = view_fragment.get(i);
           int remain = obj.remaining_codes;
           if(remain ==0){
               soldList.add(obj);
           }
       }
       Adapter_Offers offers_adap = new Adapter_Offers(getContext(),soldList);
       lv.setAdapter(offers_adap);
       lv.setOnItemClickListener(this);
   }

    @Override

    public void sold_in_filter(){
        view_fragment = synceddb.getallOffers();
        ArrayList<Class_Offers> availableList = new ArrayList<Class_Offers>();

        for (int i = 0; i < view_fragment.size(); i++){

            Class_Offers obj = view_fragment.get(i);
            int remaining = obj.remaining_codes;
            Log.i("this","remaining count :"+remaining);
           if(remaining >= 1){
               availableList.add(obj);
           }
        }

        Adapter_Offers offers_adap = new Adapter_Offers(getContext(),availableList);
        lv.setAdapter(offers_adap);
        lv.setOnItemClickListener(this);
    }


    @Override

    public void approved_filter(){
      view_fragment = synceddb.getallOffers();
        ArrayList<Class_Offers> approvedList = new ArrayList<Class_Offers>();

        for (int i = 0; i < view_fragment.size(); i++) {

            Class_Offers obj = view_fragment.get(i);

            Boolean approve = obj.approved;

            if(approve){
                approvedList.add(obj);

            }
        }

            Adapter_Offers offers_adap = new Adapter_Offers(getContext(),approvedList);
        lv.setAdapter(offers_adap);
        lv.setOnItemClickListener(this);
    }


     @Override

     public void not_approved_filter(){
         view_fragment = synceddb.getallOffers();
         ArrayList<Class_Offers> notapprovedList = new ArrayList<Class_Offers>();

         for (int i = 0; i < view_fragment.size(); i++) {

             Class_Offers obj = view_fragment.get(i);

             Boolean approv = obj.approved;

             if(!approv){

                 notapprovedList.add(obj);
             }

         }

         Adapter_Offers offers_adap = new Adapter_Offers(getContext(),notapprovedList);
         lv.setAdapter(offers_adap);
         lv.setOnItemClickListener(this);
     }



    @Override
    public void date_filter(String   reformattedStr){

        Log.i("datepicker","pick date: "+reformattedStr);
        view_fragment = synceddb.getallOffers();
        ArrayList<Class_Offers> Bp = new ArrayList<Class_Offers>();

        for (int i = 0; i < view_fragment.size(); i++) {
            Class_Offers obj = view_fragment.get(i);
            String s = obj.dom;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");


            try {
                Date dom1 = sdf.parse(s);
                Log.i("fgh","dhj"+dom1);

                Date pickerDate = df.parse(reformattedStr);

                if((dom1.compareTo(pickerDate)) >=0  ){

                    Bp.add(obj);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Adapter_Offers offers_adap = new Adapter_Offers(getContext(),Bp);
        lv.setAdapter(offers_adap);
        lv.setOnItemClickListener(this);
    }


    @Override
    public void store_filter(String pp){
        view_fragment = synceddb.getStoreWiseOffers(pp);
        Adapter_Offers offers_adap = new Adapter_Offers(getContext(),view_fragment);
        lv.setAdapter(offers_adap);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getContext(),View_Stores_for_Offer.class);
        if(view_fragment.get(position).IsOfferLocal){
            i.putExtra("id", view_fragment.get(position).id);
           Log.i("TAG","hey checking id viewFragment : "+ view_fragment.get(position).id);
        }
        else
            i.putExtra("offer_id",view_fragment.get(position).offer_id);
         Log.i("TAG","hey checking id123 viewFragment : "+ view_fragment.get(position).offer_id);
        i.putExtra("offer_type", view_fragment.get(position).IsOfferLocal);

        int requestCode = 1;
        startActivityForResult(i,requestCode);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

            now_reload_data();
    }



}

package jlabs.fepp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;

import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jlabs-Win on 21/09/2016.
 */
public class All_FE extends AppCompatActivity {

       ListView lv;
       Maindb sync ;
       Button btn ;
      // ArrayList<Class_Fe> feList ;
    List<Class_Fe> feList  = new ArrayList<Class_Fe>();
    Adapter_All_FE adp ;
    String url ;
    Boolean Success = false;
    ProgressDialog pDialog;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_fragment);

        sync = new Maindb(getApplicationContext());
        btn = (Button) findViewById(android.R.id.empty);
        btn.setVisibility(View.GONE);
        lv = (ListView) findViewById(R.id.offer);

        cd = new ConnectionDetector(All_FE.this);
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent){
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            list_ofAllFE();

        }else {

            Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_LONG).show();
        }


        //feList = sync.getAllFE();

    }


    public void list_ofAllFE(){

        String tag_json_obj = "json_obj_req_get_items";
        url = Static_Catelog.getLiveurl() + "cersei/feadmin/list_fe";

        String api_KEY = Static_Catelog.getStringProperty(getApplicationContext(),"api_key");
        String fe_ID = Static_Catelog.getStringProperty(getApplicationContext(),"fe_id");


        JSONObject obj = new JSONObject();

        try {
            obj.put("api_key",api_KEY);
            obj.put("fe_id",fe_ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            pDialog.dismiss();
                            Success = response.getBoolean("success");

                            if(Success){

                                feList.clear();
                                JSONArray arr = response.getJSONArray("data");
                                sync.delete_Fe_List();
                                for (int i = 0; i < arr.length(); i++) {
                                    // sync.create_Fe_list(arr.getJSONObject(i));

                                JSONObject obj =  arr.getJSONObject(i);

                                    Class_Fe  class_fe = new Class_Fe();

                                    class_fe.setFE_id(obj.getString("fe_id"));
                                    class_fe.setFeName(obj.getString("name"));
                                    class_fe.setFeContact(obj.getString("contact"));
                                    class_fe.setFeEmail(obj.getString("email_id"));
                                    class_fe.setFeAdd_by(obj.getString("add_by"));
                                    class_fe.setFeCompany_id(obj.getString("company_id"));

                                    feList.add(class_fe);
                                }

                            }else {

                                Toast.makeText(getApplicationContext(),"Please Login Again", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        adp = new Adapter_All_FE(All_FE.this,feList);
                        lv.setAdapter(adp);

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                //t4.setTextColor(Color.RED);
                VolleyLog.d("Error", "Error: " + error.getMessage());

                pDialog.hide();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


}

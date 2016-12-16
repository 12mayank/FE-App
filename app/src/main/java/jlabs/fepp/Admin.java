package jlabs.fepp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jlabs-Win on 23/08/2016.
 */
public class Admin extends Fragment implements View.OnClickListener {

    Maindb sync ;
    RelativeLayout create_fe,  list_fe, approved_offers,send_sms;

    Intent myIntent ;

    String url ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_layout, container, false);

        sync = new Maindb(getContext());
        create_fe = (RelativeLayout) view.findViewById(R.id.create_fe);
        list_fe = (RelativeLayout) view.findViewById(R.id.all_fe);
        approved_offers = (RelativeLayout) view.findViewById(R.id.offer_approval);
        send_sms = (RelativeLayout) view.findViewById(R.id.send_sms);

        create_fe.setOnClickListener(this);
        list_fe.setOnClickListener(this);
        approved_offers.setOnClickListener(this);
        send_sms.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.create_fe :

                myIntent = new Intent(getActivity(),FE_Form.class);
                startActivity(myIntent);
                 break;

            case R.id.all_fe :

                myIntent = new Intent(getActivity(),All_FE.class);
                startActivity(myIntent);
               // list_ofAllFE();
                break;


            case R.id.offer_approval :

                myIntent = new Intent(getActivity(),Approve_Offer.class);
                startActivity(myIntent);
                break;

            case R.id.send_sms :
                            myIntent = new Intent(getActivity(),Send_Sms.class);
                    startActivity(myIntent);
                break;
        }

    }

//
//    public void list_ofAllFE(){
//
//        String tag_json_obj = "json_obj_req_get_items";
//        url = Static_Catelog.getLiveurl() + "cersei/feadmin/list_fe";
//
//        String api_KEY = Static_Catelog.getStringProperty(getContext(),"api_key");
//        String fe_ID = Static_Catelog.getStringProperty(getContext(),"fe_id");
//
//
//        JSONObject obj = new JSONObject();
//
//        try {
//            obj.put("api_key",api_KEY);
//            obj.put("fe_id",fe_ID);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, obj,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray arr = response.getJSONArray("data");
//                                sync.delete_Fe_List();
//                            for (int i = 0; i < arr.length(); i++) {
//                                sync.create_Fe_list(arr.getJSONObject(i));
//                                Log.i("Myapp", "Stores Times Found " + arr.getJSONObject(i).toString());
//
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //t4.setTextColor(Color.RED);
//                VolleyLog.d("Error", "Error: " + error.getMessage());
//
//            }
//        });
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//
//    }
}

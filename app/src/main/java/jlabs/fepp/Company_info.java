package jlabs.fepp;



import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by Jlabs-Win on 05/05/2016.
 */
public class Company_info extends Fragment{

    ListView lv;
    List<Message_Info> info  = new ArrayList<Message_Info>();

    String url ;
    Context context ;
    Adapter_CompanyInfo adap ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_info, container, false);
        lv = (ListView) view.findViewById(R.id.list_companyInfo);


        getData();


        return view;
    }

//
//    @Override
//    public void sync_fe_message(){
//
//        try {
//            getData();
//            adap = new Adapter_CompanyInfo(getContext(),info);
//            lv.setAdapter(adap);
//            adap.notifyDataSetChanged();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

//    public fe_message return_msg_interface()
//    {
//        return this;
//    }


    public void getData(){

        String tag_json_obj = "json_obj_req_get_items";
        url = Static_Catelog.getLiveurl() + "cersei/feadmin/show_msg";
        String api_key = Static_Catelog.getStringProperty(getContext(),"api_key");
        String fe_id = Static_Catelog.getStringProperty(getContext(),"fe_id");



        JSONObject obj = new JSONObject();

        try {
            obj.put("api_key",api_key);
            obj.put("fe_id", fe_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.i("FE","FE Message :"+response.toString());
                             Boolean Success =  response.getBoolean("success");



                            if (Success){

                                info.clear();
                                JSONArray arr = response.getJSONArray("data");
                                for (int i =0;i< arr.length();i++){

                                  JSONObject obj1 = arr.getJSONObject(i);

                                    JSONArray array = obj1.getJSONArray("messages");
                                    for (int j =0 ; j<array.length(); j++){

                                        JSONObject obj2 = array.getJSONObject(j);
                                        JSONObject obj3 = obj2.getJSONObject("timestamp");
                                        Message_Info inn = new Message_Info();

                                        String ab = obj3.getString("$date");
                                        String date_time = coverted(Long.parseLong(ab));

                                        inn.setInfo(obj2.getString("msg"));
                                        inn.setTime(date_time);
                                        Log.i("TAGHJ" , date_time );
                                        info.add(0,inn); // 0 is index value  which is used to add last element at the top of listview
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        adap = new Adapter_CompanyInfo(getContext(),info);
                        lv.setAdapter(adap);

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

      public String coverted (long tm){

          long unix_timeStamp = tm/1000 ;
          Date date = new Date(unix_timeStamp*1000L); // *1000 is to convert seconds to milliseconds
          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss "); // the format of your date
          sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30 h")); // give a timezone reference for formating (see comment at the bottom

          String formattedDate = sdf.format(date);
          return formattedDate ;
      }


}

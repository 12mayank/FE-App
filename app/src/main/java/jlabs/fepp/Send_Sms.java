package jlabs.fepp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jlabs-Win on 23/09/2016.
 */
public class Send_Sms extends AppCompatActivity {

     EditText edit_info ;
     TextView Send ;
     Boolean Success = false;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.send_message);

        edit_info  = (EditText) findViewById(R.id.edit_info);
        Send  = (TextView)  findViewById(R.id.send_info);

        Send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cd = new ConnectionDetector(Send_Sms.this);
                    // get Internet status
                    isInternetPresent = cd.isConnectingToInternet();

                    if(edit_info.getText().toString().equals("")){

                        Toast.makeText(getApplicationContext(), " Please Type Information ", Toast.LENGTH_SHORT).show();
                    }else
                    {

                        if(isInternetPresent){
                            sendCompanyInfo();

                        }
                        else {

                            Toast.makeText(getApplicationContext(), " No Internet Connection ", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
    }


    public void sendCompanyInfo(){

        String message = edit_info.getText().toString().trim();

        String tag_json_obj = "json_obj_req";

        String LOGIN_URL = Static_Catelog.getLiveurl() + "cersei/feadmin/send_msg" ;
        String api_key = Static_Catelog.getStringProperty(getApplicationContext(),"api_key");
        String fe_id = Static_Catelog.getStringProperty(getApplicationContext(),"fe_id");


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Sending...");
        pDialog.show();


        JSONObject obj = new JSONObject();

        try {
            obj.put("api_key",api_key);
            obj.put("fe_id",fe_id);
            obj.put("message",message);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                LOGIN_URL,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Volley", "res :" + response);
                        pDialog.dismiss();
                        try {
                            Success = response.getBoolean("success");

                            if (Success){
                                String data = response.getString("data");
                                Toast.makeText(getApplicationContext(),data,Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Unauthorized access" ,Toast.LENGTH_LONG).show();

                                Intent ii = new Intent(Send_Sms.this, LoginPage.class);
                                startActivity(ii);
                                finish();

                            }
                            Log.i("Volley", "Boolean  :" + Success );
                           finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        VolleyLog.e("TAG", "Error: ", error.getMessage());

                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        pDialog.hide();

                    }
                });

        AppController.getInstance().addToRequestQueue(req, tag_json_obj);
    }
}

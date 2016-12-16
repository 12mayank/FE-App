package jlabs.fepp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jlabs-Win on 21/09/2016.
 */
public class FE_Form extends AppCompatActivity  {


    EditText name_fe;
    EditText fe_contact;
    EditText fe_email;
    EditText fe_user_name;
    EditText fe_pass ;
    Button submit ;

    Boolean Success = false ;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fe_form);

        name_fe = (EditText) findViewById(R.id.name_fe);
        fe_contact = (EditText) findViewById(R.id.fe_contact);
        fe_email = (EditText) findViewById(R.id.fe_email);
        fe_user_name = (EditText) findViewById(R.id.fe_user_name);
        fe_pass= (EditText) findViewById(R.id.fe_pass);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cd = new ConnectionDetector(FE_Form.this);
                isInternetPresent = cd.isConnectingToInternet();

                if(name_fe.getText().toString().equals("") || fe_contact.getText().toString().equals("") || fe_email.getText().toString().equals("") || fe_user_name.getText().toString().equals("") || fe_pass.getText().toString().equals("")){


                    Toast.makeText(getApplicationContext(),"Please fill All Fields", Toast.LENGTH_LONG).show();
                }else {


                    if(isInternetPresent){
                        send_fe_data();

                    }else {

                        Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }




    private void send_fe_data(){

        String tag_json_obj = "json_obj_req";

        String LOGIN_URL = Static_Catelog.getLiveurl() + "cersei/feadmin/create_user" ;

        String a = name_fe.getText().toString().trim();
        String b = fe_contact.getText().toString().trim();
        String c = fe_email.getText().toString().trim();
        String d = fe_user_name.getText().toString().trim();
        String e = fe_pass.getText().toString().trim();

        String api_key = Static_Catelog.getStringProperty(getApplicationContext(),"api_key");
        String fe_id = Static_Catelog.getStringProperty(getApplicationContext(),"fe_id");

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Sending...");
        pDialog.show();


        JSONObject obj = new JSONObject();

        try {
            obj.put("api_key",api_key);
            obj.put("fe_id",fe_id);
            obj.put("name",a);
            obj.put("contact",b);
            obj.put("email",c);
            obj.put("username",d);
            obj.put("password",e);
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


                               Toast.makeText(FE_Form.this,"FE Created successfully",Toast.LENGTH_LONG).show();
                                finish();

                            }else {
                                Toast.makeText(FE_Form.this,"Something wrong",Toast.LENGTH_LONG).show();

                                Intent ii = new Intent(FE_Form.this, LoginPage.class);
                                startActivity(ii);
                                finish();
                            }
                            Log.i("Volley", "Boolean  :" + Success);


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

package jlabs.fepp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jlabs.fepp.passwordedittext.PasswordEditText;

/**
 * Created by Jlabs-Win on 20/06/2016.
 */
public class LoginPage  extends AppCompatActivity implements View.OnClickListener {


    EditText user;
    PasswordEditText pass;
    Button btn;
    String username;
    String password;
    Activity context;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Boolean Success = false;
    String api_KEY, FE_ID,FE_type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        user = (EditText) findViewById(R.id.username);
        pass = (PasswordEditText) findViewById(R.id.password);
        btn = (Button) findViewById(R.id.btn);
        context=this;
        btn.setOnClickListener(this);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        cd = new ConnectionDetector(LoginPage.this);
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();
            if (user.getText().toString().equals("") || pass.getText().toString().equals("")){

                Toast.makeText(getApplicationContext(),"Please fill username and password ", Toast.LENGTH_LONG).show();

            }else {
                if (isInternetPresent) {

                    userLogin();
                } else {

                    Toast.makeText(getApplicationContext(), "Please check your internet connection ", Toast.LENGTH_LONG).show();
                }
            }
    }

    private void userLogin(){
        String tag_json_obj = "json_obj_req";

        String LOGIN_URL = Static_Catelog.getLiveurl() + "cersei/auth/fe/login" ;

        username = user.getText().toString().trim();
        Static_Catelog.setStringProperty(context,"user",username);
        if(username.equals("admin"))
            username="mayank";

        String  passwor = pass.getText().toString().trim();
        password = md5(passwor);
        Log.i("hiii", "Mayank :" + password);
        Log.i("hhhhh","bhai :"+username);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


          JSONObject obj = new JSONObject();
        try {
            obj.put("username", username);
            obj.put("password", password);
            obj.put("user_type","fe");
            Log.e("obj","obj"+obj);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
                                JSONObject Dataa = response.getJSONObject("data");
                                FE_ID  = Dataa.getString("fe_id");
                                api_KEY = Dataa.getString("api_key");
                                FE_type = Dataa.getString("type");
                                Static_Catelog.setStringProperty(context,"api_key",api_KEY);
                                Static_Catelog.setStringProperty(context,"fe_id",FE_ID);
                                Static_Catelog.setStringProperty(context,"fe_type",FE_type);
                                openProfile();
                                Log.i("Volley", "ans  :" + FE_ID + api_KEY);
                            }else {
                                Toast.makeText(LoginPage.this, "invalid  username and password", Toast.LENGTH_LONG).show();
                                user.setError(" username is incorrect");
                                pass.setError("Password is incorrect");
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

                        pDialog.hide();

                    }
                });

        AppController.getInstance().addToRequestQueue(req, tag_json_obj);
    }

    private void openProfile(){
        Intent intent = new Intent(this, MainActivity.class);

       // intent.putExtra("api_key", api_KEY);
       // intent.putExtra("FE_ID", FE_ID);
        startActivity(intent);
        context.finish();
    }

}

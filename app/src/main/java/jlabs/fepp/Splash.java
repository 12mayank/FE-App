package jlabs.fepp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import android.os.Handler;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by Mayank Sharma on 01/02/16.
 */
public class Splash extends AppCompatActivity {
    Intent myIntent;
    Activity context;
    public static int splash_time = 3000;
    String fe_id, api_key;

    private RelativeLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        context=this;
        coordinatorLayout = (RelativeLayout) findViewById(R.id.coordinatorLayout);
        fe_id = Static_Catelog.getStringProperty(getApplicationContext(),"fe_id");
        api_key = Static_Catelog.getStringProperty(getApplicationContext(),"api_key");
        setFullScreen();



            openProfile();

    }

    private void setFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    public void openProfile(){

        if(fe_id != null && api_key != null){

            myIntent = new Intent(Splash.this, MainActivity.class);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startActivity(myIntent);
                    context.finish();
                }
            }, splash_time);
        }else {

            myIntent = new Intent(Splash.this, LoginPage.class);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startActivity(myIntent);
                    context.finish();
                }
            }, splash_time);
        }
    }




}

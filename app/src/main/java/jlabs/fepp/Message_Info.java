package jlabs.fepp;

import android.content.Context;
import android.util.Log;

/**
 * Created by Jlabs-Win on 23/09/2016.
 */
public class Message_Info {

   private String msg ;

    private String date ;





    public String getInfo(){

        Log.i("TAGG", "getInfo "+ msg);
        return msg;
    }

    public void setInfo(String message){
        Log.i("TAGG", "setInfo "+ message);
        this.msg = message ;
    }

    public String getTime(){

        return date;
    }

    public void setTime(String date){

        this.date = date ;
    }
}

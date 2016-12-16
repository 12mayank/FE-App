package jlabs.fepp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jlabs-Win on 21/09/2016.
 */
public class Adapter_All_FE extends BaseAdapter {

    private Context context;

    List<Class_Fe> feList ;
   Boolean Success = false ;



    public Adapter_All_FE(Context context ,  List<Class_Fe> feList) {
        this.context = context;
        this.feList = feList ;

    }



    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView fe_id;
        TextView Name;
        TextView Contact;
        TextView Email;
        TextView Add_by;
        TextView Company_id;
        ImageView delete_fe;
        View Close;
    }

    public View getView(final int position, View gridView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder viewHolder;

        if (gridView == null) {

            gridView = inflater.inflate(R.layout.adap_all_fe, null);
            viewHolder = new ViewHolder();

            viewHolder.fe_id = (TextView) gridView.findViewById(R.id.tt11);
            viewHolder.Name = (TextView) gridView.findViewById(R.id.tt22);
            viewHolder.Contact = (TextView) gridView.findViewById(R.id.tt33);
            viewHolder.Email = (TextView) gridView.findViewById(R.id.tt44);
            viewHolder.Add_by = (TextView) gridView.findViewById(R.id.tt55);
            viewHolder.Company_id = (TextView) gridView.findViewById(R.id.tt66);
            viewHolder.delete_fe = (ImageView) gridView.findViewById(R.id.delete_fe);
            viewHolder.delete_fe.setTag(position);
            gridView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) gridView.getTag();
        }

        viewHolder.fe_id.setText(feList.get(position).getFE_id());
        viewHolder.Name.setText(feList.get(position).getFeName());
        viewHolder.Contact.setText(feList.get(position).getFeContact());
        viewHolder.Email.setText(feList.get(position).getFeEmail());
        viewHolder.Add_by.setText(feList.get(position).getFeAdd_by());
        viewHolder.Company_id.setText(feList.get(position).getFeCompany_id());

       viewHolder.delete_fe.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

             //  int idd = feList.get(position).id ;
               String fe_id = feList.get(position).getFE_id();

               deleteFe(fe_id,position);
           }
       });
        return gridView;
    }


    @Override
    public int getCount() {
        return feList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    public void deleteFe(final String remove_fe_id ,  final int position){

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Delete");
        builder1.setMessage("Do you want to delete this FE");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      //  Maindb sync = new Maindb(context);
                       // sync.delete_fe(idd);
                        String api_key = Static_Catelog.getStringProperty(context,"api_key");
                        String fe_id =  Static_Catelog.getStringProperty(context,"fe_id");
                        feList.remove(position);
                        delete_fe_from_server(remove_fe_id,api_key,fe_id);
                        notifyDataSetChanged();

                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void delete_fe_from_server(String remove_fe_id, String api_key, String fe_id){

        Log.i("TAG","Delete fe_id :"+ remove_fe_id);
        String tag_json_obj = "json_obj_req";
        String Delete_URL = Static_Catelog.getLiveurl() + "cersei/feadmin/remove" ;


        JSONObject obj = new JSONObject();

        try {
            obj.put("api_key",api_key);
            obj.put("fe_id",fe_id);
            obj.put("remove_id", remove_fe_id);
            Log.i("TAG", "hey :"+obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                Delete_URL,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Volley", "res :" + response);

                        try {
                            Success = response.getBoolean("success");
                            if (Success){

                                String Data = response.getString("data");
                              Toast.makeText(context, Data, Toast.LENGTH_LONG).show();

                            }else {

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

                    }
                });

        AppController.getInstance().addToRequestQueue(req, tag_json_obj);

    }

}
package jlabs.fepp;

/**
 * Created by Wadi on 19-12-2015.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Adapter_Offers extends BaseAdapter {
    private Context context;
    ArrayList<Class_Offers> offers;
    Boolean Success = false;



    public Adapter_Offers(Context context,ArrayList<Class_Offers> offers) {
        this.context = context;
        this.offers= offers;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    static class ViewHolder
    {
        TextView title;
        TextView points;
        TextView weight;
        TextView price;
        View new_offer;
        TextView approved;
        TextView soldout;
        TextView store;
        TextView remaining_code;
        ImageView delete_offer;
        TextView approve_button;

        View Close;
    }

    public View getView(final int position, View gridView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder viewHolder;

        if (gridView == null) {

            gridView = inflater.inflate(R.layout.adap_offers, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) gridView.findViewById(R.id.title);
            viewHolder.points = (TextView) gridView.findViewById(R.id.points);
            viewHolder.weight = (TextView) gridView.findViewById(R.id.weight);
            viewHolder.price = (TextView) gridView.findViewById(R.id.weight1);
            viewHolder.new_offer=gridView.findViewById(R.id.new_offer);
            viewHolder.approved = (TextView) gridView.findViewById(R.id.approved);
            viewHolder.soldout = (TextView) gridView.findViewById(R.id.soldout);
            viewHolder.store=(TextView) gridView.findViewById(R.id.name_of_store);
            viewHolder.remaining_code=(TextView) gridView.findViewById(R.id.remaining_offers);
            viewHolder.delete_offer = (ImageView) gridView.findViewById(R.id.deleteimg);
            viewHolder.approve_button = (TextView) gridView.findViewById(R.id.approve_button);
            viewHolder.approve_button.setTag(position);
            viewHolder.delete_offer.setTag(position);  // add this line
            gridView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) gridView.getTag();
        }
        viewHolder.title.setText(offers.get(position).item_code);
        viewHolder.points.setText(""+offers.get(position).point+"%");
     viewHolder.weight.setText(offers.get(position).dom);
        viewHolder.store.setText(offers.get(position).name);
        viewHolder.remaining_code.setText(""+offers.get(position).remaining_codes);
       int s = offers.get(position).id;
        Log.i("Main", "this is id :"+s);
    // convert timestamp to date format
  /**   String s =offers.get(position).dom;
        Log.i("giii","date"+s);
        long timestamp = Long.parseLong(s) * 1000;

        viewHolder.weight.setText(getDate(timestamp)); **/

        if (offers.get(position).approved) {
            viewHolder.approved.setText("Approved");
            viewHolder.approved.setTextColor(Color.parseColor("#2E8B57"));

        }
        else {
            viewHolder.approved.setText("Not Approved");
            viewHolder.approved.setTextColor(Color.parseColor("#FF0000"));

        }

        if(offers.get(position).IsOfferLocal) {
            viewHolder.new_offer.setVisibility(View.VISIBLE);

            viewHolder.approved.setText("Not Approved");
            viewHolder.approved.setTextColor(Color.RED);
        }
        else {
            viewHolder.new_offer.setVisibility(View.GONE);
        }
        if(offers.get(position).remaining_codes ==0) {
            viewHolder.soldout.setText("Sold Out");
            viewHolder.soldout.setTextColor(Color.RED);
        }
        else {
            viewHolder.soldout.setTextColor(Color.parseColor("#2E8B57"));
            viewHolder.soldout.setText("Available");
        }

         viewHolder.approve_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                // Maindb sync = new Maindb(context);
                 String offer_iddd = offers.get(position).offer_id;
                 String fe_iddd =  Static_Catelog.getStringProperty(context,"fe_id");
                 String api_keyyy =  Static_Catelog.getStringProperty(context,"api_key");
               //  sync.insertApproved(offer_iddd);
                 //offers.remove(position);
                 toApproveOffer(offer_iddd,fe_iddd,api_keyyy, position);
                viewHolder.approve_button.setVisibility(View.GONE);
//                 notifyDataSetChanged();
             }
         });


        viewHolder.delete_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("hii ","delete button position is:"+position);

                  int  idd =  offers.get(position).id;
                    Log.i("Main", "this is Local id :"+idd);

                String  offerID = offers.get(position).offer_id;
                Boolean offer_is_Local = offers.get(position).IsOfferLocal;

                String storeID = offers.get(position).vendor_id;

                Log.i("hii ","offer id@@@  :"+offerID);
                Log.i("hii ","ven id@@@  :"+storeID);
                deleteAlert(offerID,idd,position,offer_is_Local,storeID);

            }
        });


        return gridView;
    }

    @Override
    public int getCount() {
        return offers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public void deleteAlert(final String offerID, final int idd, final int position, final Boolean IsOfferLocal, final String storeId){


        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Delete");
        builder1.setMessage("Do you want to delete this offer");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

//                        Maindb sync = new Maindb(context);
//                        sync.deleteRow(offerID,idd,IsOfferLocal,storeId);
//                        offers.remove(position);
                        if(IsOfferLocal){

                            Maindb sync = new Maindb(context);
                            sync.deleteRow(offerID,idd,IsOfferLocal,storeId);
                            offers.remove(position);
                            notifyDataSetChanged();
                        }else {
                            String a =  Static_Catelog.getStringProperty(context,"fe_id");
                            String b =  Static_Catelog.getStringProperty(context,"api_key");
                            Delete_Offer_from_server(a,b,offerID,idd, IsOfferLocal,storeId, position);
                            notifyDataSetChanged();
                        }
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


    // to approve offer

    public void toApproveOffer (final String offer_id, String fe_id, String api_key, final int position){

        Log.i("TAG","Approve OfferID :"+ offer_id);
        String tag_json_obj = "json_obj_req";
        String approve_URL = Static_Catelog.getLiveurl() + "cersei/feadmin/approve" ;


        JSONObject obj = new JSONObject();

        try {
            obj.put("fe_id",fe_id);
            obj.put("api_key",api_key);
            obj.put("offer_id",offer_id);
            Log.i("TAG", "hey :"+obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("TAG","TT"+approve_URL +"gg" +obj);
        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                approve_URL,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Volley", "res :" + response);

                        try {
                            Success = response.getBoolean("success");
                            if (Success){
                                Maindb sync = new Maindb(context);
                                sync.insertApproved(offer_id);
                                offers.remove(position);

                                notifyDataSetChanged();
                                Toast.makeText(context," Offer Successfully Approved", Toast.LENGTH_SHORT).show();

                            }else {
                                Intent in = new Intent(context, LoginPage.class);
                                context.startActivity(in);
                                ((Approve_Offer)context).finish();
                                Toast.makeText(context," Token Expire Please Login Again", Toast.LENGTH_SHORT).show();


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



    public void Delete_Offer_from_server(String fe, String api , final String offer_id, final int idd, final Boolean IsOfferLocal, final String storeId , final int position){

        Log.i("TAG","Delete OfferID :"+ offer_id);
        String tag_json_obj = "json_obj_req";
        String Delete_URL = Static_Catelog.getLiveurl() + "cersei/fe/delete_offers" ;

        JSONObject obj = new JSONObject();

        try {
            obj.put("fe_id",fe);
            obj.put("api_key",api);
            obj.put("offer_id",offer_id);
            Log.i("TAG", "hey :"+obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("TAG","TT"+Delete_URL +"gg" +obj);
        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                Delete_URL,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Volley", "res :" + response);

                        try {
                            Success = response.getBoolean("success");
                            if (Success){

                                Maindb sync = new Maindb(context);
                                sync.deleteRow(offer_id,idd,IsOfferLocal,storeId);
                                offers.remove(position);
                                notifyDataSetChanged();
                        Toast.makeText(context,"Offer Successfully Deleted",Toast.LENGTH_LONG ).show();
                            }else {
                                Toast.makeText(context, "Token Expire Please Login Again", Toast.LENGTH_LONG).show();
                              Intent in = new Intent(context, LoginPage.class);
                              context.startActivity(in);
                                ((MainActivity)context).finish();
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
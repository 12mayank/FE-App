package jlabs.fepp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jlabs.fepp.pickerview.popwindow.DatePickerPopWin;


public class MainActivity extends AppCompatActivity implements  ViewPager.OnPageChangeListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    reload_data reload_data;
    current_reload_data current_reload_data;
    fe_message  fe_message;

    String url;
    Maindb synceddb;
    localdb localdb;
    FloatingActionButton Fab;
    LinearLayout LL;

    TextView t1, t2, t3, t4;
    ArrayList<Class_Offers> view_fragment ;


    ListView lv;
    int filter_type=0;
    String filter_param="";
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    public Menu menu;
    String today,api_KEY, fe_ID ;
    Boolean  Success = false ;
    AlertDialog.Builder dialogBuilder ;
    Activity context; ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        synceddb = new Maindb(this);
        localdb = new localdb(this);

        api_KEY = Static_Catelog.getStringProperty(getApplicationContext(),"api_key");
        fe_ID = Static_Catelog.getStringProperty(getApplicationContext(),"fe_id");
         Log.i("TAG", "api_key "+api_KEY + "fe_id "+fe_ID);
        //api_KEY = getIntent().getStringExtra("api_key");
       // fe_ID  = getIntent().getStringExtra("FE_ID");

        Fab = (FloatingActionButton) findViewById(R.id.Fab);
        LL = (LinearLayout) findViewById(R.id.LL);
        t1 = (TextView) findViewById(R.id.offer_Upload);
        t2 = (TextView) findViewById(R.id.sync_offer);
        t3 = (TextView) findViewById(R.id.sync_store);
        t4 = (TextView) findViewById(R.id.sync_items);

        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd = new ConnectionDetector(MainActivity.this);
                // get Internet status
                isInternetPresent = cd.isConnectingToInternet();
                if(isInternetPresent){
                    change_mode(true);
                    upload_offers();
                }
             else {
                    Toast.makeText(getApplicationContext(),"Please check your internet connection ", Toast.LENGTH_LONG).show();
                }
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        Create_Offer_SelectStore create_offer_selectStore = new Create_Offer_SelectStore();
        current_reload_data = create_offer_selectStore.return_current_interface();
        adapter.addFragment(create_offer_selectStore, "Create Offers");


        View_Fragment view_fragment = new View_Fragment();
        reload_data = view_fragment.return_reload_interface();
        adapter.addFragment(view_fragment, "View Offers");


        Company_info company_info = new Company_info();
        //fe_message = company_info.return_msg_interface();
        adapter.addFragment(company_info, "My Messages");
        Log.e("admin",""+Static_Catelog.getStringProperty(getApplicationContext(),"user"));


        if (Static_Catelog.getStringProperty(getApplicationContext(),"fe_type").equals("feadmin")){

            Admin  admin = new Admin();
            adapter.addFragment(admin,"Admin");
        }
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void change_mode(Boolean b) {
        if (b) {
            Fab.setVisibility(View.GONE);
            LL.setVisibility(View.VISIBLE);
            t1.setTextColor(Color.BLACK);
            t2.setTextColor(Color.BLACK);
            t3.setTextColor(Color.BLACK);
            t4.setTextColor(Color.BLACK);

        } else {
            Fab.setVisibility(View.VISIBLE);
            LL.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        try{

            if(position==1)
            {

                switch(filter_type)
                {
                    case 0:

                        reload_data.now_reload_data();
                       // fe_message.sync_fe_message();

                        break;
                    case 1:

                        reload_data.sold_out_filter();
                        break;
                    case 2:

                        reload_data.sold_in_filter();
                        break;
                    case 3:

                        reload_data.approved_filter();
                        break;
                    case 4:

                        reload_data.not_approved_filter();
                        break;
                    case 5:
                        reload_data.date_filter(filter_param);
                        break;
                    case 6:
                        reload_data.store_filter(filter_param);
                        break;
                    default : break;
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void upload_offers() {
        String tag_json_obj = "json_obj_post_offers";

        url = Static_Catelog.getLiveurl() + "cersei/fe/create_offers" ;

        Log.i("Myapp", "Calling url " + url);
        JSONObject topost = createJson();
        Log.i("Myapp", "Json " + topost);
        //  t1.setTextColor(Color.MAGENTA);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, topost,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        Log.i("Myapp", "Got Result " + response);
                        t1.setTextColor(Color.GRAY);
                        handleCreateOfferResult(response);
                        sync_offers();


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                // t1.setTextColor(Color.RED);
                //    Toast.makeText(getActivity().getBaseContext(), "Error Occurred During Uploading Offers", Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    private JSONObject createJson() {
        JSONObject json_topost = new JSONObject();
        JSONObject Json_new_offers = new JSONObject();
        JSONArray Json_new_offersArray = new JSONArray();
        JSONArray Json_old_offersArray = new JSONArray();
        JSONObject Json_Vendor = null;

        JSONArray Json_Vendor_Array = null;
        JSONArray Json_qrcodes = null;
        ArrayList<Class_Offers> new_create_offers = localdb.getAllLocalOffer();

        try {
            json_topost.put("fe_id", fe_ID);
            json_topost.put("api_key",api_KEY);
        } catch (Exception e) {
        }

        for (int i = 0; i < new_create_offers.size(); i++) {

            Json_new_offers = new JSONObject();
           int offer_id = new_create_offers.get(i).id;
           // ArrayList<Integer> stores = localdb.getAllLocalStoreFromLocalOffer(offer_id);
            ArrayList<String> stores = localdb.getAllLocalStoreFromLocalOfferFor_Json(offer_id);
            // Log.i("Myapp","Testing "+offer_id+" "+stores.size());
           // Json_Vendor_Array = new JSONArray();

            String store = new_create_offers.get(i).vendor_id;
                ArrayList<Barcodes> qrcodes = localdb.getAllBarcodeForLocal(offer_id, store);
                Json_qrcodes = new JSONArray();
                for (int k = 0; k < qrcodes.size(); k++)
                    Json_qrcodes.put(qrcodes.get(k).toString());
//                try {
//                    Json_Vendor.put("qrcodes", Json_qrcodes);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
             //  Json_Vendor_Object.put(Json_Vendor);

            try {
                String S=  new_create_offers.get(i).dom.replace('/', '-');
                String dateString2 = null;
                try {
                    Date date = new SimpleDateFormat("dd-MM-yyyy").parse(S);
                    dateString2 = new SimpleDateFormat("yyyy-MM-dd").format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
               // Json_new_offers.put("vendors", Json_Vendor);
                Json_new_offers.put("vid", new_create_offers.get(i).vendor_id);
                Json_new_offers.put("qrcodes", Json_qrcodes);
                Json_new_offers.put("offer_id", new_create_offers.get(i).id);
                Json_new_offers.put("item_id", new_create_offers.get(i).item_code);
                Json_new_offers.put("dom",dateString2);
                Json_new_offers.put("shelf_life", synceddb.getItemShelfLife(new_create_offers.get(i).item_code));
                Json_new_offers.put("cashback", new_create_offers.get(i).point);
                Json_new_offers.put("approved", new_create_offers.get(i).approved);
                Json_new_offers.put("name", new_create_offers.get(i).name);
                Json_new_offers.put("remaining_codes", new_create_offers.get(i).remaining_codes);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Json_new_offersArray.put(Json_new_offers);
        }
        try {
            json_topost.put("new_offer", Json_new_offersArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Class_Offers> old_data = localdb.modified_old_data();
        Log.i("Y", "array data :" + old_data.toString() + " size :" + old_data.size());

        for (int p =0 ; p < old_data.size(); p++){

                Json_new_offers = new JSONObject();
                ArrayList<Barcodes> qrcodes = localdb.getAllBarcodeFor_Json(old_data.get(p).offer_id, old_data.get(p).vendor_id);
                Json_qrcodes = new JSONArray();
                for (int k = 0; k < qrcodes.size(); k++) {
                    Json_qrcodes.put(qrcodes.get(k).toString());
                }
                    Log.i("Old Offers qrcodes : ", " view "+ Json_qrcodes.toString());

                    try {

                        String SS = old_data.get(p).dom.replace('/', '-');
                        String dateString3 = null;
                        try {
                            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(SS);
                            dateString3 = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Json_new_offers.put("offer_id", old_data.get(p).offer_id);
                        Json_new_offers.put("item_id", old_data.get(p).item_code);
                        Json_new_offers.put("dom", dateString3);
                        Json_new_offers.put("shelf_life", synceddb.getItemShelfLife(old_data.get(p).item_code));
                        Json_new_offers.put("cashback", old_data.get(p).point);
                        Json_new_offers.put("vid", old_data.get(p).vendor_id);
                        Json_new_offers.put("qrcodes", Json_qrcodes);
                        Log.i("Old Offers", "upload :"+Json_new_offers.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            Json_old_offersArray.put(Json_new_offers);

        }
        try {
            json_topost.put("old_offer", Json_old_offersArray);
            Log.i("Old ", "up :"+ json_topost.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json_topost;
    }


    private void sync_offers() {
        String tag_json_obj = "json_obj_req_get_offers";

       url = Static_Catelog.getLiveurl() +"cersei/fe/list_offer";

        t2.setTextColor(Color.MAGENTA);
        Log.i("Myapp", "Calling url " + url);

        JSONObject obj = new JSONObject();

        try {
            obj.put("api_key",api_KEY);
            obj.put("fe_id",fe_ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray arr = response.getJSONArray("data");
                            synceddb.deleteOffers();
                            for (int i = 0; i < arr.length(); i++) {
                                synceddb.create_sync_offer(arr.getJSONObject(i));
                                Log.i("Myapp", "Offers Times Found " + arr.getJSONObject(i).toString());

                            }

                            t2.setTextColor(Color.GRAY);
                            sync_stores();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                //  t2.setTextColor(Color.RED);

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    private void sync_stores() {

        String tag_json_obj = "json_obj_req_get_stores";
        url = Static_Catelog.getLiveurl() + "cersei/fe/list_retailer";
        t3.setTextColor(Color.MAGENTA);
        Log.i("Myapp", "Calling url " + url);

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
                            JSONObject obj = response.getJSONObject("data");
                            JSONArray arr = obj.getJSONArray("stores");
                           // JSONArray my_store_arr = obj.getJSONArray("mystore");
                            synceddb.deleteStores();
//                            ArrayList<Integer> my_store_int = new ArrayList<>();
//                            for (int k = 0; k < my_store_arr.length(); k++) {
//                                my_store_int.add(my_store_arr.getInt(k));
//                                Log.i("Myapp", "My Store Int " + my_store_int.contains(my_store_arr.getInt(k)));
//                            }
                            for (int i = 0; i < arr.length(); i++) {
                                synceddb.create_sync_store(arr.getJSONObject(i));
                                Log.i("Myapp", "Stores Times Found " + arr.getJSONObject(i).toString() + "Vendor ID" + arr.getJSONObject(i).getString("retailer_id") );
                            }


                            t3.setTextColor(Color.GRAY);
                            sync_Items();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                //  t3.setTextColor(Color.RED);


            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    private void sync_Items() {

        String tag_json_obj = "json_obj_req_get_items";
        url = Static_Catelog.getLiveurl() + "cersei/fe/list_item";
        t4.setTextColor(Color.MAGENTA);
        Log.i("Myapp", "Calling url " + url);
        Log.i("TAG", " checking Api,fe :"+fe_ID+ api_KEY);
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
                            JSONArray arr = response.getJSONArray("data");
                            synceddb.deleteItems();
                            for (int i = 0; i < arr.length(); i++) {
                                synceddb.create_sync_Items(arr.getJSONObject(i));
                                Log.i("Myapp", "Stores Times Found " + arr.getJSONObject(i).toString());

                                LL.setVisibility(View.GONE);
                                Fab.setVisibility(View.VISIBLE);
                            }

                            t4.setTextColor(Color.GRAY);
                            change_mode(false);
                            // reset_numbers();
                            current_reload_data.current_offer_reload_data();
                            reload_data.now_reload_data();
                           // fe_message.sync_fe_message();

                           menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.filter2));

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                //t4.setTextColor(Color.RED);
                VolleyLog.d("Error", "Error: " + error.getMessage());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void handleCreateOfferResult(JSONObject data) {
        Log.i("Myapp", "Inside ");
        // {"success": true, "data": {"new_offer": [1], "old_offer": [], "count": 1}}
        Boolean Success = false;
        try {
            Success = data.getBoolean("success");

            if(Success){

                Log.i("Myapp", "Inside1 " + Success);
                data = data.getJSONObject("data");
                JSONArray new_offers = data.getJSONArray("new_offer");
                JSONArray old_offers = data.getJSONArray("old_offer");

                synceddb.delete_added_store();

                for (int i = 0; i < new_offers.length(); i++) {
                    Log.i("Myapp", "Inside2 " + i);
                    Log.d("me " , "new offers id" + new_offers.get(i));
                    synceddb.deleteNewOffer((int) new_offers.get(i));
                }

                for (int i = 0; i < old_offers.length(); i++) {
                    Log.i("Myapp", "Inside3 " + i);
                    synceddb.deleteOldOffer(String.valueOf (old_offers.get(i)));
                }
            }else {

                Toast.makeText(getApplicationContext(),"Token is Expired Please Login Again",Toast.LENGTH_LONG).show();
                Intent ii  =  new Intent(MainActivity.this, LoginPage.class);
                startActivity(ii);
                finish();

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_star, menu);
        return super.onCreateOptionsMenu(menu);
    }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
     // Handle action bar item clicks here. The action bar will
     // automatically handle clicks on the Home/Up button, so long
     // as you specify a parent activity in AndroidManifest.xml.
         switch (item.getItemId()) {
             case R.id.out:
                    filter_type=1;
                 item.setChecked(true);
                 menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.filter3));
                    reload_data.sold_out_filter();
                    return true;

             case R.id.In:
                 filter_type=2;
                 item.setChecked(true);
                 menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.filter3));
                 reload_data.sold_in_filter();
                 return true;

             case R.id.approve:
                 filter_type=3;
                 item.setChecked(true);
                 menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.filter3));
                 reload_data.approved_filter();
                 return true;

             case R.id.not_approved:
                 filter_type=4;
                 item.setChecked(true);
                 menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.filter3));
                 reload_data.not_approved_filter();
                 return true;

             case R.id.date:
                 item.setChecked(true);
                 menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.filter3));
                 final SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd"); // to find current date
                 today = sdf.format(new Date());
                 DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(MainActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                     @Override
                     public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                      //   Toast.makeText(MainActivity.this, dateDesc, Toast.LENGTH_SHORT).show();

                       //  reload_data.date_filter(dateDesc);
                         getDate( dateDesc);

                         Log.i("MyDate", "here :" + dateDesc);
                     }
                 }).textConfirm("CONFIRM") //text of confirm button
                         .textCancel("CANCEL") //text of cancel button
                         .btnTextSize(16) // button text size
                         .viewTextSize(25) // pick view text size
                         .colorCancel(Color.parseColor("#999999")) //color of cancel button
                         .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                         .minYear(1990) //min year in loop
                         .maxYear(2030) // max year in loop
                         .dateChose(today) // date chose when init popwindow
                         .build();
                 pickerPopWin.showPopWin(MainActivity.this);

                 return true;

             case R.id.store:
                 item.setChecked(true);
                 menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.filter3));
                 AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                 LayoutInflater inflater = this.getLayoutInflater();

                 final View dialogView = inflater.inflate(R.layout.custom_alert_listview, null);
                 dialogBuilder.setView(dialogView);

                 dialogBuilder.setTitle(" Select Store ");


                lv  = (ListView) dialogView.findViewById(R.id.store_listview);

                 view_fragment = synceddb.getUniqueStoreName();

                  Log.i("array", "value" + view_fragment);

                Adapter_StoreName ada_storename = new Adapter_StoreName(getApplicationContext(),view_fragment);
                lv.setAdapter(ada_storename);

                  final AlertDialog as = dialogBuilder.create();
                 as.show();

                 lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String pp = view_fragment.get(position).name;

                        Log.i("mayank", "value selected" + pp);
                        reload_data.store_filter(pp);
                        filter_param=pp;
                        filter_type=6;
                         as.dismiss();
                    }
                });
                        return  true;
             case R.id.clear :
                          item.setChecked(true);
                  menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.filter2));
                          reload_data.now_reload_data();
                          filter_type=0;
                          return  true;

                 }
     //noinspection SimplifiableIfStatement
     return super.onOptionsItemSelected(item);
     }

    public  String  getDate (String dateDesc) {

        SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy");
        String reformattedStr = null;
        try {
            if (Integer.parseInt(dateDesc.split("-")[0]) > 13) {
                SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
                reformattedStr = newformat.format(oldformat.parse(dateDesc));
                Log.i("second", "here :" + reformattedStr);
                filter_type=5;
                filter_param=reformattedStr;
                reload_data.date_filter(reformattedStr);
                return reformattedStr;
            }
        } catch (Exception e) {
            return null;
        }

        return reformattedStr;
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           // onBackPressed();
            exit_pop_up();
            return true;

        }

        return super.onKeyDown(keyCode, event);
    }

    public void exit_pop_up(){

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("");
        dialogBuilder.setMessage("Do you want to exit ?");
        dialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                finish();
            }
        });

        dialogBuilder.setNegativeButton("Logout",null);
        final   AlertDialog b = dialogBuilder.create();
        b.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

                Button c = b.getButton(DialogInterface.BUTTON_NEGATIVE);

                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String a = "";

                        Static_Catelog.setStringProperty(MainActivity.this,"fe_id", null);
                        Static_Catelog.setStringProperty(MainActivity.this,"api_key", null);
                        dialog.dismiss();
                        finish();
                    }
                });

            }
        });
        b.show();
    }

}

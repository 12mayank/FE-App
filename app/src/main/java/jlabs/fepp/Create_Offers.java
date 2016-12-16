package jlabs.fepp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.app.AlertDialog;

import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import jlabs.fepp.pickerview.popwindow.DatePickerPopWin;

public class Create_Offers extends FragmentActivity   {

    Maindb db;
    String store_id,item_code, name;

    EditText dd,mm,yyyy;
    ImageView date_pop;
    TextView selected_point;
    String pic,item_name,weight;
    int price;
    ImageLoader imageLoader;
    EditText pnt;
    String today;
    NetworkImageView niv;
    Button feedMe;
    DateValidator valid ;

    String DOM1 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_offer);
        imageLoader = AppController.getInstance().getImageLoader();

       valid  = new DateValidator();
        date_pop = (ImageView) findViewById(R.id.date_pop);
        //spinner.setOnItemSelectedListener(this);
         dd= (EditText) findViewById(R.id.dd);
        mm= (EditText) findViewById(R.id.mm);
        yyyy= (EditText) findViewById(R.id.yyyy);
        selected_point = (TextView) findViewById(R.id.poi_en);

        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
        today = sdf.format(new Date());

        date_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(Create_Offers.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {

                        dd.getText().clear();
                        mm.getText().clear();
                        yyyy.getText().clear();

                        dd.setText("" + day);
                        mm.setText("" + month);
                        yyyy.setText("" + year);
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
                pickerPopWin.showPopWin(Create_Offers.this);
            }
        });

        dd.addTextChangedListener(textWatcher);
        mm.addTextChangedListener(textWatcher);
        yyyy.addTextChangedListener(textWatcher);
        store_id=getIntent().getStringExtra("store_id");
        item_code=getIntent().getStringExtra("item_id");
        name=getIntent().getStringExtra("name");
        pic = getIntent().getStringExtra("pic");
        item_name= getIntent().getStringExtra("item_name");
        weight = getIntent().getStringExtra("weight");
        price =  getIntent().getIntExtra("price",0);
        Log.d("Img","Pic of item"+pic);
        db=new Maindb(this);

        ((TextView)findViewById(R.id.itemcode)).setText(item_code);
        ((TextView)findViewById(R.id.store_id)).setText("" + store_id);
        ((TextView)findViewById(R.id.store_na)).setText(name);
         niv =  (NetworkImageView) findViewById(R.id.thumbnail);
        niv.setImageUrl(pic,imageLoader);
        niv.setDefaultImageResId(R.drawable.kl);
        niv.setErrorImageResId(R.drawable.no_img33);
        ((TextView)findViewById(R.id.item_Name)).setText(item_name);
        ((TextView)findViewById(R.id.weight)).setText("Weight: "+weight);
        ((TextView)findViewById(R.id.price)).setText("Price: "+price);

          feedMe = (Button)findViewById(R.id.doneButton);

//        feedMe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addtodb();
//            }
//        });


    }


    private TextWatcher textWatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


            if(dd.getText().toString().length() == 2){

                mm.requestFocus();
                mm.setCursorVisible(true);
            }

            if (mm.getText().toString().length() == 2){
                yyyy.requestFocus();
                yyyy.setCursorVisible(true);
            }
            if (yyyy.getText().toString().length() == 4){
                yyyy.requestFocus();
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

            if(dd.getText().toString().equals("")||mm.getText().toString().equals("")||yyyy.getText().toString().equals("")){



            }else {

                check(dd.getText().toString() + "/" + mm.getText().toString() + "/" + yyyy.getText().toString());
                String aa = dd.getText().toString() + "/" + mm.getText().toString() + "/" + yyyy.getText().toString();

                Boolean isValid = valid.validate(aa);

                if(isValid){
                    // String yourDate = dd.getText().toString() + "/" + mm.getText().toString() +"/"+ yyyy.getText().toString();
                    String yourDate = DOM1;
                    SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
                    String paya = sdf.format(new Date());

                    try {
                        Date d1 = sdf.parse(paya);
                        Date d2 = sdf.parse(yourDate);

                        if ((d2.compareTo(d1)) > 0) {

                            Toast.makeText(getApplicationContext(), "Invalid Date", Toast.LENGTH_SHORT).show();

                        } else{


                            numberShow();
                            feedMe.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addtodb();
                                }
                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else {

                    Toast.makeText(getApplicationContext(),"Invalid Date",Toast.LENGTH_SHORT).show();
                }


            }
        }
    };



    private void check(String DOM)
    {
        SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d2 = sdf.parse(DOM);
        DOM1 =  convertDateToString(d2);


        Log.d("TAG","check "+DOM1);
        Log.i("TAG", "checkMethod"+DOM1 +item_code + store_id);
        ArrayList<Class_Offers> offers = db.get_offer_for_itemcodeAndDOM(item_code, DOM1, store_id);
        ListView lv = (ListView) findViewById(R.id.offers);
        Adapter_Offers offers_adap = new Adapter_Offers(this,offers);
        lv.setAdapter(offers_adap);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    private void addtodb() {


                Class_Offers offer = new Class_Offers();
                offer.item_code = item_code;
                String selected;
                selected = pnt.getText().toString();
                //offer.dom = dd.getText().toString() + "/" + mm.getText().toString() +"/"+ yyyy.getText().toString();
                Log.d("TAG","DOM1"+DOM1);
                offer.dom = DOM1;
                offer.point = Integer.parseInt(selected);
                offer.name=name;
                offer.approved=false;
                offer.IsOfferLocal=true;
               // offer.deliverable = 0;
                offer.vendor_id = store_id;

                Log.i("Myapp","sad "+selected+" sdfdf "+offer.point+"vendor_id"+offer.vendor_id);
                Intent i = new Intent(this, ContinuousCaptureActivity.class);
                i.putExtra("offer", offer);
                i.putExtra("store_id", store_id);
                startActivity(i);
               finish();
            }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

     public void numberShow(){

         AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
         LayoutInflater inflater = this.getLayoutInflater();
         View dialogView = inflater.inflate(R.layout.number_dialog, null);
         dialogBuilder.setView(dialogView);

         TextView tv1 = (TextView) dialogView.findViewById(R.id.selectpt);
         pnt = (EditText) dialogView.findViewById(R.id.editPoint);


         dialogBuilder.setTitle("Select CashBack");

         dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

             @Override
             public void onClick(DialogInterface dialog, int which) {

                 String pnnt =  pnt.getText().toString();
                 selected_point.setText(pnnt+"%");
                 getWindow().setSoftInputMode(
                         WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                 );
                 dialog.dismiss();
             }
         });

         dialogBuilder.setNegativeButton("Edit",null);

         final   AlertDialog b = dialogBuilder.create();

         b.setOnShowListener(new DialogInterface.OnShowListener() {
             @Override
             public void onShow(DialogInterface dialog) {

                 Button c = b.getButton(DialogInterface.BUTTON_NEGATIVE);

                 c.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         pnt.setCursorVisible(true);
                         pnt.setFocusableInTouchMode(true);
                         pnt.setEnabled(true);
                         pnt.requestFocus();
                         InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                         imm.showSoftInput(pnt, InputMethodManager.SHOW_IMPLICIT);
                         int textLength = pnt.getText().length();
                         pnt.setSelection(textLength, textLength);

                     }
                 });

             }
         });

         b.show();
     }

    public String convertDateToString(Date indate)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");

        try{
            dateString = sdfr.format( indate );
        }catch (Exception ex ){
           // System.out.println(ex);
            ex.printStackTrace();
        }
        return dateString;
    }


}

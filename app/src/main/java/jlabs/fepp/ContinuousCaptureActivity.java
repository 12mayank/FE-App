package jlabs.fepp;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousCaptureActivity extends Activity {
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private CompoundBarcodeView barcodeView;
   String prevQrCode="";
    Maindb synceddb;
    Context context;
    String store_id;
    int total_qr_codes=0;
    ImageLoader imageLoader;
    Class_Offers offer;

    String img;
    EditText tv44,tv55;
    Button btc;
    ArrayList<String> qrcodes;
    ListView showList;
    DateValidator dateValidator;


    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                if(!prevQrCode.equals(result.getText()))
                {
                    prevQrCode=result.getText();
                    qrcodes.add(prevQrCode);

                      QrCode_Alert(prevQrCode);
                }

            }

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.continuous_scan);
        synceddb = new Maindb(this);
        dateValidator = new DateValidator();

        imageLoader = AppController.getInstance().getImageLoader();
        if(getIntent().hasExtra("store_id"))
        {
            store_id=getIntent().getStringExtra("store_id");
            offer= (Class_Offers) getIntent().getSerializableExtra("offer");

        }
        if(offer.IsOfferLocal)
        {
            Log.i("hello","offer_id"+ offer.id+ "namme "+ offer.name);
        }
        else
        {
            Log.i("hello","mr."+ offer.offer_id+ "namme "+ offer.name);
        }
        qrcodes=new ArrayList<>();

        barcodeView = (CompoundBarcodeView) findViewById(R.id.barcode_scanner);
       // barcodeView.decodeContinuous(callback);
        barcodeView.decodeSingle(callback);
        barcodeView.setStatusText("Scan Qr Code and It will be added to DB");
        btc = (Button) findViewById(R.id.btc);
        btc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlert();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    public void addQRcode(String Qrcode)
    {
        Boolean found=synceddb.AddNewBarcode(offer.offer_id, store_id, offer.IsOfferLocal, Qrcode);
        if(found)
            Toast.makeText(this, "Looks like QRcode Already Exists in Some Offer", Toast.LENGTH_LONG).show();
        else {
            total_qr_codes=total_qr_codes+1;
            Toast.makeText(this, "QR Code Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

       private void showAlert(){

           AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
           LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog, null);
           dialogBuilder.setView(dialogView);
           dialogBuilder.setTitle("Summary");

           TextView tv11 = (TextView) dialogView.findViewById(R.id.tv11);
           TextView tv22 = (TextView) dialogView.findViewById(R.id.tv22);
           TextView tv33 = (TextView) dialogView.findViewById(R.id.tv33);
           tv44 = (EditText) dialogView.findViewById(R.id.tv44);
           tv55 = (EditText) dialogView.findViewById(R.id.tv55);
            showList = (ListView)  dialogView.findViewById(R.id.showList);
           NetworkImageView niv = (NetworkImageView) dialogView.findViewById(R.id.niv);

           tv11.setText(String.valueOf(offer.item_code));
           tv22.setText(String.valueOf(qrcodes.size()));

           Class_Store store;
          // store = synceddb.getStoreInfo(stores);
           store = synceddb.getStoreInfo( store_id);
           tv33.setText(String.valueOf(store.title));

           tv44.setText(String.valueOf(offer.point));
           tv55.setText(String.valueOf(offer.dom));

           img = synceddb.getItemImage(offer.item_code);
           niv.setImageUrl(img,imageLoader);
           niv.setDefaultImageResId(R.drawable.kl);
           niv.setErrorImageResId(R.drawable.no_img33);


           ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, android.R.id.text1, qrcodes);
          showList.setAdapter(adapter);

           dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

               @Override
               public void onClick(DialogInterface dialog, int which) {


                   String date = tv55.getText().toString();
                   boolean isValid = dateValidator.validate(date);

                   if(isValid){
                       Date d =  changedate(date);
                       String changedDate =  convertStringToDate(d);
                       offer.dom = changedDate;
                       offer.point = Integer.parseInt(tv44.getText().toString());
                       //  offer.remaining_codes = qrcodes.size();
                       Class_Store store;
                       store = synceddb.getStoreInfo( store_id);
                       offer.name= String.valueOf(store.title);
                       Log.d("TAG","Hello vendor :"+offer.name);

                       Class_Offer_Store x=CheckOrCreateOffer();
                       switch(x.store_id)
                       {
                           case 1:

                               offer.IsOfferLocal=false;
                               offer.offer_id=x.offer_id;

                               break;
                           case 2:
                           case 3:
                               offer.IsOfferLocal=true;
                               offer.offer_id=x.offer_id;
                               break;
                       }
                       for(int i=0;i<qrcodes.size();i++)
                       {
                           addQRcode(qrcodes.get(i));
                       }
                       finish();

                   }else {

                       Toast.makeText(getApplicationContext(),"Invalid Date", Toast.LENGTH_LONG).show();
                   }


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

                           tv55.setCursorVisible(true);
                           tv55.setFocusableInTouchMode(true);
                           tv55.setEnabled(true);

                           tv44.setCursorVisible(true);
                           tv44.setFocusableInTouchMode(true);
                           tv44.setEnabled(true);
                       }
                   });

               }
           });
           b.show();

       }


    public  Class_Offer_Store CheckOrCreateOffer(){
        return synceddb.create_local_offer(store_id, offer);
    }


    public void QrCode_Alert( String prevQrCode){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.qrcodes_alert, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("QR Codes ");
        dialogBuilder.setPositiveButton("Undo",null);

       final TextView total = (TextView) dialogView.findViewById(R.id.total_qr_codes);
       final TextView latest = (TextView) dialogView.findViewById(R.id.latestQRCodes);


             latest.setText(prevQrCode);
             total.setText(""+qrcodes.size());

        dialogBuilder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                barcodeView.decodeSingle(callback);

            }
        });

        final AlertDialog b = dialogBuilder.create();
        b.show();

        b.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                   qrcodes.remove(qrcodes.size() - 1);
                  String last = qrcodes.get(qrcodes.size() - 1);
                   latest.setText(last);
                    total.setText("" + qrcodes.size());
               } catch (Exception e) {
                   e.printStackTrace();
                  latest.setText("NULL");
                 total.setText("0");
               }

            }
        });
    }

// function for change string to date
    public Date changedate(String a){

        Date d2 = null;
        SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");

        try {
            d2 = sdf.parse(a);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d2;
    }

//  function for change date  to string
    public String convertStringToDate(Date indate)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");

        try{
            dateString = sdfr.format( indate );


        }catch (Exception ex ){
            ex.printStackTrace();
        }
   return dateString;
    }

}

package jlabs.fepp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jlabs-Win on 27/07/2016.
 */
public class View_QrCode_Adapter extends BaseAdapter {

    private Context context;
    ArrayList<Barcodes> barcodes;

    public View_QrCode_Adapter(Context context, ArrayList<Barcodes> barcodes) {

        this.context = context;
        this.barcodes = barcodes;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    static class ViewHolder {

        TextView qrcodes;
        ImageView delete_qrcode;
    }

    public View getView(final int position, View gridView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder viewHolder;

        if (gridView == null) {

            gridView = inflater.inflate(R.layout.qr_codes_view, null);
            viewHolder = new ViewHolder();

            viewHolder.qrcodes = (TextView) gridView.findViewById(R.id.view_qr);
            viewHolder.delete_qrcode = (ImageView) gridView.findViewById(R.id.delete_qrcode);
            viewHolder.delete_qrcode.setTag(position);
            gridView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) gridView.getTag();
        }

        viewHolder.qrcodes.setText(barcodes.get(position).QR_code);

        viewHolder.delete_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               int  idd =  barcodes.get(position).id;

                Log.d("Tag","QR CODES ID :"+idd);
                deleteAlert(idd,position);
            }
        });
        return gridView;
    }


    @Override
    public int getCount() {
        return barcodes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public void deleteAlert(final int idd, final int position){


        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Delete");
        builder1.setMessage("Do you want to delete this QR Code");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Maindb sync = new Maindb(context);
                        sync.delete_QRCodes(idd);
                        barcodes.remove(position);
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
}

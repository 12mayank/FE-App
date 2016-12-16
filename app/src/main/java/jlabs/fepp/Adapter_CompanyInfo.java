package jlabs.fepp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jlabs-Win on 01/09/2016.
 */
public class Adapter_CompanyInfo extends BaseAdapter {

   private   Context context;
   // private Company_info  com_info ;
    List<Message_Info> information;



    public Adapter_CompanyInfo(Context context, List<Message_Info> information) {

         this.context = context ;
        this.information = information;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    static class ViewHolder {
        TextView detailInfo;
        FrameLayout fema;
        TextView sms_date ;


    }


    public View getView(final int position, View gridView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder viewHolder;

        if (gridView == null) {

            gridView = inflater.inflate(R.layout.adapter_info, null);
            viewHolder = new ViewHolder();

            viewHolder.detailInfo = (TextView) gridView.findViewById(R.id.detailInfo);
            viewHolder.fema = (FrameLayout) gridView.findViewById(R.id.fema);
            viewHolder.sms_date = (TextView) gridView.findViewById(R.id.sms_date);


            gridView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) gridView.getTag();
        }

        Log.i("tag", "message Info :" + information.get(position).getInfo());
        Log.i("tag", "message date  :" + information.get(position).getTime());
       viewHolder.detailInfo.setText(information.get(position).getInfo());
        viewHolder.sms_date.setText(information.get(position).getTime());

        final String a = information.get(position).getInfo();
         final String b = information.get(position).getTime();

        viewHolder.fema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pop_up_dialog(a,b);

            }
        });

        return gridView;

    }


    @Override
    public int getCount() {
        return information.size();
    }

    @Override
    public Object getItem(int position) {
        return information.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    private void pop_up_dialog(String a, String b){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
       // LayoutInflater inflater = context.getLayoutInflater();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.pop_up_message, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Message");

        TextView tv_msg = (TextView) dialogView.findViewById(R.id.pop_up_message);
        TextView tv_date = (TextView) dialogView.findViewById(R.id.pop_up_date);

        tv_msg.setMaxLines(Integer.MAX_VALUE);
        tv_msg.setText(a);
        tv_date.setText(b);


        dialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        final AlertDialog bbb = dialogBuilder.create();
        bbb.show();

    }
}

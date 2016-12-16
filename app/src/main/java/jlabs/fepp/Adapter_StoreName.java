package jlabs.fepp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jlabs.fepp.Class_Offers;
import jlabs.fepp.R;

/**
 * Created by Jlabs-Win on 26/05/2016.
 */
public class Adapter_StoreName extends BaseAdapter {

    private Context context;
   ArrayList<Class_Offers> storename;


    public Adapter_StoreName(Context context, ArrayList<Class_Offers> storename) {

        this.context = context;
        this.storename = storename;
    }


    static class ViewHolder {

        TextView store_name;


        View Close;
    }

    public View getView(int position, View gridView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder;


        if (gridView == null) {

            gridView = inflater.inflate(R.layout.adap_storename, null);
            viewHolder = new ViewHolder();

            viewHolder.store_name = (TextView) gridView.findViewById(R.id.storeTextview);
            gridView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) gridView.getTag();
        }
        viewHolder.store_name.setText(storename.get(position).name);

        return gridView;
    }

    @Override
    public int getCount() {
        return storename.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



}
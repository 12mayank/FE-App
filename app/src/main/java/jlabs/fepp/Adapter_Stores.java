package jlabs.fepp;

/**
 * Created by Pradeep on 19-12-2015.
 */
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;


public class Adapter_Stores extends BaseAdapter {
    private Context context;
    ArrayList<Class_Store> stores;



    public Adapter_Stores(Context context,ArrayList<Class_Store> stores) {
        this.context = context;
        this.stores=stores;
    }

    static class ViewHolder
    {
        TextView title;
        TextView address;
        View mystore;
        LinearLayout frame;
    }

    public View getView(final int position, View gridView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder;

        if (gridView == null) {

            gridView = inflater.inflate(R.layout.adap_stores, null);
            viewHolder = new ViewHolder();


            viewHolder.title = (TextView) gridView.findViewById(R.id.title_shop);
            viewHolder.address = (TextView) gridView.findViewById(R.id.add);
            viewHolder.frame = (LinearLayout) gridView.findViewById(R.id.offer_view);

            viewHolder.mystore = gridView.findViewById(R.id.mystore);


            gridView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) gridView.getTag();
        }
        viewHolder.title.setText(stores.get(position).title);
        viewHolder.address.setText(stores.get(position).address);
         Log.i("NEW :","StoreID :"+stores.get(position).store_id);

//        if(stores.get(position).mystore)
//            viewHolder.mystore.setVisibility(View.VISIBLE);
//        else
//            viewHolder.mystore.setVisibility(View.GONE);

        return gridView;
    }

    @Override
    public int getCount() {
        return stores.size();
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
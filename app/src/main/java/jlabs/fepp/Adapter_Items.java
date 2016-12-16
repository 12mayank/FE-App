package jlabs.fepp;

/**
 * Created by Wadi on 19-12-2015.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;


public class Adapter_Items extends BaseAdapter {
    private Context context;
    ArrayList<Class_Items> items;
  ImageLoader imageLoader = AppController.getInstance().getImageLoader();



    public Adapter_Items(Context context, ArrayList<Class_Items> items) {
        this.context = context;
        this.items = items;
    }

    static class ViewHolder
    {
        TextView title;
        TextView address;
        NetworkImageView imagenet;
        ImageLoader imageLoader ;
    }

    public View getView(int position, View gridView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder;

        if (gridView == null) {

            gridView = inflater.inflate(R.layout.adap_item_row, null);
            viewHolder = new ViewHolder();

            if (imageLoader == null)
                viewHolder.imageLoader = AppController.getInstance().getImageLoader();
                viewHolder.imagenet = (NetworkImageView) gridView.findViewById(R.id.thumbnail);

                viewHolder.title = (TextView) gridView.findViewById(R.id.title12);
                viewHolder.address = (TextView) gridView.findViewById(R.id.detail);


            gridView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) gridView.getTag();
        }
        viewHolder.title.setText(items.get(position).Item_name);
        viewHolder.address.setText("Weight: "+items.get(position).Weight+" , Price: "+items.get(position).price);
      String ih = items.get(position).Image;
      //  String ih = items.get(position).Desc ;
        Log.i("Hel","this is image:"+ ih);

      viewHolder.imagenet.setImageUrl(ih,imageLoader);
        viewHolder.imagenet.setDefaultImageResId(R.drawable.kl);
       viewHolder.imagenet.setErrorImageResId(R.drawable.no_img33);

        return gridView;
    }

    @Override
    public int getCount() {
        return items.size();
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
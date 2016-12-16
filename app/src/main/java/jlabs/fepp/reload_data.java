package jlabs.fepp;

/**
 * Created by Pradeep on 2/8/2016.
 */
public interface reload_data
{
    void now_reload_data();

    void sold_out_filter();

    void sold_in_filter();

    void approved_filter();

    void not_approved_filter();

    void date_filter(String reformattedStr);

    void store_filter(String pp);



}

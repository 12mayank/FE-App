package jlabs.fepp;

import java.io.Serializable;

/**
 * Created by JussConnect on 7/2/2015.
 */
public class Class_Offers implements Serializable{
    int id;
    public boolean IsOfferLocal;
    public String offer_id;
    public String item_code;
    public String dom;
    public Integer point;
    public Integer deliverable;
    public Boolean approved;
    public String sold_out;
    public String name;
    public  Integer remaining_codes;
    public  String vendor_id;


    Class_Offers(){
        offer_id = new String();
        item_code = new String();
        dom = new String();
        point = new Integer("0");
        deliverable = new Integer("0");
        approved= false;
        sold_out = new String();
        name = new String();
        remaining_codes= new Integer("0");
        vendor_id = new String();
        IsOfferLocal=false;
    }
}

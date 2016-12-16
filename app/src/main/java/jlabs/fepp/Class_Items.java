package jlabs.fepp;

/**
 * Created by JussConnect on 7/2/2015.
 */
public class Class_Items {
    int id;
    public String companyId;
    public Integer Shelf_life;
    public String Item_code;

    public String Barcode;
    public String Item_name;
    public Integer price;
    public String Weight;
    public String Desc;
    public String Image;

    Class_Items(){
        companyId = new String();
        Shelf_life = new Integer("0");
        Item_code = new String();

        Barcode= new String();
        Item_name= new String();
        price= new Integer("0");
        Weight= new String();
        Desc= new String();
        Image = new String();
    }

    public String toString()
    {
        return "Company Id "+companyId+" "+Shelf_life+" "+Item_code+" "+Barcode+" "+Item_name+" "+price+" "+Weight+" "+Desc+" "+Image+"";
    }
}

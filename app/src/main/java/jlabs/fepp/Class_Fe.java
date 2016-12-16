package jlabs.fepp;

/**
 * Created by Jlabs-Win on 21/09/2016.
 */
public class Class_Fe {

    int id;
    public String FE_id;
    public String Name ;
    public String Contact ;
    public String  Email ;
    public String Add_by;
    public String Company_id;


//    Class_Fe(){
//
//        FE_id = new String();
//        Name = new String();
//        Contact = new String();
//        Email = new String();
//        Add_by = new String();
//        Company_id = new String();
//    }


    public String getFE_id(){

        return FE_id;
    }

    public void setFE_id(String FE_id){

        this.FE_id = FE_id ;
    }



    public String getFeName(){

        return Name;
    }

    public void setFeName(String Name){

        this.Name = Name ;
    }


    public String getFeContact(){

        return Contact;
    }


    public void setFeContact(String Contact){

        this.Contact = Contact ;
    }


    public String getFeEmail(){

        return Email;
    }

    public void setFeEmail(String Email){

        this.Email = Email ;
    }

    public String getFeAdd_by(){

        return Add_by;
    }
    public void setFeAdd_by(String Add_by){

        this.Add_by = Add_by ;
    }


    public String getFeCompany_id(){

        return Company_id;
    }

    public void setFeCompany_id(String Company_id){

        this.Company_id = Company_id ;
    }
}

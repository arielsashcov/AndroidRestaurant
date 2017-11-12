package cgodin.qc.ca.androidrestaurant.model;

/**
 * Created by Ariel S on 2017-10-29.
 */

public class User {

    private int id;
    private int utype;
    private String uid;
    private String name;
    private String email;
    private String password;


    public User(){

    }

    /* Getter and setters */
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUtype() { return utype; }

    public void setUtype(int utype) { this.utype = utype; }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}

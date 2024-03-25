package com.example.DogBreeds;

public class User {
    String name, phone, email, zilla, upozilla,union,image,date;

    public User() {

    }

    public User(String name, String phone, String email, String zilla, String upozilla,String union,String image,String date) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.zilla = zilla;
        this.upozilla = upozilla;
        this.union=union;
        this.image=image;
        this.date=date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getZilla() {
        return zilla;
    }

    public String getUpozilla() {
        return upozilla;
    }

    public String getUnion() {
        return union;
    }
    public String getImage(){return image;}

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setZilla(String zilla) {
        this.zilla = zilla;
    }

    public void setUpozilla(String upozilla) {
        this.upozilla = upozilla;
    }

    public void setUnion(String union) {
        this.union = union;
    }
    public void setImage(String image){this.image=image;}

}
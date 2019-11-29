package com.example.findmyband;

import android.net.Uri;

public class cards {
//    private String userId;
    private String name;
    private String lName;
    private String image;
    private String location;
    private String primaryInstrument;
    private String secondaryInstrument;
    private String genre1;
    private String genre2;
    private String genre3;
    private String bio;


    public cards (String name,String lName, String imageUrl,String location,String primaryInstrument,String secondaryInstrument,String genre1,String genre2, String genre3,String bio) {

        this.name = name;
        this.lName = lName;
        this.image = imageUrl;
        this.location = location;
        this.primaryInstrument = primaryInstrument;
        this.secondaryInstrument = secondaryInstrument;
        this.genre1 = genre1;
        this.genre2 = genre2;
        this.genre3 = genre3;
        this.bio = bio;
    }

//    public String getUserId(){
//        return userId;
//    }
//
//    public void setUserId(String userId){
//        this.userId = userId;
//    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getLName(){
        return lName;
    }

    public void setLName(){
        this.lName = lName;
    }

    public String getImageUrl(){
        return image;
    }

    public void setImageUrl(){
        this.image = image;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getPrimaryInstrument(){
        return primaryInstrument;
    }

    public void setPrimaryInstrument(String primaryInstrument){
        this.primaryInstrument = primaryInstrument;
    }

    public String getSecondaryInstrument(){
        return secondaryInstrument;
    }

    public void setSecondaryInstrument(String secondaryInstrument){
        this.secondaryInstrument = secondaryInstrument;
    }

    public String getGenre1(){
        return genre1;
    }

    public void setGenre1(String genre1){
        this.genre1 = genre1;
    }

    public String getGenre2(){
        return genre2;
    }

    public void setGenre2(String genre2){
        this.genre2 = genre2;
    }

    public String getGenre3(){
        return genre3;
    }

    public void setGenre3(String genre3){
        this.genre3 = genre3;
    }

    public String getBio(){
        return bio;
    }

    public void setBio(){
        this.bio = bio;
    }

}

package com.example.skinify.Model;


import com.google.firebase.database.Exclude;

public class Teacher {
    private String itemName;
    private String itemImage;
    private String key;
    private String collection;
    private String game;
    private String grade;
    private Long requiredPoints;
    private int position;

    public Teacher() {
        //empty constructor needed
    }
    public Teacher (int position){
        this.position = position;
    }
    public Teacher(String itemName, String itemImage,String collection ,String game,String grade,Long requiredPoints) {
        if (itemName.trim().equals("")) {
            itemName = "No Name";
        }
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.game = game;
        this.grade=grade;
        this.collection=collection;
        this.requiredPoints=requiredPoints;
    }







    public Long getrequiredPoints() {
        return requiredPoints;
    }
    public void setRequiredPoints(Long requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public String getgrade() {
        return grade;
    }
    public void setgrade(String grade) {
        this.grade = grade;
    }


    public String getgame() {
        return game;
    }
    public void setgame(String game) {
        this.game = game;
    }

    public String getitemName() {
        return itemName;
    }
    public void setitemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCollection() {
        return collection;
    }
    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getitemImage() {
        return itemImage;
    }
    public void setitemImage(String itemImage) {
        this.itemImage = itemImage;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}

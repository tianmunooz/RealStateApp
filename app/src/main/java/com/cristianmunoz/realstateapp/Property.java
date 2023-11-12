package com.cristianmunoz.realstateapp;

public class Property {
    private String title;
    private String price;
    private String location;
    private String size;
    private String imageUrl;

    public Property(String title, String price, String location, String size, String imageUrl) {
        this.title = title;
        this.price = price;
        this.location = location;
        this.size = size;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getSize() {
        return size;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


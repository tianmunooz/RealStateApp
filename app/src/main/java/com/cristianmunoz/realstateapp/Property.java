package com.cristianmunoz.realstateapp;

public class Property {
    private String title;
    private Long price; // Assuming price is a number in Firestore
    private String location;
    private Long size; // Assuming size is a number in Firestore
    private String image_url; // Changed to match Firestore field name
    private String url; // Assuming there is a url field in Firestore

    private String province; // Make sure this field exists in Firestore

    // No-argument constructor (required for Firestore)
    public Property() {
    }

    // Constructor with arguments
    public Property(String title, Long price, String location, Long size, String image_url, String url) {
        this.title = title;
        this.price = price;
        this.location = location;
        this.size = size;
        this.image_url = image_url;
        this.url = url;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public Long getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public Long getSize() {
        return size;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getUrl() {
        return url;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}

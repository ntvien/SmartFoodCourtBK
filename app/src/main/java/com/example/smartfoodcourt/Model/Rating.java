package com.example.smartfoodcourt.Model;

public class Rating {

    private String phone;
    private String rateValue;
    private String comment;

    public Rating() {
    }

    public Rating(String phone, String rateValue, String comment) {
        this.phone = phone;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}

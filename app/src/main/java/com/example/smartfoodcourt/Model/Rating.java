package com.example.smartfoodcourt.Model;

public class Rating {

    private String rateValue;
    private String comment;

    public Rating() {
    }

    public Rating(String rateValue, String comment) {
        this.rateValue = rateValue;
        this.comment = comment;
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

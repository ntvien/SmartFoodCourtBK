package com.example.smartfoodcourt.Model;

import java.util.Map;

public class Comment {
    public Comment(float ratingValue, String comment, String name, String id, Map<String, Object> commentTimeStamp) {
        this.ratingValue = ratingValue;
        this.comment = comment;
        this.name = name;
        this.id = id;
        this.commentTimeStamp = commentTimeStamp;
    }
    public Comment(){}
    private float ratingValue;
    private String comment, name, id;

    public float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId(String email) {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getCommentTimeStamp() {
        return commentTimeStamp;
    }

    public void setCommentTimeStamp(Map<String, Object> commentTimeStamp) {
        this.commentTimeStamp = commentTimeStamp;
    }

    private Map<String, Object> commentTimeStamp;


}

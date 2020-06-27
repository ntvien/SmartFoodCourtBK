package com.example.smartfoodcourt.ui.food;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartfoodcourt.Model.Comment;

public class FoodViewModel extends ViewModel {

    private MutableLiveData<Comment> mutableLiveDataComment;

    public void setCommentModel(Comment comment){
        if(mutableLiveDataComment != null)
            mutableLiveDataComment.setValue(comment);
    }

    public MutableLiveData<Comment> getMutableLiveDataComment(){
        return mutableLiveDataComment;
    }

    public FoodViewModel(){
        mutableLiveDataComment = new MutableLiveData<>();
    }


}
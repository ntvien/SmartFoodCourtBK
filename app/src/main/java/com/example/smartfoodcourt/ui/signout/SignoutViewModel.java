package com.example.smartfoodcourt.ui.signout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignoutViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public SignoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is signout fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

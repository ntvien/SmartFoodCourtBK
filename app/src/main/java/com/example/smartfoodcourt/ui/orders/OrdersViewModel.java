package com.example.smartfoodcourt.ui.orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrdersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OrdersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Orders fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
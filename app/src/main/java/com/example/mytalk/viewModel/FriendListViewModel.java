package com.example.mytalk.viewModel;

import android.content.Context;
import android.widget.VideoView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class FriendListViewModel extends ViewModel {

    public MutableLiveData <ArrayList>users= new MutableLiveData<>();

    public void add(ArrayList list){//json으로 값 바꿔서 가져오기
        if(list!=null){
            users.setValue(list);
        }
    }


}

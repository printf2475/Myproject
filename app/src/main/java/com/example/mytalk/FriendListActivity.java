package com.example.mytalk;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mytalk.databinding.ActivityFriendListBinding;
import com.example.mytalk.viewModel.FriendListViewModel;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);


        kakaoApi kakaoApi = new kakaoApi(this);
        ActivityFriendListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_friend_list);
        FriendListViewModel FLViewModel = new ViewModelProvider(this).get(FriendListViewModel.class);

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                gotoActivity(LoginActivity.class);
            }
        });

        FLViewModel.add(kakaoApi.getFriend());
        FLViewModel.users.observe(this, new Observer<ArrayList>() {
            @Override
            public void onChanged(ArrayList list) {
                //Ui 변경 프레그먼트 ,리사이클러뷰 만들고 구현

            }
        });


    }

    public void logout(){
        UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Log.i("KAKAO_API", "로그아웃 완료");
                    }
                });

    }

    public void gotoActivity(Class<?> cls){
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();

    }

}



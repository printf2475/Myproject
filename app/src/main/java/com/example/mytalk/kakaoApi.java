package com.example.mytalk;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.AppFriendOrder;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;
import com.kakao.util.exception.KakaoException;

import java.util.ArrayList;


public class kakaoApi {

    private SessionCallback sessionCallback;
    Context mContext;

    AppFriendContext context =
            new AppFriendContext(AppFriendOrder.NICKNAME, 0, 3, "asc");

    private String myNicname=null;
    private String myProfileImage;
    private ArrayList friends = new ArrayList();
    private String uuid;

    public kakaoApi(Context mContext){
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
        this.mContext=mContext;

    }


    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            if(myNicname==null){
                getMyKakaoProfile();
                getFriendsKakaoProfile();
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            toastMessege("로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요");
            Log.e("KAKAO_API", "로그인도중 오류"+e.toString());
        }
    }

    public void getMyKakaoProfile(){
        KakaoTalkService.getInstance()
                .requestProfile(new TalkResponseCallback<KakaoTalkProfile>() {
                    @Override
                    public void onNotKakaoTalkUser() {
                        Log.e("KAKAO_API", "카카오톡 사용자가 아님");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "카카오톡 프로필 조회 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(KakaoTalkProfile result) {
                        Log.i("KAKAO_API", "카카오톡 닉네임: " + result.getNickName());
                        Log.i("KAKAO_API", "카카오톡 프로필이미지: " + result.getProfileImageUrl());
                        myNicname=result.getNickName();
                        myProfileImage=result.getProfileImageUrl();

                    }
                });
    }


    public void getFriendsKakaoProfile(){

        KakaoTalkService.getInstance()
                .requestAppFriends(context, new TalkResponseCallback<AppFriendsResponse>() {
                    @Override
                    public void onNotKakaoTalkUser() {
                        Log.e("KAKAO_API", "카카오톡 사용자가 아님");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "친구 조회 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(AppFriendsResponse result) {

                        for (AppFriendInfo friend : result.getFriends()) {
                            Log.i("KAKAO_API", "친구 조회 성공");

                            Log.i("KAKAO_API", "친구정보="+friend.toString());

                            friends.add(friend.toString());
                            uuid = friend.getUUID();     // 메시지 전송 시 사용
                        }
                    }
                });
    }

    public void toastMessege(String msg){
        Toast.makeText(mContext,msg, Toast.LENGTH_SHORT).show();

    }
}

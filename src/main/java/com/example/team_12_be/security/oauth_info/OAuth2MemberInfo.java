package com.example.team_12_be.security.oauth_info;

public interface OAuth2MemberInfo {
    String getProviderId(); //공급자 아이디 ex) kakao , naver

    String getProvider(); //공급자 ex) kakao , naver

    String getName(); //사용자 이름 ex) 김한석

    String getEmail();  //사용자 이메일
}

package com.mediacare.user.service;

import com.mediacare.user.dto.request.UserLoginRequest;
import com.mediacare.user.dto.request.UserRegisterRequest;
import com.mediacare.user.dto.response.UserInfoResponse;

public interface AuthService {
    UserInfoResponse login(UserLoginRequest request);

    void register(UserRegisterRequest request);
}

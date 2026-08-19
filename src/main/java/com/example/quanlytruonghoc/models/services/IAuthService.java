package com.example.quanlytruonghoc.models.services;


import com.example.quanlytruonghoc.models.data.req.auth.ActiveUserReq;
import com.example.quanlytruonghoc.models.data.req.auth.LoginReq;
import com.example.quanlytruonghoc.models.data.req.auth.RegisterReq;
import com.example.quanlytruonghoc.models.data.res.LoginRes;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {
    void register(RegisterReq req);
    LoginRes login(LoginReq req);
    String activeUser(ActiveUserReq req);
    void logout(HttpServletRequest request);
}

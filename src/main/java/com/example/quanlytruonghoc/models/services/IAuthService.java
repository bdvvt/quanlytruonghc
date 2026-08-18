package com.example.quanlytruonghoc.models.services;


import com.example.quanlytruonghoc.models.data.req.ActiveUserReq;
import com.example.quanlytruonghoc.models.data.req.LoginReq;
import com.example.quanlytruonghoc.models.data.req.RegisterReq;
import com.example.quanlytruonghoc.models.data.res.LoginRes;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {
    void register(RegisterReq req);
    LoginRes login(LoginReq req);
    String activeUser(ActiveUserReq req);
    boolean verifyToken(String token);
    void logout(HttpServletRequest request);
}

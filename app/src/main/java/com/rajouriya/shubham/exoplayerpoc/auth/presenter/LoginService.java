package com.rajouriya.shubham.exoplayerpoc.auth.presenter;

public interface LoginService {
    public void onLoginSuccess(String userName);
    public void onLoginFaliuir(String msg);
    public void ifAlreadyActiveUser(String msg);
}

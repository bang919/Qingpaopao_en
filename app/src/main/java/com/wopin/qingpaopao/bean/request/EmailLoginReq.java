package com.wopin.qingpaopao.bean.request;

import com.wopin.qingpaopao.utils.EncryptionUtil;

import java.io.Serializable;

public class EmailLoginReq implements Serializable {

    private String email;
    private String userPwd;
    private String userName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return userPwd;
    }

    public void setPassword(String password) {
        this.userPwd = EncryptionUtil.md5(password);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

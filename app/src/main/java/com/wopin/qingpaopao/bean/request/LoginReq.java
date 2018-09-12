package com.wopin.qingpaopao.bean.request;

import com.wopin.qingpaopao.utils.EncryptionUtil;

import java.io.Serializable;

public class LoginReq implements Serializable {

    /**
     * phone : 15989082970
     * userPwd : 900150983cd24fb0d6963f7d28e17f72
     * userName : 15989082970
     * v_code : 1234
     */

    private String phone;
    private String userPwd;
    private String userName;
    private String v_code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getV_code() {
        return v_code;
    }

    public void setV_code(String v_code) {
        this.v_code = v_code;
    }
}

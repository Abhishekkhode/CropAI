package com.CropAI.CropAI.Dto;

import lombok.Data;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;

@Data
public class SignupRequest {
    private String phone;
    private String username;

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}



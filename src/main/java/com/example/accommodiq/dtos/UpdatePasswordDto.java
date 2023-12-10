package com.example.accommodiq.dtos;

import org.springframework.security.crypto.password.PasswordEncoder;

public class UpdatePasswordDto {
    private String oldPassword;
    private String newPassword;

    public UpdatePasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public UpdatePasswordDto() {
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void encode(PasswordEncoder passwordEncoder) {
        this.oldPassword = passwordEncoder.encode(this.oldPassword);
        this.newPassword = passwordEncoder.encode(this.newPassword);
    }
}

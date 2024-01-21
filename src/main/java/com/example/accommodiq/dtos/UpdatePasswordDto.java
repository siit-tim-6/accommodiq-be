package com.example.accommodiq.dtos;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UpdatePasswordDto {
    @NotNull(message = "Old password is required")
    private String oldPassword;
    @NotNull(message = "New password is required")
    private String newPassword;

    public UpdatePasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public UpdatePasswordDto() {
        super();
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

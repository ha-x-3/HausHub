package org.launchcode.homebase.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.launchcode.homebase.models.enums.Role;

public class RegisterFormDTO extends LoginFormDTO {

    @NotNull
    @NotBlank
    @Size(min = 2, max = 30, message = "Invalid name. Must be between 2 and 30 characters.")
    private String username;

    private String verifyPassword;

    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}


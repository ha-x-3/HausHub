package org.launchcode.homebase.models.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    ADMIN("ADMIN", "ROLE_ADMIN"),
    USER("USER", "ROLE_USER");

    private String name;
    private String authority;

    Role(String name, String authority) {
        this.name = name;
        this.authority = authority;
    }

    Role() {
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}

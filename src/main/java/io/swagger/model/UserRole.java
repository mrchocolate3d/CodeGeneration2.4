package io.swagger.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_EMPLOYEE,
    ROLE_CUSTOMER,
    ROLE_BANK;

    @Override
    public String getAuthority() {
        return name();
    }
}
